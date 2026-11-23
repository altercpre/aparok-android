package io.aparok.app.di.feature.oswidgets

import android.content.Context
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.interactor.LaunchAccount
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.auth.interactor.LaunchWallet
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.feature_os_widgets.persistence.OsWidgetIconCache
import io.aparok.app.feature_os_widgets.persistence.OsWidgetObjectShortcutEntity
import io.aparok.app.feature_os_widgets.persistence.OsWidgetsDataStore
import io.aparok.app.feature_os_widgets.presentation.ObjectShortcutIconCache
import io.aparok.app.feature_os_widgets.presentation.ObjectShortcutWidgetConfigStore
import io.aparok.app.feature_os_widgets.presentation.ObjectShortcutWidgetConfigViewModel
import io.aparok.app.feature_os_widgets.presentation.ObjectShortcutWidgetUpdater
import io.aparok.app.feature_os_widgets.ui.OsObjectShortcutWidgetUpdater
import io.aparok.app.ui.oswidgets.ObjectShortcutWidgetConfigActivity
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [ObjectShortcutWidgetConfigDependencies::class],
    modules = [ObjectShortcutWidgetConfigModule::class]
)
@PerScreen
interface ObjectShortcutWidgetConfigComponent {

    fun inject(activity: ObjectShortcutWidgetConfigActivity)

    @Component.Factory
    interface Factory {
        fun create(dependencies: ObjectShortcutWidgetConfigDependencies): ObjectShortcutWidgetConfigComponent
    }
}

@Module
object ObjectShortcutWidgetConfigModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectShortcutStore(context: Context): ObjectShortcutWidgetConfigStore {
        val appContext = context.applicationContext
        val dataStore = OsWidgetsDataStore(appContext)
        return object : ObjectShortcutWidgetConfigStore {
            override suspend fun save(config: OsWidgetObjectShortcutEntity) {
                dataStore.saveObjectShortcutConfig(config)
            }
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectShortcutIconCache(context: Context): ObjectShortcutIconCache {
        val appContext = context.applicationContext
        val iconCache = OsWidgetIconCache(appContext)
        return object : ObjectShortcutIconCache {
            override suspend fun cacheForWidget(url: String, appWidgetId: Int): String? {
                return iconCache.cacheShortcutIcon(
                    url = url,
                    widgetId = appWidgetId,
                    prefix = OsWidgetIconCache.PREFIX_OBJECT
                )
            }
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectShortcutWidgetUpdater(context: Context): ObjectShortcutWidgetUpdater {
        val appContext = context.applicationContext
        return ObjectShortcutWidgetUpdater { appWidgetId ->
            OsObjectShortcutWidgetUpdater.update(appContext, appWidgetId)
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideLaunchWallet(
        authRepository: AuthRepository,
        pathProvider: PathProvider
    ): LaunchWallet = LaunchWallet(
        repository = authRepository,
        pathProvider = pathProvider
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideLaunchAccount(
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
    @Provides
    @PerScreen
    fun provideObjectShortcutViewModelFactory(
        spaceViews: SpaceViewSubscriptionContainer,
        urlBuilder: UrlBuilder,
        searchObjects: SearchObjects,
        configStore: ObjectShortcutWidgetConfigStore,
        iconCache: ObjectShortcutIconCache,
        widgetUpdater: ObjectShortcutWidgetUpdater,
        launchWallet: LaunchWallet,
        launchAccount: LaunchAccount
    ): ObjectShortcutWidgetConfigViewModel.Factory = ObjectShortcutWidgetConfigViewModel.Factory(
        spaceViews = spaceViews,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        configStore = configStore,
        iconCache = iconCache,
        widgetUpdater = widgetUpdater,
        launchWallet = launchWallet,
        launchAccount = launchAccount
    )
}

interface ObjectShortcutWidgetConfigDependencies : ComponentDependencies {
    fun context(): Context
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun urlBuilder(): UrlBuilder
    fun searchObjects(): SearchObjects
    fun authRepository(): AuthRepository
    fun pathProvider(): PathProvider
    fun configStorage(): ConfigStorage
    fun spaceManager(): SpaceManager
    fun metricsProvider(): InitialParamsProvider
    fun userSettingsRepository(): UserSettingsRepository
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}
