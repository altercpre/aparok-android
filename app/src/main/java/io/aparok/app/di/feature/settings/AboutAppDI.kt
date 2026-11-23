package io.aparok.app.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.interactor.GetAccount
import io.aparok.app.domain.auth.interactor.GetLibraryVersion
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.ui.settings.AboutAppFragment
import io.aparok.app.ui_settings.about.AboutAppViewModel
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    modules = [AboutAppModule::class],
    dependencies = [AboutAppDependencies::class]
)
@AboutAppScope
interface AboutAppComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: AboutAppDependencies): AboutAppComponent
    }

    fun inject(fragment: AboutAppFragment)
}

@Module
object AboutAppModule {

    @JvmStatic
    @Provides
    @AboutAppScope
    fun provideGetAccountUseCase(
        repo: AuthRepository,
        dispatcher: AppCoroutineDispatchers
    ): GetAccount = GetAccount(repo = repo, dispatcher = dispatcher)

    @JvmStatic
    @Provides
    @AboutAppScope
    fun provideGetVersion(
        repo: AuthRepository
    ): GetLibraryVersion = GetLibraryVersion(repo)

    @Module
    interface Declarations {

        @AboutAppScope
        @Binds
        fun bindViewModelFactory(
            factory: AboutAppViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface AboutAppDependencies : ComponentDependencies {
    fun authRepo(): AuthRepository
    fun configStorage(): ConfigStorage
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AboutAppScope