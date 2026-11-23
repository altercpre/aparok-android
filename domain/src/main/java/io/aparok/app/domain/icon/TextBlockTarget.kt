package io.aparok.app.domain.icon

import io.aparok.app.core_models.Id

data class TextBlockTarget(
    val context: Id,
    val blockId: Id,
)