package io.aparok.app.di.feature.spaces

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.search.ProfileSubscriptionManager
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.spaces.CreateSpaceViewModel
import io.aparok.app.ui.spaces.CreateSpaceFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [CreateSpaceDependencies::class],
    modules = [
        CreateSpaceModule::class,
        CreateSpaceModule.Declarations::class
    ]
)
@PerScreen
interface CreateSpaceComponent {
    @Component.Factory
    interface Builder {
        fun create(
            @BindsInstance vmParams: CreateSpaceViewModel.VmParams,
            dependencies: CreateSpaceDependencies
        ): CreateSpaceComponent
    }

    fun inject(fragment: CreateSpaceFragment)
}

@Module
object CreateSpaceModule {
    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: CreateSpaceViewModel.Factory): ViewModelProvider.Factory
    }
}

interface CreateSpaceDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun userSettingsRepo(): UserSettingsRepository
    fun permissions(): UserPermissionProvider
    fun profileContainer(): ProfileSubscriptionManager
    fun urlBuilder(): UrlBuilder
}