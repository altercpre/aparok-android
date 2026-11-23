package io.aparok.app.core_models

import io.aparok.app.core_models.primitives.RelationKey

data class RelationLink(val key: Key, val format: RelationFormat)

data class RelationListWithValueItem(
    val key: RelationKey,
    val counter: Long
)
