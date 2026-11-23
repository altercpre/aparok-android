package io.aparok.app.presentation.editor.editor.slash

import io.aparok.app.core_utils.diff.DefaultObjectDiffIdentifier
import io.aparok.app.presentation.relations.ObjectRelationView

sealed class SlashPropertyView : DefaultObjectDiffIdentifier {
    data class Item(val view: ObjectRelationView) : SlashPropertyView() {
        override val identifier: String get() = view.identifier
    }

    sealed class Section : SlashPropertyView() {
        object Subheader : Section() {
            override val identifier: String get() = javaClass.simpleName
        }

        object SubheaderWithBack : Section() {
            override val identifier: String get() = javaClass.simpleName
        }
    }
    object PropertyNew : SlashPropertyView() {
        override val identifier: String get() = javaClass.simpleName
    }
}