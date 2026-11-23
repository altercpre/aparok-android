package io.aparok.app.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.presentation.device.BuildProvider
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.account.DeleteAccount
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.device.ClearFileCache
import io.aparok.app.domain.download.FileAutoDownloadSetLimit
import io.aparok.app.domain.download.FileSetAutoDownload
import io.aparok.app.domain.workspace.FileLimitsEventChannel
import io.aparok.app.domain.workspace.InterceptFileLimitEvents
import io.aparok.app.domain.workspace.SpacesUsageInfo
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.settings.FilesStorageViewModel
import io.aparok.app.ui.settings.FilesStorageFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@PerScreen
@Component(
    dependencies = [FilesStorageDependencies::class],
    modules = [
        FilesStorageModule::class,
        FilesStorageModule.Declarations::class
    ]
)
interface FilesStorageComponent {

    @Component.Builder
    interface Builder {

        fun withDependencies(dependency: FilesStorageDependencies): Builder
        fun build(): FilesStorageComponent
    }

    fun inject(fragment: FilesStorageFragment)
}

@Module
object FilesStorageModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun clearFileCache(repo: BlockRepository): ClearFileCache = ClearFileCache(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSpaceUsage(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SpacesUsageInfo = SpacesUsageInfo(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileSetAutoDownload(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): FileSetAutoDownload = FileSetAutoDownload(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileAutoDownloadSetLimit(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): FileAutoDownloadSetLimit = FileAutoDownloadSetLimit(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileLimitEvents(
        channel: FileLimitsEventChannel,
        dispatchers: AppCoroutineDispatchers
    ): InterceptFileLimitEvents = InterceptFileLimitEvents(channel, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDeleteAccountUseCase(
        repo: AuthRepository
    ): DeleteAccount = DeleteAccount(repo = repo)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: FilesStorageViewModel.Factory): ViewModelProvider.Factory
    }
}

interface FilesStorageDependencies : ComponentDependencies {
    fun blockRepo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun fileEventsChannel(): FileLimitsEventChannel
    fun buildProvider(): BuildProvider
    fun authRepo(): AuthRepository
    fun logger(): Logger
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
}