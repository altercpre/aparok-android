package io.aparok.app.data.auth.event

import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.domain.event.interactor.EventChannel
import kotlinx.coroutines.flow.Flow

class EventDataChannel(private val remote: EventRemoteChannel) : EventChannel {

    override fun observeEvents(context: Id?): Flow<List<Event>> = remote.observeEvents(context)

}