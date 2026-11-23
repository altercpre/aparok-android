package io.aparok.app.core_ui.features.editor.slash.holders

import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.databinding.ItemSlashWidgetObjectTypeBinding
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.presentation.editor.editor.slash.SlashItem

class ObjectTypeMenuHolder(
    val binding: ItemSlashWidgetObjectTypeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SlashItem.ObjectType) = with(binding) {
        val objectType = item.objectTypeView
        ivIcon.setIcon(objectType.icon)
        tvTitle.text = objectType.name
        if (objectType.description.isNullOrBlank()) {
            tvSubtitle.gone()
        } else {
            tvSubtitle.visible()
            tvSubtitle.text = objectType.description
        }
    }
}