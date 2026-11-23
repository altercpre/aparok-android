package io.aparok.app.presentation.editor

import io.aparok.app.core_models.Id

sealed class Snack {
    data class UndoRedo(val message: String) : Snack()
}