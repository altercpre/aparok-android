package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.download.DownloadFile
import io.aparok.app.domain.download.Downloader
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.presentation.media.MediaViewModel
import io.aparok.app.ui.media.MediaActivity
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Component(
    dependencies = [MediaDependencies::class],
    modules = [
        MediaModule::class,
        MediaModule.Declarations::class
    ]
)
@PerScreen
interface MediaComponent {

    @Component.Factory
    interface Factory {
        fun create(dependency: MediaDependencies): MediaComponent
    }

    fun inject(activity: MediaActivity)
}

@Module
object MediaModule {

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun factory(factory: MediaViewModel.Factory): ViewModelProvider.Factory
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDownloadFileUseCase(
        downloader: Downloader
    ): DownloadFile = DownloadFile(
        downloader = downloader,
        context = Dispatchers.IO
    )
}

interface MediaDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun downloader(): Downloader
    fun userSettings(): UserSettingsRepository
}