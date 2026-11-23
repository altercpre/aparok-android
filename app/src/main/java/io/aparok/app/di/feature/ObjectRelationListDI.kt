package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.primitives.SetObjectTypeRecommendedFields
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.relations.AddToFeaturedRelations
import io.aparok.app.domain.relations.DeleteRelationFromObject
import io.aparok.app.domain.relations.RemoveFromFeaturedRelations
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.objects.LockedStateProvider
import io.aparok.app.presentation.relations.ObjectRelationListViewModelFactory
import io.aparok.app.presentation.relations.RelationListViewModel
import io.aparok.app.presentation.relations.providers.ObjectRelationListProvider
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.primitives.ObjectFieldsFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [ObjectRelationListModule::class])
@PerModal
interface ObjectRelationListComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withVmParams(vmParams: RelationListViewModel.VmParams) : Builder
        fun module(module: ObjectRelationListModule): Builder
        fun build(): ObjectRelationListComponent
    }

    fun inject(fragment: ObjectFieldsFragment)
}

@Module
object ObjectRelationListModule {
    @JvmStatic
    @Provides
    @PerModal
    fun factory(
        vmParams: RelationListViewModel.VmParams,
        lockedStateProvider: LockedStateProvider,
        objectRelationListProvider: ObjectRelationListProvider,
        urlBuilder: UrlBuilder,
        dispatcher: Dispatcher<Payload>,
        updateDetail: UpdateDetail,
        addToFeaturedRelations: AddToFeaturedRelations,
        removeFromFeaturedRelations: RemoveFromFeaturedRelations,
        deleteRelationFromObject: DeleteRelationFromObject,
        analytics: Analytics,
        storeOfRelations: StoreOfRelations,
        storeOfObjectTypes: StoreOfObjectTypes,
        addRelationToObject: AddRelationToObject,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        userPermissionProvider: UserPermissionProvider,
        setObjectTypeRecommendedFields: SetObjectTypeRecommendedFields
    ): ObjectRelationListViewModelFactory {
        return ObjectRelationListViewModelFactory(
            vmParams = vmParams,
            lockedStateProvider = lockedStateProvider,
            objectRelationListProvider = objectRelationListProvider,
            urlBuilder = urlBuilder,
            dispatcher = dispatcher,
            updateDetail = updateDetail,
            addToFeaturedRelations = addToFeaturedRelations,
            removeFromFeaturedRelations = removeFromFeaturedRelations,
            deleteRelationFromObject = deleteRelationFromObject,
            analytics = analytics,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            addRelationToObject = addRelationToObject,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            fieldParser = fieldParser,
            userPermissionProvider = userPermissionProvider,
            setObjectTypeRecommendedFields = setObjectTypeRecommendedFields
        )
    }

    @JvmStatic
    @Provides
    @PerModal
    fun addToFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): AddToFeaturedRelations =
        AddToFeaturedRelations(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun removeFromFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): RemoveFromFeaturedRelations =
        RemoveFromFeaturedRelations(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun deleteRelationFromObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationFromObject =
        DeleteRelationFromObject(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun provideTypeSetRecommendedFields(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectTypeRecommendedFields = SetObjectTypeRecommendedFields(repo, dispatchers)
}