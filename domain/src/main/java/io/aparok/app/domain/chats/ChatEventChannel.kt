package io.aparok.app.domain.chats

import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import kotlinx.coroutines.flow.Flow

interface ChatEventChannel {
    fun observe(chat: Id): Flow<List<Event.Command.Chats>>
    fun subscribe(subscribe: Id): Flow<List<Event.Command.Chats>>
}