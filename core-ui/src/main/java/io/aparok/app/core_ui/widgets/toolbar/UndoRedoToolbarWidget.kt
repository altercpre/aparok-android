package io.aparok.app.core_ui.widgets.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import io.aparok.app.core_ui.databinding.WidetUndoRedoPanelBinding

class UndoRedoToolbarWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(context, attrs) {

    val binding = WidetUndoRedoPanelBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    val undo: View get() = binding.btnUndo
    val redo: View get() = binding.btnRedo
}