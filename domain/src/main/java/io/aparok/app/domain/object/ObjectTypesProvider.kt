package io.aparok.app.domain.`object`

import io.aparok.app.core_models.ObjectType

@Deprecated("To be deleted")
interface ObjectTypesProvider {
    fun get(): List<ObjectType>
    fun set(objectTypes: List<ObjectType>)
}