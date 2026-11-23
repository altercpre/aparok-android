package io.aparok.app.domain.search

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.SubscriptionEvent
import kotlinx.coroutines.flow.Flow

/**
 * Channel for events related to changes and transformations of objects.
 * @see SubscriptionEvent
 */
interface SubscriptionEventChannel {
    fun subscribe(subscriptions: List<Id>): Flow<List<SubscriptionEvent>>
}