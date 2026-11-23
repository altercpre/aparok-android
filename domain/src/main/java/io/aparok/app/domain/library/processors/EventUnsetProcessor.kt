package io.aparok.app.domain.library.processors

import io.aparok.app.core_models.SubscriptionEvent
import io.aparok.app.domain.library.SubscriptionObject
import io.aparok.app.domain.`object`.unset

class EventUnsetProcessor : SubscriptionEventProcessor<SubscriptionEvent.Unset> {

    override fun process(
        event: SubscriptionEvent.Unset,
        dataItems: MutableList<SubscriptionObject>
    ): MutableList<SubscriptionObject> = with(dataItems) {
        for (index in indices) {
            val item = get(index)
            if (item.id == event.target) {
                set(index, SubscriptionObject(item.id, item.objectWrapper?.unset(event.keys)))
            }
        }
        return this
    }

}