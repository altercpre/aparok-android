package io.aparok.app.presentation.editor.cover

import io.aparok.app.core_models.Hash
import io.aparok.app.core_models.Id


interface CoverImageHashProvider {
    fun provide(id: Id): Hash?
}