package io.aparok.app.ui.editor

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

internal fun clearEditorTextSelections(root: View) {
    // Lose focus on any currently focused child
    root.clearFocus()
    root.findFocus()?.clearFocus()
    // Walk the tree
    clearTextViewsRecursively(root)
}

private fun clearTextViewsRecursively(view: View) {
    when (view) {
        is EditText -> {
            // Remove focus and any selection
            view.clearFocus()
            if (view.hasSelection() && view.text.isNotEmpty()) {
                view.setSelection(0, 0)
            }
            clearActionModeCallbacks(view)
        }
        is TextView -> {
            // Other TextViews: just clear any lingering callbacks
            view.clearFocus()
            clearActionModeCallbacks(view)
        }
        is ViewGroup -> {
            for (i in 0 until view.childCount) {
                clearTextViewsRecursively(view.getChildAt(i))
            }
        }
        else -> Unit
    }
}

private fun clearActionModeCallbacks(view: TextView) {
    view.customSelectionActionModeCallback = null
    view.customInsertionActionModeCallback = null
}
