package io.aparok.app.core_models

data class ObjectOrder(
    val view: Id,
    val group: Id,
    val ids: List<Id>
)
