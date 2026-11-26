package io.aparok.app.ui.editor

import android.content.Context
import android.os.Build
import android.text.Selection
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class EditorFragmentTextSelectionTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun dummyActionModeCallback(): ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = false
        override fun onDestroyActionMode(mode: ActionMode?) = Unit
    }

    @Test
    fun `clearEditorTextSelections clears selection focus and action mode callbacks across nested views`() {
        val root = FrameLayout(context)
        val nested = LinearLayout(context)

        val selectedEditText = EditText(context).apply {
            setText("Sovereign offline text")
            Selection.setSelection(text, 2, 10)
            customSelectionActionModeCallback = dummyActionModeCallback()
            customInsertionActionModeCallback = dummyActionModeCallback()
            isFocusableInTouchMode = true
            requestFocus()
        }

        val emptyEditText = EditText(context).apply {
            setText("")
            customSelectionActionModeCallback = dummyActionModeCallback()
            customInsertionActionModeCallback = dummyActionModeCallback()
        }

        val labelTextView = TextView(context).apply {
            text = "Read-only label"
            customSelectionActionModeCallback = dummyActionModeCallback()
            customInsertionActionModeCallback = dummyActionModeCallback()
            isFocusableInTouchMode = true
            requestFocus()
        }

        val divider = View(context)

        nested.addView(selectedEditText)
        nested.addView(emptyEditText)
        nested.addView(labelTextView)
        nested.addView(divider)
        root.addView(nested)

        clearEditorTextSelections(root)

        assertFalse(selectedEditText.hasSelection())
        assertEquals(0, selectedEditText.selectionStart)
        assertEquals(0, selectedEditText.selectionEnd)
        assertFalse(selectedEditText.hasFocus())
        assertNull(selectedEditText.customSelectionActionModeCallback)
        assertNull(selectedEditText.customInsertionActionModeCallback)

        assertEquals(0, emptyEditText.selectionStart)
        assertEquals(0, emptyEditText.selectionEnd)
        assertNull(emptyEditText.customSelectionActionModeCallback)
        assertNull(emptyEditText.customInsertionActionModeCallback)

        assertFalse(labelTextView.hasFocus())
        assertNull(labelTextView.customSelectionActionModeCallback)
        assertNull(labelTextView.customInsertionActionModeCallback)
    }

    @Test
    fun `clearEditorTextSelections handles direct EditText root cleanly`() {
        val editText = EditText(context).apply {
            setText("Direct root editor")
            Selection.setSelection(text, 0, 6)
            customSelectionActionModeCallback = dummyActionModeCallback()
        }

        clearEditorTextSelections(editText)

        assertFalse(editText.hasSelection())
        assertEquals(0, editText.selectionStart)
        assertEquals(0, editText.selectionEnd)
        assertNull(editText.customSelectionActionModeCallback)
    }
}
