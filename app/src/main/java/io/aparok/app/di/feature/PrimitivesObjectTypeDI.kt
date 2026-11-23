package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.CreateFromScratch
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.interactor.UpdateText
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.dataview.SetDataViewProperties
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.event.interactor.EventChannel
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.DuplicateObjects
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.DeleteObjects
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.primitives.GetObjectTypeConflictingFields
import io.aparok.app.domain.primitives.SetObjectTypeHeaderRecommendedFields
import io.aparok.app.domain.primitives.SetObjectTypeRecommendedFields
import io.aparok.app.domain.relations.AddToFeaturedRelations
import io.aparok.app.domain.relations.CreateRelation
import io.aparok.app.domain.relations.RemoveFromFeaturedRelations
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.spaces.ClearLastOpenedSpace
import io.aparok.app.domain.types.CreateObjectType
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.feature_object_type.ui.ObjectTypeVmParams
import io.aparok.app.feature_object_type.viewmodel.CreateObjectTypeVMFactory
import io.aparok.app.feature_object_type.viewmodel.CreateTypeVmParams
import io.aparok.app.feature_object_type.viewmodel.ObjectTypeVMFactory
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.navigation.backstack.BackHistoryDelegate
import io.aparok.app.presentation.navigation.backstack.NavigationBackStackInspector
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.feature_properties.space.SpacePropertiesViewModel
import io.aparok.app.feature_properties.space.SpacePropertiesVmFactory
import io.aparok.app.presentation.types.SpaceTypesViewModel
import io.aparok.app.presentation.types.SpaceTypesVmFactory
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.providers.DefaultCoverImageHashProvider
import io.aparok.app.ui.primitives.CreateTypeFragment
import io.aparok.app.ui.primitives.ObjectTypeFieldsFragment
import io.aparok.app.ui.primitives.ObjectTypeFragment
import io.aparok.app.ui.primitives.SpacePropertiesFragment
import io.aparok.app.ui.primitives.SpaceTypesFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [ObjectTypeDependencies::class],
    modules = [
        ObjectTypeModule::class,
        ObjectTypeModule.Declarations::class
    ]
)
@PerScreen
interface ObjectTypeComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: ObjectTypeVmParams,
            dependencies: ObjectTypeDependencies
        ): ObjectTypeComponent
    }

    fun inject(fragment: ObjectTypeFragment)
    fun inject(fragment: ObjectTypeFieldsFragment)
}

