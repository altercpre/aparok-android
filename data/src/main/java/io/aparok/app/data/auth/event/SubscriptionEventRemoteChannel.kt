package io.aparok.app.data.auth.event

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.SubscriptionEvent
import kotlinx.coroutines.flow.Flow

interface SubscriptionEventRemoteChannel {
    fun subscribe(subscriptions: List<Id>): Flow<List<SubscriptionEvent>>
}