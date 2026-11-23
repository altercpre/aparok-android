package io.aparok.app.domain.library.processors

import io.aparok.app.core_models.SubscriptionEvent
import io.aparok.app.domain.library.SubscriptionObject

interface SubscriptionEventProcessor<T: SubscriptionEvent> {
    fun process(
        event: T,
        dataItems: MutableList<SubscriptionObject>
    ): MutableList<SubscriptionObject>
}