package io.aparok.app.features.emoji

import io.aparok.app.presentation.editor.picker.ObjectIconPickerViewModelFactory
import io.aparok.app.ui.editor.modals.ObjectIconPickerFragment

class TestDocumentEmojiPickerFragment : ObjectIconPickerFragment() {
    init {
        factory = testViewModelFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testViewModelFactory: ObjectIconPickerViewModelFactory
    }
}