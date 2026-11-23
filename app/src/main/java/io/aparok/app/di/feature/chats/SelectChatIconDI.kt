package io.aparok.app.di.feature.chats

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.emojifier.data.Emoji
import io.aparok.app.emojifier.data.EmojiProvider
import io.aparok.app.emojifier.suggest.EmojiSuggester
import io.aparok.app.feature_chats.presentation.SelectChatIconViewModel
import io.aparok.app.ui.chats.SelectChatIconFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SelectChatIconDependencies::class],
    modules = [
        SelectChatIconModule::class,
        SelectChatIconModule.Declarations::class
    ]
)
@PerScreen
interface SelectChatIconComponent {
    @Component.Builder
    interface Builder {
        fun withDependencies(dependencies: SelectChatIconDependencies): Builder
        fun build(): SelectChatIconComponent
    }

    fun getViewModel(): SelectChatIconViewModel
    fun inject(fragment: SelectChatIconFragment)
}

@Module
object SelectChatIconModule {
    @Provides
    @PerScreen
    @JvmStatic
    fun provideEmojiProvider(): EmojiProvider = Emoji

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: SelectChatIconViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface SelectChatIconDependencies : ComponentDependencies {
    fun dispatchers(): AppCoroutineDispatchers
    fun suggester(): EmojiSuggester
}
