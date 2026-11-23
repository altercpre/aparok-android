package io.aparok.app.data.auth.event

import io.aparok.app.core_models.Event
import kotlinx.coroutines.flow.Flow

interface EventRemoteChannel {
    fun observeEvents(context: String? = null): Flow<List<Event>>
}