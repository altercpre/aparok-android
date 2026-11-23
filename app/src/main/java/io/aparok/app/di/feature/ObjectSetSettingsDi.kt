package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.DeleteRelationFromDataView
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.relations.ObjectSetSettingsViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.sets.modals.ObjectSetSettingsFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ObjectSetSettingsModule::class])
@PerModal
interface ObjectSetSettingsSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ObjectSetSettingsModule): Builder
        fun build(): ObjectSetSettingsSubComponent
    }

    fun inject(fragment: ObjectSetSettingsFragment)
}

@Module
object ObjectSetSettingsModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        store: StoreOfRelations,
        analytics: Analytics,
        deleteRelationFromDataView: DeleteRelationFromDataView,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        spaceManager: SpaceManager
    ): ObjectSetSettingsViewModel.Factory = ObjectSetSettingsViewModel.Factory(
        objectState = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        analytics = analytics,
        store = store,
        deleteRelationFromDataView = deleteRelationFromDataView,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        spaceManager = spaceManager
    )
}