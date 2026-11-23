package io.aparok.app.data.auth.types

import io.aparok.app.core_models.ObjectType
import io.aparok.app.domain.`object`.ObjectTypesProvider

class DefaultObjectTypesProvider : ObjectTypesProvider {

    private val objectTypes: MutableList<ObjectType> = mutableListOf()

    override fun get(): List<ObjectType> = objectTypes

    override fun set(objectTypes: List<ObjectType>) {
        this.objectTypes.apply {
            clear()
            addAll(objectTypes)
        }
    }
}