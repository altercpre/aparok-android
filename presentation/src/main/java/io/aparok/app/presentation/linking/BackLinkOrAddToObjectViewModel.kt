package io.aparok.app.presentation.linking

import androidx.lifecycle.viewModelScope
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.domain.base.Resultat
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.navigation.DefaultObjectView
import io.aparok.app.core_models.ui.ObjectIcon
import io.aparok.app.presentation.search.ObjectSearchConstants
import io.aparok.app.presentation.search.ObjectSearchViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class BackLinkOrAddToObjectViewModel(
    private val vmParams: VmParams,
    urlBuilder: UrlBuilder,
    searchObjects: SearchObjects,
    analytics: Analytics,
    analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    fieldParser: FieldParser,
    storeOfObjectTypes: StoreOfObjectTypes,
    spaceViews: SpaceViewSubscriptionContainer
) : ObjectSearchViewModel(
    vmParams = vmParams,
    urlBuilder = urlBuilder,
    searchObjects = searchObjects,
    analytics = analytics,
    analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
    fieldParser = fieldParser,
    storeOfObjectTypes = storeOfObjectTypes,
    spaceViews = spaceViews
) {

    private val _commands = MutableSharedFlow<Command>(replay = 0)
    val commands: SharedFlow<Command> = _commands

    /**
     * Adding a source object as a backlink is only possible in objects
     * with these Layouts and also in Collection objects.
     */
    private val supported = listOf(
        ObjectType.Layout.BASIC,
        ObjectType.Layout.PROFILE,
        ObjectType.Layout.TODO,
        ObjectType.Layout.NOTE,
        ObjectType.Layout.COLLECTION
    )

    override suspend fun getSearchObjectsParams(ignore: Id?): SearchObjects.Params {
        return SearchObjects.Params(
            space = vmParams.space,
            limit = SEARCH_LIMIT,
            filters = ObjectSearchConstants.filtersBackLinkOrAddToObject(
                ignore = ignore
            ),
            sorts = ObjectSearchConstants.sortBackLinkOrAddToObject,
            fulltext = EMPTY_QUERY,
            keys = ObjectSearchConstants.defaultKeys
        )
    }

    override fun onObjectClicked(view: DefaultObjectView) {
        sendSearchResultEvent(view.id)
        viewModelScope.launch {
            _commands.emit(
                Command.CreateBacklink(
                    id = view.id,
                    name = view.name,
                    layout = view.layout,
                    icon = view.icon
                )
            )
        }
    }

    override fun onDialogCancelled() {
        viewModelScope.launch {
            _commands.emit(Command.Exit)
        }
    }

    override suspend fun setObjects(data: List<ObjectWrapper.Basic>) {
        objects.value = Resultat.success(
            data.filter {
                supported.contains(it.layout)
            }
        )
    }

    sealed class Command {
        data object Exit : Command()
        data class CreateBacklink(
            val id: Id,
            val name: String,
            val layout: ObjectType.Layout?,
            val icon: ObjectIcon
        ) : Command()
    }
}