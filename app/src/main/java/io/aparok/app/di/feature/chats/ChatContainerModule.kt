package io.aparok.app.di.feature.chats

import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.chats.ChatContainer
import io.aparok.app.domain.chats.ChatEventChannel
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

/** Shared by chats and discussions, which both collect a ChatContainer. */
@Module
object ChatContainerModule {
    @JvmStatic
    @Provides
    @PerScreen
    fun provideChatContainer(
        repo: BlockRepository,
        channel: ChatEventChannel,
        logger: Logger,
        subscription: StorelessSubscriptionContainer,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        dispatchers: AppCoroutineDispatchers
    ): ChatContainer = ChatContainer(
        repo, channel, logger, subscription,
        readScope = CoroutineScope(scope.coroutineContext + dispatchers.io)
    )
}
