package io.aparok.app.di.feature.widgets

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.presentation.widgets.CreateChatObjectViewModel
import io.aparok.app.ui.widgets.CreateChatObjectFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [CreateChatObjectDependencies::class],
    modules = [
        CreateChatObjectModule::class,
        CreateChatObjectModule.Declarations::class
    ]
)
@PerScreen
interface CreateChatObjectComponent {
    @Component.Factory
    interface Builder {
        fun create(
            @BindsInstance vmParams: CreateChatObjectViewModel.VmParams,
            dependencies: CreateChatObjectDependencies
        ): CreateChatObjectComponent
    }

    fun inject(fragment: CreateChatObjectFragment)
}

@Module
object CreateChatObjectModule {
    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: CreateChatObjectViewModel.Factory): ViewModelProvider.Factory
    }
}

interface CreateChatObjectDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun settings(): UserSettingsRepository
}
