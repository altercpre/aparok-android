package io.aparok.app.core_ui.features.dataview.holders

import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemViewerGridCellDescriptionBinding
import io.aparok.app.core_ui.extensions.dark
import io.aparok.app.core_models.ThemeColor
import io.aparok.app.presentation.sets.model.CellView

class DVGridCellStatusHolder(val binding: ItemViewerGridCellDescriptionBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cell: CellView.Status) = with(binding) {
        val status = cell.status.firstOrNull()
        if (status != null) {
            tvText.text = status.status
            val color = ThemeColor.values().find { v -> v.code == status.color }
            val defaultTextColor = itemView.resources.getColor(R.color.text_primary, null)
            if (color != null && color != ThemeColor.DEFAULT) {
                tvText.setTextColor(itemView.resources.dark(color, defaultTextColor))
            } else {
                tvText.setTextColor(defaultTextColor)
            }
        } else {
            tvText.text = ""
        }
    }
}