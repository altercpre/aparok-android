package io.aparok.app.presentation.sets.viewer

import io.aparok.app.core_models.DVViewerType
import io.aparok.app.core_models.Id

data class ViewerView(
    val id: Id,
    val name: String,
    val type: DVViewerType,
    val isActive: Boolean,
    val showActionMenu: Boolean = false,
    val isUnsupported: Boolean = false,
    val defaultObjectType: Id?,
    val relations: List<Id>,
    val sorts: List<Id>,
    val filters: List<Id>,
    val isDefaultObjectTypeEnabled: Boolean
)


