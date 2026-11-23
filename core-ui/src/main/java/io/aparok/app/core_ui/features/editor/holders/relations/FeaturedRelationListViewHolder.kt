package io.aparok.app.core_ui.features.editor.holders.relations

import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemBlockFeaturedRelationsBinding
import io.aparok.app.core_ui.features.editor.BlockViewHolder
import io.aparok.app.core_utils.ext.dimen
import io.aparok.app.presentation.editor.editor.listener.ListenerType
import io.aparok.app.presentation.editor.editor.model.BlockView

class FeaturedRelationListViewHolder(
    val binding: ItemBlockFeaturedRelationsBinding
) : BlockViewHolder(binding.root) {

    private val root = binding.featuredRelationRoot

    fun bind(item: BlockView.FeaturedRelation, click: (ListenerType) -> Unit) {
        if (item.isTodoLayout) {
            val lr = itemView.context.dimen(R.dimen.dp_60).toInt()
            binding.featuredRelationRoot.setPadding(lr, 0, 0, 0)
        } else {
            val lr = itemView.context.dimen(R.dimen.dp_20).toInt()
            binding.featuredRelationRoot.setPadding(lr, 0, 0, 0)
        }
        root.set(item, click)
    }
}