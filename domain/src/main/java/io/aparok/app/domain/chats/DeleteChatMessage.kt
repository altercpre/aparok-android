package io.aparok.app.domain.chats

import io.aparok.app.core_models.Command
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class DeleteChatMessage @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.ChatCommand.DeleteMessage, Unit>(dispatchers.io) {
    override suspend fun doWork(params: Command.ChatCommand.DeleteMessage) {
        return repo.deleteChatMessage(command = params)
    }
}