@Module
object ObjectTypeModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDataViewProperties(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetDataViewProperties = SetDataViewProperties(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideStoreLessSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): StorelessSubscriptionContainer = StorelessSubscriptionContainer.Impl(
        repo = repo,
        channel = channel,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateDetailUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(repository, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun coverHashProvider(): CoverImageHashProvider = DefaultCoverImageHashProvider()

    @JvmStatic
    @PerScreen
    @Provides
    fun getDeleteObjects(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteObjects = DeleteObjects(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun getObjectTypeConflictingFields(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetObjectTypeConflictingFields = GetObjectTypeConflictingFields(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateObjectsListUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DuplicateObjects = DuplicateObjects(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTypeSetRecommendedFields(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectTypeRecommendedFields = SetObjectTypeRecommendedFields(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTypeSetHeaderRecommendedFields(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectTypeHeaderRecommendedFields =
        SetObjectTypeHeaderRecommendedFields(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddToFeaturedRelations(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): AddToFeaturedRelations = AddToFeaturedRelations(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRemoveFromFeaturedRelations(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RemoveFromFeaturedRelations = RemoveFromFeaturedRelations(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateBlockUseCase(
        repo: BlockRepository
    ): UpdateText = UpdateText(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBackHistoryDelegate(
        inspector: NavigationBackStackInspector,
        searchObjects: SearchObjects,
        fieldParser: FieldParser,
        dispatchers: AppCoroutineDispatchers
    ): BackHistoryDelegate = BackHistoryDelegate.Default(
        inspector = inspector,
        searchObjects = searchObjects,
        fieldParser = fieldParser,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideExitToVaultDelegate(
        spaceManager: SpaceManager,
        clearLastOpenedSpace: ClearLastOpenedSpace
    ): ExitToVaultDelegate = ExitToVaultDelegate.Default(
        spaceManager = spaceManager,
        clearLastOpenedSpace = clearLastOpenedSpace
    )

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ObjectTypeVMFactory
        ): ViewModelProvider.Factory
    }
}

interface ObjectTypeDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun subEventChannel(): SubscriptionEventChannel
    fun logger(): Logger
    fun localeProvider(): LocaleProvider
    fun config(): ConfigStorage
    fun userPermissionProvider(): UserPermissionProvider
    fun provideStoreOfRelations(): StoreOfRelations
    fun provideSpaceSyncAndP2PStatusProvider(): SpaceSyncAndP2PStatusProvider
    fun provideUserSettingsRepository(): UserSettingsRepository
    fun fieldParser(): FieldParser
    fun provideEventChannel(): EventChannel
    fun provideStringResourceProvider(): StringResourceProvider
    fun dispatcher(): Dispatcher<Payload>
    fun spaceManager(): SpaceManager
    fun navigationBackStackInspector(): NavigationBackStackInspector
}

//region Space Types Screen
@Component(
    dependencies = [SpaceTypesDependencies::class],
    modules = [
        SpaceTypesModule::class,
        SpaceTypesModule.Declarations::class
    ]
)
@PerScreen
interface SpaceTypesComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: SpaceTypesViewModel.VmParams,
            dependencies: SpaceTypesDependencies
        ): SpaceTypesComponent
    }

    fun inject(fragment: SpaceTypesFragment)
}

@Module
object SpaceTypesModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: SpaceTypesVmFactory
        ): ViewModelProvider.Factory
    }
}

interface SpaceTypesDependencies : ComponentDependencies {
    fun stringResourceProvider(): StringResourceProvider
    fun blockRepository(): BlockRepository
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun userPermissionProvider(): UserPermissionProvider
    fun fieldParser(): FieldParser
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
}
//endregion

//region Space Properties Screen
@Component(
    dependencies = [SpacePropertiesDependencies::class],
    modules = [
        SpacePropertiesModule::class,
        SpacePropertiesModule.Declarations::class
    ]
)
@PerScreen
interface SpacePropertiesComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: SpacePropertiesViewModel.VmParams,
            dependencies: SpacePropertiesDependencies
        ): SpacePropertiesComponent
    }

    fun inject(fragment: SpacePropertiesFragment)
}

@Module
object SpacePropertiesModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: SpacePropertiesVmFactory
        ): ViewModelProvider.Factory
    }
}

interface SpacePropertiesDependencies : ComponentDependencies {
    fun stringResourceProvider(): StringResourceProvider
    fun blockRepository(): BlockRepository
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun storeOfRelations(): StoreOfRelations
    fun userPermissionProvider(): UserPermissionProvider
    fun fieldParser(): FieldParser
}
//endregion

//region Create Type Screen
@Component(
    dependencies = [CreateObjectTypeDependencies::class],
    modules = [
        CreateObjectTypeModule::class,
        CreateObjectTypeModule.Declarations::class
    ]
)
@PerScreen
interface CreateObjectTypeComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: CreateTypeVmParams,
            dependencies: CreateObjectTypeDependencies
        )
                : CreateObjectTypeComponent
    }

    fun inject(fragment: CreateTypeFragment)
}

@Module
object CreateObjectTypeModule {

    @Provides
    @PerScreen
    @JvmStatic
    fun createObjectType(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateObjectType = CreateObjectType(repo, dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: CreateObjectTypeVMFactory
        ): ViewModelProvider.Factory
    }
}

interface CreateObjectTypeDependencies : ComponentDependencies {
    fun stringResourceProvider(): StringResourceProvider
    fun blockRepository(): BlockRepository
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
}
//endregion