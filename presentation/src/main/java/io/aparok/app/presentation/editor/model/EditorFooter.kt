package io.aparok.app.presentation.editor.model

sealed class EditorFooter {
    object Note : EditorFooter()
    object None : EditorFooter()
}