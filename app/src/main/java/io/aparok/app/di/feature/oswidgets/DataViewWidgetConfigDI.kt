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
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.feature_os_widgets.persistence.DataViewItemsFetcher
import io.aparok.app.feature_os_widgets.persistence.OsWidgetDataViewEntity
import io.aparok.app.feature_os_widgets.persistence.OsWidgetsDataStore
import io.aparok.app.feature_os_widgets.presentation.DataViewWidgetConfigStore
import io.aparok.app.feature_os_widgets.presentation.DataViewWidgetUpdater
import io.aparok.app.feature_os_widgets.presentation.DataViewWidgetConfigViewModel
import io.aparok.app.feature_os_widgets.ui.OsDataViewWidgetUpdater
import io.aparok.app.ui.oswidgets.DataViewWidgetConfigActivity
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [DataViewWidgetConfigDependencies::class],
    modules = [DataViewWidgetConfigModule::class]
)
@PerScreen
interface DataViewWidgetConfigComponent {

    fun inject(activity: DataViewWidgetConfigActivity)

    @Component.Factory
    interface Factory {
        fun create(dependencies: DataViewWidgetConfigDependencies): DataViewWidgetConfigComponent
    }
}

@Module
object DataViewWidgetConfigModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDataStore(context: Context): DataViewWidgetConfigStore {
        val appContext = context.applicationContext
        val dataStore = OsWidgetsDataStore(appContext)
        return object : DataViewWidgetConfigStore {
            override suspend fun save(config: OsWidgetDataViewEntity) {
                dataStore.saveDataViewConfig(config)
            }
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDataViewWidgetUpdater(context: Context): DataViewWidgetUpdater {
        val appContext = context.applicationContext
        return DataViewWidgetUpdater { appWidgetId ->
            OsDataViewWidgetUpdater.update(appContext, appWidgetId)
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDataViewItemsFetcher(
        getObject: GetObject,
        searchObjects: SearchObjects,
        blockRepository: BlockRepository
    ): DataViewItemsFetcher = DataViewItemsFetcher(
        getObject = getObject,
        searchObjects = searchObjects,
        blockRepository = blockRepository
    )

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
    fun provideDataViewWidgetViewModelFactory(
        spaceViews: SpaceViewSubscriptionContainer,
        urlBuilder: UrlBuilder,
        searchObjects: SearchObjects,
        getObject: GetObject,
        dataStore: DataViewWidgetConfigStore,
        itemsFetcher: DataViewItemsFetcher,
        widgetUpdater: DataViewWidgetUpdater,
        launchWallet: LaunchWallet,
        launchAccount: LaunchAccount
    ): DataViewWidgetConfigViewModel.Factory = DataViewWidgetConfigViewModel.Factory(
        spaceViews = spaceViews,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        getObject = getObject,
        dataStore = dataStore,
        itemsFetcher = itemsFetcher,
        widgetUpdater = widgetUpdater,
        launchWallet = launchWallet,
        launchAccount = launchAccount
    )
}

interface DataViewWidgetConfigDependencies : ComponentDependencies {
    fun context(): Context
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun urlBuilder(): UrlBuilder
    fun searchObjects(): SearchObjects
    fun blockRepository(): BlockRepository
    fun userSettingsRepository(): UserSettingsRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun authRepository(): AuthRepository
    fun pathProvider(): PathProvider
    fun configStorage(): ConfigStorage
    fun spaceManager(): SpaceManager
    fun metricsProvider(): InitialParamsProvider
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}
