package io.aparok.app.core_ui.features.relations.holders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_models.Relation
import io.aparok.app.core_ui.databinding.ItemRelationFormatBinding
import io.aparok.app.core_ui.databinding.ItemRelationFormatCreateFromScratchBinding

class DefaultRelationViewHolder(
    val binding: ItemRelationFormatBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        name: String,
        format: Relation.Format
    ) = with(binding) {
        ivRelationFormat.bind(format)
        tvRelationName.text = name
    }
}

class DefaultRelationFormatViewHolder(
    val binding: ItemRelationFormatCreateFromScratchBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        name: String,
        format: Relation.Format,
        isSelected: Boolean
    ) {
        binding.ivRelationFormat.bind(format)
        binding.tvRelationName.text = name
        binding.selectionIcon.isVisible = isSelected
    }

    fun setIsSelected(isSelected: Boolean) {
        binding.selectionIcon.isVisible = isSelected
    }
}