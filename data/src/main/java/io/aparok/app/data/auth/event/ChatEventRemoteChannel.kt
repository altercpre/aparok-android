package io.aparok.app.data.auth.event

import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.domain.chats.ChatEventChannel
import kotlinx.coroutines.flow.Flow

interface ChatEventRemoteChannel {
    fun observe(chat: Id): Flow<List<Event.Command.Chats>>
    fun subscribe(subscription: Id): Flow<List<Event.Command.Chats>>
    class Default(
        private val channel: ChatEventRemoteChannel
    ) : ChatEventChannel {

        override fun subscribe(subscribe: Id): Flow<List<Event.Command.Chats>> {
            return channel.subscribe(subscribe)
        }

        override fun observe(chat: Id): Flow<List<Event.Command.Chats>> {
            return channel.observe(chat)
        }
    }
}