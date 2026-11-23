package io.aparok.app.core_ui.features.editor.holders.other

import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemBlockUnsupportedBinding
import io.aparok.app.core_ui.features.editor.BlockViewHolder
import io.aparok.app.core_utils.ext.dimen
import io.aparok.app.presentation.editor.editor.model.BlockView

class Unsupported(
    val binding: ItemBlockUnsupportedBinding
) : BlockViewHolder(binding.root), BlockViewHolder.IndentableHolder {
    fun bind(item: BlockView.Unsupported) {
        indentize(item)
        itemView.isSelected = item.isSelected
    }

    override fun indentize(item: BlockView.Indentable) {
        binding.indentator.updateLayoutParams<LinearLayout.LayoutParams> {
            width = item.indent * dimen(R.dimen.indent)
        }
    }
}