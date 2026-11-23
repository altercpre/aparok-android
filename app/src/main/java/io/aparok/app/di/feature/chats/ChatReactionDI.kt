package io.aparok.app.di.feature.chats

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import io.aparok.app.feature_chats.presentation.ChatReactionViewModel
import io.aparok.app.ui.chats.ChatReactionFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [ChatReactionDependencies::class],
    modules = [
        ChatReactionModule::class,
        ChatReactionModule.Declarations::class
    ]
)
@PerScreen
interface ChatReactionComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: ChatReactionViewModel.Params): Builder
        fun withDependencies(dependencies: ChatReactionDependencies): Builder
        fun build(): ChatReactionComponent
    }

    fun inject(fragment: ChatReactionFragment)
}

@Module
object ChatReactionModule {

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ChatReactionViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface ChatReactionDependencies : ComponentDependencies {
    fun dispatchers(): AppCoroutineDispatchers
    fun repo(): BlockRepository
    fun auth(): AuthRepository
    fun urlBuilder(): UrlBuilder
    fun members(): ActiveSpaceMemberSubscriptionContainer
}