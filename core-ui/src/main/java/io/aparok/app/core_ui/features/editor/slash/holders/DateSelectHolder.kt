package io.aparok.app.core_ui.features.editor.slash.holders

import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.databinding.ItemSlashWidgetSelectDateBinding
import io.aparok.app.core_models.ui.ObjectIcon

class DateSelectHolder(
    val binding: ItemSlashWidgetSelectDateBinding
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.ivIcon.setIcon(
            ObjectIcon.TypeIcon.Default.DATE
        )
    }
}