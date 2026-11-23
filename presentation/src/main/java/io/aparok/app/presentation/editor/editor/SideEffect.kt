package io.aparok.app.presentation.editor.editor

sealed class SideEffect {
    object ClearMultiSelectSelection : SideEffect()
    object ResetFocusToFirstTextBlock: SideEffect()
}