package io.aparok.app.core_ui.common

import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.databinding.ItemDefaultListSectionBinding

class DefaultSectionViewHolder(
    val binding: ItemDefaultListSectionBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(name: String) {
        binding.tvSectionName.text = name
    }
}