package io.aparok.app.core_ui.features.editor.holders.other

import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import io.aparok.app.core_ui.BuildConfig
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemBlockDividerDotsBinding
import io.aparok.app.core_ui.features.editor.decoration.DecoratableViewHolder
import io.aparok.app.core_utils.ext.dimen
import io.aparok.app.presentation.editor.editor.listener.ListenerType
import io.aparok.app.presentation.editor.editor.model.BlockView

class DividerDots(
    private val binding: ItemBlockDividerDotsBinding
) : Divider(binding.root), DecoratableViewHolder {

    override val container: View = binding.container
    override val decoratableContainer = binding.decorationContainer

    fun bind(item: BlockView.DividerDots, clicked: (ListenerType) -> Unit) {
        super.bind(
            id = item.id,
            item = item,
            isItemSelected = item.isSelected,
            clicked = clicked,
            background = item.background
        )
    }

    override fun applyDecorations(decorations: List<BlockView.Decoration>) {
        decoratableContainer.decorate(decorations) { rect ->
            val defaultIndentOffset = dimen(R.dimen.default_indent)
            container.updateLayoutParams<FrameLayout.LayoutParams> {
                marginStart = rect.left + defaultIndentOffset
                marginEnd = rect.right + defaultIndentOffset
                bottomMargin = rect.bottom + dimen(R.dimen.divider_extra_space_bottom)
            }
        }
    }
}