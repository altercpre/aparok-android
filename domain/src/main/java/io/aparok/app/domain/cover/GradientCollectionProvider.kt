package io.aparok.app.domain.cover

import io.aparok.app.core_models.Id


interface GradientCollectionProvider {
    fun provide(): List<Id>
}