package io.aparok.app.presentation.relations

import io.aparok.app.core_models.Relation

data class RelationFormatView(
    val format: Relation.Format,
    val isSelected: Boolean = false
)