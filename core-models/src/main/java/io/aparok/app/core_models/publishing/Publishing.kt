package io.aparok.app.core_models.publishing

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Struct
import io.aparok.app.core_models.primitives.Space

object Publishing {

    enum class Status {
        CREATED,
        PUBLISHED
    }

    class State(
        val obj: Id,
        val space: Space,
        val uri: String,
        val status: Status,
        val timestamp: Long,
        val size: Long,
        val version: String,
        val details: Struct,
        val showJoinSpaceButton: Boolean
    )

}