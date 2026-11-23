package io.aparok.app.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.presentation.settings.DebugViewModel
import io.aparok.app.presentation.util.downloader.UriFileProvider
import io.aparok.app.ui.settings.DebugFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [DebugDependencies::class],
    modules = [
        DebugModule::class,
        DebugModule.Declarations::class
    ]
)
@PerScreen
interface DebugComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: DebugDependencies): DebugComponent
    }

    fun inject(fragment: DebugFragment)
}

@Module
object DebugModule {
    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: DebugViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface DebugDependencies : ComponentDependencies {
    fun path(): PathProvider
    fun auth(): AuthRepository
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun uriFileProvider(): UriFileProvider
    fun userSettingsRepository(): UserSettingsRepository
}