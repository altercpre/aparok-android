package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.CrashReporter
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.data.auth.event.EventProcessMigrationDateChannel
import io.aparok.app.data.auth.event.EventProcessMigrationRemoteChannel
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.interactor.GetLastOpenedObject
import io.aparok.app.domain.auth.interactor.LaunchAccount
import io.aparok.app.domain.auth.interactor.LaunchWallet
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.launch.SetDefaultObjectType
import io.aparok.app.domain.deeplink.PendingIntentStore
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.page.CreateObjectByTypeAndTemplate
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.search.RelationsSubscriptionManager
import io.aparok.app.domain.spaces.SpaceDeletedStatusWatcher
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.templates.GetTemplates
import io.aparok.app.domain.workspace.EventProcessMigrationChannel
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.middleware.EventProxy
import io.aparok.app.middleware.interactor.EventProcessMigrationMiddlewareChannel
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.auth.account.MigrationHelperDelegate
import io.aparok.app.presentation.splash.SplashViewModelFactory
import io.aparok.app.ui.splash.SplashFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SplashDependencies::class],
    modules = [
        SplashModule::class,
        SplashModule.Declarations::class
    ]
)
@PerScreen
interface SplashComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: SplashDependencies): SplashComponent
    }

    fun inject(fragment: SplashFragment)
}

@Module
object SplashModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLaunchAccountUseCase(
        authRepository: AuthRepository,
        pathProvider: PathProvider,
        configStorage: ConfigStorage,
        spaceManager: SpaceManager,
        initialParamsProvider: InitialParamsProvider,
        userSettings: UserSettingsRepository,
        awaitAccountStartManager: AwaitAccountStartManager,
        preferredSpaceIdHolder: PreferredSpaceIdHolder
    ): LaunchAccount = LaunchAccount(
        repository = authRepository,
        pathProvider = pathProvider,
        configStorage = configStorage,
        spaceManager = spaceManager,
        initialParamsProvider = initialParamsProvider,
        settings = userSettings,
        awaitAccountStartManager = awaitAccountStartManager,
        preferredSpaceIdHolder = preferredSpaceIdHolder
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLaunchWalletUseCase(
        authRepository: AuthRepository,
        pathProvider: PathProvider
    ): LaunchWallet =
        LaunchWallet(
            repository = authRepository,
            pathProvider = pathProvider
        )

    @JvmStatic
    @Provides
    @PerScreen
    fun getLastOpenedObject(
        repo: BlockRepository,
        userSettingsRepository: UserSettingsRepository
    ): GetLastOpenedObject = GetLastOpenedObject(
        settings = userSettingsRepository,
        blockRepo = repo,
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideGetDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSetDefaultPageType(
        repo: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetDefaultObjectType = SetDefaultObjectType(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getCreateObject(
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
    fun provideGetTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates = GetTemplates(
        repo = repo,
        spaceManager = spaceManager,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchObjects(
        repo: BlockRepository
    ): io.aparok.app.domain.search.SearchObjects =
        io.aparok.app.domain.search.SearchObjects(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateObjectByType(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): CreateObjectByTypeAndTemplate = CreateObjectByTypeAndTemplate(repo, logger, dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: SplashViewModelFactory): ViewModelProvider.Factory

        @Binds
        @PerScreen
        fun bindMigrationHelperDelegate(
            impl: MigrationHelperDelegate.Impl
        ): MigrationHelperDelegate

        @Binds
        @PerScreen
        fun bindMigrationChannel(
            impl: EventProcessMigrationDateChannel
        ): EventProcessMigrationChannel

        @Binds
        @PerScreen
        fun bindMigrationRemoteChannel(
            impl: EventProcessMigrationMiddlewareChannel
        ): EventProcessMigrationRemoteChannel
    }

}

interface SplashDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun urlBuilder(): UrlBuilder
    fun analytics(): Analytics
    fun relationsSubscriptionManager(): RelationsSubscriptionManager
    fun authRepository(): AuthRepository
    fun pathProvider(): PathProvider
    fun featureToggles(): FeatureToggles
    fun configStorage(): ConfigStorage
    fun userSettingsRepository(): UserSettingsRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun crashReporter(): CrashReporter
    fun metricsProvider(): InitialParamsProvider
    fun spaceManager(): SpaceManager
    fun spaceStatusWatcher(): SpaceDeletedStatusWatcher
    fun localeProvider(): LocaleProvider
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun userPermissionProvider(): UserPermissionProvider
    fun analyticSpaceHelperDelegate(): AnalyticSpaceHelperDelegate
    fun globalSubscriptionManager(): GlobalSubscriptionManager
    fun logger(): Logger
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun eventProxy(): EventProxy
    fun deepLinkResolver(): DeepLinkResolver
    fun pendingIntentStore(): PendingIntentStore
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}
