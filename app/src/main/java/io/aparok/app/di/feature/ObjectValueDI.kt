package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.`object`.DuplicateObject
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider
import io.aparok.app.presentation.relations.providers.ObjectValueProvider
import io.aparok.app.presentation.relations.value.`object`.ObjectValueViewModel
import io.aparok.app.presentation.relations.value.`object`.ObjectValueViewModelFactory
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.relations.value.ObjectValueFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

//region OBJECT (layout BASIC, TASK etc.)
@PerModal
@Subcomponent(
    modules = [ObjectValueObjectModule::class]
)
interface ObjectValueObjectComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: ObjectValueViewModel.ViewModelParams): Builder
        fun build(): ObjectValueObjectComponent
    }

    fun inject(fragment: ObjectValueFragment)
}

@Module
object ObjectValueObjectModule {

    @JvmStatic
    @PerModal
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        spaceManager: SpaceManager,
        params: ObjectValueViewModel.ViewModelParams,
        objectSearch: SearchObjects,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        objectListIsArchived: SetObjectListIsArchived,
        duplicateObject: DuplicateObject,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer
    ): ObjectValueViewModelFactory = ObjectValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        spaceManager = spaceManager,
        objectSearch = objectSearch,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        objectListIsArchived = objectListIsArchived,
        duplicateObject = duplicateObject,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )
}
//endregion

//region SET or COLLECTION (layout SET, COLLECTION )
@PerModal
@Subcomponent(
    modules = [ObjectValueSetModule::class]
)
interface ObjectValueSetComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: ObjectValueViewModel.ViewModelParams): Builder
        fun build(): ObjectValueSetComponent
    }

    fun inject(fragment: ObjectValueFragment)
}

@Module
object ObjectValueSetModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        spaceManager: SpaceManager,
        params: ObjectValueViewModel.ViewModelParams,
        objectSearch: SearchObjects,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        objectListIsArchived: SetObjectListIsArchived,
        duplicateObject: DuplicateObject,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer
    ): ObjectValueViewModelFactory = ObjectValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        spaceManager = spaceManager,
        objectSearch = objectSearch,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        objectListIsArchived = objectListIsArchived,
        duplicateObject = duplicateObject,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )
}
//endregion

//region DATA VIEW
@PerModal
@Subcomponent(
    modules = [ObjectValueDataViewModule::class]
)
interface ObjectValueDataViewComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: ObjectValueViewModel.ViewModelParams): Builder
        fun build(): ObjectValueDataViewComponent
    }

    fun inject(fragment: ObjectValueFragment)
}

@Module
object ObjectValueDataViewModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.DATA_VIEW_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        spaceManager: SpaceManager,
        params: ObjectValueViewModel.ViewModelParams,
        objectSearch: SearchObjects,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        objectListIsArchived: SetObjectListIsArchived,
        duplicateObject: DuplicateObject,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer
    ): ObjectValueViewModelFactory = ObjectValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        spaceManager = spaceManager,
        objectSearch = objectSearch,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        objectListIsArchived = objectListIsArchived,
        duplicateObject = duplicateObject,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )
}
//endregion