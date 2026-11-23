package io.aparok.app.di.feature.sets;

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.objects.options.GetOptions
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.sets.ObjectSetDatabase
import io.aparok.app.presentation.sets.filter.FilterViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.sets.modals.filter.CreateFilterFromInputFieldValueFragment
import io.aparok.app.ui.sets.modals.filter.CreateFilterFromSelectedValueFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [CreateFilterModule::class])
@PerModal
interface CreateFilterSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: CreateFilterModule): Builder
        fun build(): CreateFilterSubComponent
    }

    fun inject(fragment: CreateFilterFromSelectedValueFragment)
    fun inject(fragment: CreateFilterFromInputFieldValueFragment)
    fun createPickConditionComponent(): PickFilterConditionSubComponent.Builder
}

@Module
object CreateFilterModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        searchObjects: SearchObjects,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        storeOfRelations: StoreOfRelations,
        objectSetDatabase: ObjectSetDatabase,
        analytics: Analytics,
        getOptions: GetOptions,
        spaceManager: SpaceManager,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): FilterViewModel.Factory = FilterViewModel.Factory(
        objectState = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        searchObjects = searchObjects,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        storeOfRelations = storeOfRelations,
        objectSetDatabase = objectSetDatabase,
        analytics = analytics,
        getOptions = getOptions,
        spaceManager = spaceManager,
        fieldParser = fieldParser,
        spaceViews = spaceViews,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )
}
