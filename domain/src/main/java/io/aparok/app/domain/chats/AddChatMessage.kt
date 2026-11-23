package io.aparok.app.domain.chats

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * returns message ID and payload commands.
 */
class AddChatMessage @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.ChatCommand.AddMessage, Pair<Id, List<Event.Command.Chats>>>(dispatchers.io) {

    override suspend fun doWork(params: Command.ChatCommand.AddMessage): Pair<Id, List<Event.Command.Chats>> {
        return repo.addChatMessage(params)
    }
}