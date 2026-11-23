package io.aparok.app.di.feature.chats

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.emojifier.data.Emoji
import io.aparok.app.emojifier.data.EmojiProvider
import io.aparok.app.emojifier.suggest.EmojiSuggester
import io.aparok.app.feature_chats.presentation.SelectChatReactionViewModel
import io.aparok.app.ui.chats.SelectChatReactionFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SelectChatReactionDependencies::class],
    modules = [
        SelectChatReactionModule::class,
        SelectChatReactionModule.Declarations::class
    ]
)
@PerScreen
interface SelectChatReactionComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: SelectChatReactionViewModel.Params): Builder
        fun withDependencies(dependencies: SelectChatReactionDependencies): Builder
        fun build(): SelectChatReactionComponent
    }

    fun getViewModel(): SelectChatReactionViewModel
    fun inject(fragment: SelectChatReactionFragment)
}

@Module
object SelectChatReactionModule {
    @Provides
    @PerScreen
    @JvmStatic
    fun provideEmojiProvider(): EmojiProvider = Emoji

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: SelectChatReactionViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface SelectChatReactionDependencies : ComponentDependencies {
    fun dispatchers(): AppCoroutineDispatchers
    fun suggester(): EmojiSuggester
    fun repo(): BlockRepository
    fun auth(): AuthRepository
    fun prefs(): UserSettingsRepository
}