package io.aparok.app.core_models

data class CreateObjectResult(
    val id: Id,
    val event: Payload,
    val details: Struct
)
