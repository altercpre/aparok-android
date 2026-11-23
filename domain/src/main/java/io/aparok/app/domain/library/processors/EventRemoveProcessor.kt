package io.aparok.app.domain.library.processors

import io.aparok.app.core_models.SubscriptionEvent
import io.aparok.app.domain.library.SubscriptionObject

class EventRemoveProcessor : SubscriptionEventProcessor<SubscriptionEvent.Remove> {

    override fun process(
        event: SubscriptionEvent.Remove,
        dataItems: MutableList<SubscriptionObject>
    ): MutableList<SubscriptionObject> = with(dataItems) {
        retainAll {
            it.id != event.target
        }
        return this
    }

}