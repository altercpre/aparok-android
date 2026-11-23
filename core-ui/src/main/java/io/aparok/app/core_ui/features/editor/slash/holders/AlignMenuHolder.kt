package io.aparok.app.core_ui.features.editor.slash.holders

import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemSlashWidgetStyleBinding
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.presentation.editor.editor.slash.SlashItem

class AlignMenuHolder(val binding: ItemSlashWidgetStyleBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SlashItem.Alignment) = with(binding) {
        when (item) {
            SlashItem.Alignment.Left -> {
                tvTitle.setText(R.string.slash_widget_align_left)
                ivIcon.setImageResource(R.drawable.ic_slash_align_left)
                tvSubtitle.gone()
            }
            SlashItem.Alignment.Center -> {
                tvTitle.setText(R.string.slash_widget_align_center)
                ivIcon.setImageResource(R.drawable.ic_slash_align_center)
                tvSubtitle.gone()
            }
            SlashItem.Alignment.Right -> {
                tvTitle.setText(R.string.slash_widget_align_right)
                ivIcon.setImageResource(R.drawable.ic_slash_align_right)
                tvSubtitle.gone()
            }
        }
    }
}