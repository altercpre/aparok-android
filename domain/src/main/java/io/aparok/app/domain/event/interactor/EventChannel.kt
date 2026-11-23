package io.aparok.app.domain.event.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Event
import kotlinx.coroutines.flow.Flow

interface EventChannel {
    fun observeEvents(context: Id? = null): Flow<List<Event>>
}