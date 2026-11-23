package io.aparok.app.core_ui.features.editor.holders.text

import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.features.editor.BlockViewHolder
import io.aparok.app.core_ui.features.editor.TextBlockHolder
import io.aparok.app.core_ui.features.editor.decoration.DecoratableViewHolder
import io.aparok.app.core_ui.widgets.text.TextInputWidget
import io.aparok.app.core_utils.ext.dimen
import io.aparok.app.presentation.editor.editor.listener.ListenerType
import io.aparok.app.presentation.editor.editor.model.BlockView

abstract class Header<T : BlockView.Text.Header>(
    view: View,
    clicked: (ListenerType) -> Unit,
) : Text<T>(view, clicked),
    TextBlockHolder,
    BlockViewHolder.IndentableHolder,
    DecoratableViewHolder {

    abstract val header: TextInputWidget
    abstract val contentTopMargin: Int

    @Deprecated("Pre-nested-styling legacy.")
    override fun indentize(item: BlockView.Indentable) {
        // Do nothing.
    }

    override fun applyDecorations(decorations: List<BlockView.Decoration>) {
        decoratableContainer.decorate(decorations) { rect ->
            content.updateLayoutParams<FrameLayout.LayoutParams> {
                marginStart = dimen(R.dimen.dp_8) + rect.left
                marginEnd = dimen(R.dimen.dp_8) + rect.right
                topMargin = contentTopMargin
                bottomMargin = rect.bottom + dimen(R.dimen.dp_2)
            }
            selectionView.updateLayoutParams<FrameLayout.LayoutParams> {
                marginStart = dimen(R.dimen.dp_8) + rect.left
                marginEnd = dimen(R.dimen.dp_8) + rect.right
                bottomMargin = rect.bottom + dimen(R.dimen.dp_2)
            }
        }
    }
}