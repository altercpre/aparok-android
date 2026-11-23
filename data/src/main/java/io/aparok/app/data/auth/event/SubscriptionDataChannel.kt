package io.aparok.app.data.auth.event

import io.aparok.app.core_models.Id
import io.aparok.app.domain.search.SubscriptionEventChannel

class SubscriptionDataChannel(
    private val remote: SubscriptionEventRemoteChannel
) : SubscriptionEventChannel {
    override fun subscribe(subscriptions: List<Id>) = remote.subscribe(subscriptions)
}