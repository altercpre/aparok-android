package io.aparok.app.di.feature.onboarding

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.presentation.onboarding.OnboardingViewModel
import io.aparok.app.presentation.util.downloader.UriFileProvider
import io.aparok.app.ui.onboarding.OnboardingFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [OnboardingDependencies::class],
    modules = [
        OnboardingModule::class,
        OnboardingModule.Declarations::class
    ]
)
@PerScreen
interface OnboardingComponent {
    @Component.Factory
    interface Builder {
        fun create(dependencies: OnboardingDependencies): OnboardingComponent
    }

    fun inject(fragment: OnboardingFragment)
}

@Module
object OnboardingModule {
    @Module
    interface Declarations {
        @Binds
        @AuthScreenScope
        fun bindViewModelFactory(factory: OnboardingViewModel.Factory): ViewModelProvider.Factory
    }
}

interface OnboardingDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun context(): Context
    fun uriFileProvider(): UriFileProvider
}