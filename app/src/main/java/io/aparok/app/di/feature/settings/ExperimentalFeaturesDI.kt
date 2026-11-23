package io.aparok.app.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.presentation.settings.ExperimentalFeaturesViewModel
import io.aparok.app.ui.settings.ExperimentalFeaturesFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [ExperimentalFeaturesDependencies::class],
    modules = [
        ExperimentalFeaturesModule::class,
        ExperimentalFeaturesModule.Declarations::class
    ]
)
@PerScreen
interface ExperimentalFeaturesComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: ExperimentalFeaturesDependencies): ExperimentalFeaturesComponent
    }

    fun inject(fragment: ExperimentalFeaturesFragment)
}

@Module
object ExperimentalFeaturesModule {
    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ExperimentalFeaturesViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface ExperimentalFeaturesDependencies : ComponentDependencies {
    fun userSettingsRepository(): UserSettingsRepository
}
