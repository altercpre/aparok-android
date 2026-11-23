package io.aparok.app.di.feature.spaces

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.presentation.spaces.SpaceListViewModel
import io.aparok.app.ui.spaces.SpaceListFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [SpaceListDependencies::class],
    modules = [
        SpaceListModule::class,
        SpaceListModule.Declarations::class
    ]
)
@PerScreen
interface SpaceListComponent {
    @Component.Factory
    interface Builder {
        fun create(dependencies: SpaceListDependencies): SpaceListComponent
    }
    fun inject(fragment: SpaceListFragment)
}

@Module
object SpaceListModule {
    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: SpaceListViewModel.Factory): ViewModelProvider.Factory
    }
}

interface SpaceListDependencies : ComponentDependencies {
    fun spaceViewContainer(): SpaceViewSubscriptionContainer
    fun permissions(): UserPermissionProvider
    fun urlBuilder(): UrlBuilder
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
}