package io.aparok.app.presentation.relations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.analytics.base.EventsDictionary
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.fold
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.relations.GetRelations
import io.aparok.app.domain.workspace.AddObjectToWorkspace
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.extension.sendAnalyticsRelationEvent
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider
import io.aparok.app.presentation.util.Dispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RelationAddToObjectViewModel(
    relationsProvider: ObjectRelationProvider,
    val vmParams: VmParams,
    private val addRelationToObject: AddRelationToObject,
    private val dispatcher: Dispatcher<Payload>,
    private val analytics: Analytics,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    val storeOfRelations: StoreOfRelations,
    appCoroutineDispatchers: AppCoroutineDispatchers,
    getRelations: GetRelations,
    addObjectToWorkspace: AddObjectToWorkspace,
) : RelationAddViewModelBase(
    vmParams = vmParams,
    relationsProvider = relationsProvider,
    appCoroutineDispatchers = appCoroutineDispatchers,
    getRelations = getRelations,
    addObjectToWorkspace = addObjectToWorkspace,
), AnalyticSpaceHelperDelegate by analyticSpaceHelperDelegate {

    val commands = MutableSharedFlow<Command>(replay = 0)

    fun onRelationSelected(
        ctx: Id,
        relation: Key,
        screenType: String
    ) {
        viewModelScope.launch {
            val params = AddRelationToObject.Params(
                ctx = ctx,
                relationKey = relation
            )
            addRelationToObject.async(params).fold(
                onSuccess = { payload ->
                    if (payload != null) dispatcher.send(payload)
                    commands.emit(Command.OnRelationAdd(relation = relation))
                    analytics.sendAnalyticsRelationEvent(
                        eventName = EventsDictionary.relationAdd,
                        storeOfRelations = storeOfRelations,
                        relationKey = relation,
                        type = screenType,
                        spaceParams = provideParams(vmParams.space.id)
                    )
                    isDismissed.value = true
                },
                onFailure = {
                    Timber.e(it, ERROR_MESSAGE)
                    _toasts.emit("$ERROR_MESSAGE: ${it.localizedMessage}")
                }
            )
        }
    }

    class Factory(
        private val vmParams: VmParams,
        private val storeOfRelations: StoreOfRelations,
        private val addRelationToObject: AddRelationToObject,
        private val dispatcher: Dispatcher<Payload>,
        private val analytics: Analytics,
        private val relationsProvider: ObjectRelationProvider,
        private val appCoroutineDispatchers: AppCoroutineDispatchers,
        private val getRelations: GetRelations,
        private val addObjectToWorkspace: AddObjectToWorkspace,
        private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RelationAddToObjectViewModel(
                vmParams = vmParams,
                relationsProvider = relationsProvider,
                addRelationToObject = addRelationToObject,
                storeOfRelations = storeOfRelations,
                dispatcher = dispatcher,
                analytics = analytics,
                appCoroutineDispatchers = appCoroutineDispatchers,
                getRelations = getRelations,
                addObjectToWorkspace = addObjectToWorkspace,
                analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
            ) as T
        }
    }

    sealed class Command {
        data class OnRelationAdd(val relation: Id) : Command()
    }
}