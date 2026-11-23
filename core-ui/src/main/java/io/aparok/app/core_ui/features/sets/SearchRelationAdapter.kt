package io.aparok.app.core_ui.features.sets

import android.view.LayoutInflater
import android.view.ViewGroup
import io.aparok.app.core_ui.common.AbstractAdapter
import io.aparok.app.core_ui.common.AbstractViewHolder
import io.aparok.app.core_ui.databinding.ItemSearchRelationBinding
import io.aparok.app.core_ui.extensions.icon
import io.aparok.app.presentation.sets.model.SimpleRelationView

class SearchRelationAdapter(
    private val onRelationClicked: (SimpleRelationView) -> Unit
) : AbstractAdapter<SimpleRelationView>(emptyList()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        binding = ItemSearchRelationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ).apply {
        itemView.setOnClickListener { onRelationClicked(items[bindingAdapterPosition]) }
    }

    class ViewHolder(
        val binding: ItemSearchRelationBinding
    ) : AbstractViewHolder<SimpleRelationView>(binding.root) {
        override fun bind(item: SimpleRelationView) = with(binding) {
            val icon: Int? = item.icon()
            if (icon != null) {
                ivRelation.setImageResource(icon)
            } else {
                ivRelation.setImageDrawable(null)
            }
            tvRelationName.text = item.title
        }
    }
}