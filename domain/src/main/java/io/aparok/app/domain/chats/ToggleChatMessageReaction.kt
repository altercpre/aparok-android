package io.aparok.app.domain.chats

import io.aparok.app.core_models.Command
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class ToggleChatMessageReaction @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.ChatCommand.ToggleMessageReaction, Unit>(dispatchers.io) {
    override suspend fun doWork(params: Command.ChatCommand.ToggleMessageReaction) {
        return repo.toggleChatMessageReaction(command = params)
    }
}