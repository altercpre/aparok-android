package io.aparok.app.presentation.editor.editor

sealed class ObjectTypeMenuItem {
    object ChangeType: ObjectTypeMenuItem()
    object OpenType: ObjectTypeMenuItem()
}
