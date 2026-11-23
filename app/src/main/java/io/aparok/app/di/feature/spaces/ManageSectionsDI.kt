package io.aparok.app.di.feature.spaces

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.spaces.ManageSectionsViewModel
import io.aparok.app.ui.settings.space.ManageSectionsFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [ManageSectionsDependencies::class],
    modules = [ManageSectionsModule.Bindings::class]
)
@PerScreen
interface ManageSectionsComponent {
    @Component.Factory
    interface Factory {
        fun create(
            dependencies: ManageSectionsDependencies
        ): ManageSectionsComponent
    }

    fun inject(fragment: ManageSectionsFragment)
}

@Module
object ManageSectionsModule {

    @Module
    interface Bindings {
        @PerScreen
        @Binds
        fun factory(factory: ManageSectionsViewModel.Factory): ViewModelProvider.Factory
    }
}

interface ManageSectionsDependencies : ComponentDependencies {
    fun spaceManager(): SpaceManager
    fun settings(): UserSettingsRepository
    fun dispatchers(): AppCoroutineDispatchers
}
