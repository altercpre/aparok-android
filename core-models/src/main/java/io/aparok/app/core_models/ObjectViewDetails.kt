package io.aparok.app.core_models

data class ObjectViewDetails(val details: Map<Id, Struct>) {
    companion object {
        val EMPTY = ObjectViewDetails(emptyMap())
    }
}