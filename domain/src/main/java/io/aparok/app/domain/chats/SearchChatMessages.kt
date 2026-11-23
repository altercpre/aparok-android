package io.aparok.app.domain.chats

import io.aparok.app.core_models.Command
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SearchChatMessages @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.ChatCommand.SearchMessages, Command.ChatCommand.SearchMessages.Response>(dispatchers.io) {
    override suspend fun doWork(params: Command.ChatCommand.SearchMessages): Command.ChatCommand.SearchMessages.Response {
        return repo.searchChatMessages(params)
    }
}
