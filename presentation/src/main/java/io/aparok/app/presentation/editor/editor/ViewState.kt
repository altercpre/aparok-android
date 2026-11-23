package io.aparok.app.presentation.editor.editor

import io.aparok.app.presentation.editor.editor.model.BlockView

sealed class ViewState {
    object Loading : ViewState()
    object NotExist : ViewState()
    data class Success(val blocks: List<BlockView>) : ViewState()
}