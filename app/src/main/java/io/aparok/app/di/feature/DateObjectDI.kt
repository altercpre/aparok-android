package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.GetDateObjectByTimestamp
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.relations.GetObjectRelationListById
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.spaces.ClearLastOpenedSpace
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.feature_date.viewmodel.DateObjectVMFactory
import io.aparok.app.feature_date.viewmodel.DateObjectVmParams
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.navigation.backstack.BackHistoryDelegate
import io.aparok.app.presentation.navigation.backstack.NavigationBackStackInspector
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.ui.date.DateObjectFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [DateObjectDependencies::class],
    modules = [
        DateObjectModule::class,
        DateObjectModule.Declarations::class
    ]
)
@PerScreen
interface DateObjectComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: DateObjectVmParams,
            dependencies: DateObjectDependencies
        ): DateObjectComponent
    }

    fun inject(fragment: DateObjectFragment)
}

@Module
object DateObjectModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun getObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        settings: UserSettingsRepository,
    ): GetObject = GetObject(
        repo = repo,
        dispatchers = dispatchers,
        settings = settings
    )

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
    fun searchObjects(
        repo: BlockRepository
    ): SearchObjects = SearchObjects(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun createObject(
        repo: BlockRepository,
        getDefaultObjectType: GetDefaultObjectType,
        dispatchers: AppCoroutineDispatchers,
    ): CreateObject = CreateObject(
        repo = repo,
        getDefaultObjectType = getDefaultObjectType,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRelationListWithValue(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetObjectRelationListById = GetObjectRelationListById(repository, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateDetailUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(repository, dispatchers)

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
            factory: DateObjectVMFactory
        ): ViewModelProvider.Factory

    }
}

interface DateObjectDependencies : ComponentDependencies {
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
    fun provideDateProvider(): DateProvider
    fun provideSpaceSyncAndP2PStatusProvider(): SpaceSyncAndP2PStatusProvider
    fun provideUserSettingsRepository(): UserSettingsRepository
    fun fieldParser(): FieldParser
    fun provideGetDateObjectByTimestamp(): GetDateObjectByTimestamp
    fun provideSpaceViews(): SpaceViewSubscriptionContainer
    fun spaceManager(): SpaceManager
    fun navigationBackStackInspector(): NavigationBackStackInspector
}