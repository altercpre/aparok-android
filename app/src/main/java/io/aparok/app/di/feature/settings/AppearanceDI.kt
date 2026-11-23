package io.aparok.app.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.theme.GetTheme
import io.aparok.app.domain.theme.SetTheme
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.ui.settings.AppearanceFragment
import io.aparok.app.ui_settings.appearance.AppearanceViewModel
import dagger.Component
import io.aparok.app.ui_settings.appearance.ThemeApplicator
import io.aparok.app.ui_settings.appearance.ThemeApplicatorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [AppearanceDependencies::class],
    modules = [
        AppearanceModule::class,
        AppearanceModule.Declarations::class
    ]
)
@PerScreen
interface AppearanceComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: AppearanceDependencies): AppearanceComponent
    }

    fun inject(fragment: AppearanceFragment)
}

@Module
object AppearanceModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetThemeUseCase(repo: UserSettingsRepository): GetTheme = GetTheme(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetThemeUseCase(repo: UserSettingsRepository): SetTheme = SetTheme(repo)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: AppearanceViewModel.Factory
        ): ViewModelProvider.Factory

        @PerScreen
        @Binds
        fun bindThemeApplicator(applicator: ThemeApplicatorImpl): ThemeApplicator
    }
}

interface AppearanceDependencies : ComponentDependencies {
    fun userUserSettingsRepository(): UserSettingsRepository
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
}