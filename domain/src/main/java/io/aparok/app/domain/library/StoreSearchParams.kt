package io.aparok.app.domain.library

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVSort
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.primitives.SpaceId

data class StoreSearchParams(
    val space: SpaceId,
    val subscription: Id,
    val sorts: List<DVSort> = emptyList(),
    val filters: List<DVFilter> = emptyList(),
    val source: List<String> = emptyList(),
    val offset: Long = 0,
    val limit: Int = 0,
    val keys: List<String> = emptyList(),
    val collection: Id? = null
)

data class StoreSearchByIdsParams(
    val space: SpaceId,
    val subscription: Id,
    val keys: List<Key>,
    val targets: List<Id>,
)

/**
 * Parameters for cross-space search subscription.
 * Unlike StoreSearchParams, this doesn't have a space parameter
 * since it searches across all spaces.
 */
data class CrossSpaceSearchParams(
    val subscription: Id,
    val sorts: List<DVSort> = emptyList(),
    val filters: List<DVFilter> = emptyList(),
    val keys: List<String> = emptyList(),
    val source: List<String> = emptyList(),
    val collection: Id? = null
)