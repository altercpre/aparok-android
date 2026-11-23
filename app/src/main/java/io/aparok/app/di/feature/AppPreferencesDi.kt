package io.aparok.app.di.feature

import android.content.Context
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.debugging.DebugExportLogs
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.networkmode.GetNetworkMode
import io.aparok.app.domain.networkmode.SetNetworkMode
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.settings.PreferencesViewModel
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.util.networkModeCopyFileToCacheDirectory
import io.aparok.app.presentation.util.downloader.UriFileProvider
import io.aparok.app.ui.settings.system.PreferenceFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [AppPreferencesDependencies::class],
    modules = [AppPreferencesModule::class]
)
@PerScreen
interface AppPreferencesComponent {

    @Component.Factory
    interface Factory {
        fun create(dependency: AppPreferencesDependencies): AppPreferencesComponent
    }

    fun inject(fragment: PreferenceFragment)
}

@Module
object AppPreferencesModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyFileToCache(
        context: Context
    ): CopyFileToCacheDirectory = networkModeCopyFileToCacheDirectory(context)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideLogsExport(
        repository: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): DebugExportLogs = DebugExportLogs(repository, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewModelFactory(
        copyFileToCacheDirectory: CopyFileToCacheDirectory,
        getNetworkMode: GetNetworkMode,
        setNetworkMode: SetNetworkMode,
        analytics: Analytics,
        debugExportLogs: DebugExportLogs,
        pathProvider: PathProvider,
        urlFileProvider: UriFileProvider
    ): PreferencesViewModel.Factory = PreferencesViewModel.Factory(
        copyFileToCacheDirectory = copyFileToCacheDirectory,
        getNetworkMode = getNetworkMode,
        setNetworkMode = setNetworkMode,
        analytics = analytics,
        debugExportLogs = debugExportLogs,
        pathProvider = pathProvider,
        uriFileProvider = urlFileProvider
    )
}

interface AppPreferencesDependencies : ComponentDependencies {
    fun context(): Context
    fun dispatchers(): AppCoroutineDispatchers
    fun authRepository(): AuthRepository
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun pathProvider(): PathProvider
    fun provideUriFileProvider(): UriFileProvider
}