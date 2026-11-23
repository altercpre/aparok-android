package io.aparok.app.core_ui.features.editor.holders.other

import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.BuildConfig
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkLoadingBinding
import io.aparok.app.core_ui.features.editor.BlockViewDiffUtil
import io.aparok.app.core_ui.features.editor.BlockViewHolder
import io.aparok.app.core_utils.ext.dimen
import io.aparok.app.presentation.editor.editor.listener.ListenerType
import io.aparok.app.presentation.editor.editor.model.BlockView

class LinkToObjectLoading(binding: ItemBlockObjectLinkLoadingBinding) :
    BlockViewHolder(binding.root),
    BlockViewHolder.IndentableHolder {

    private val container = binding.container

    fun bind(
        item: BlockView.LinkToObject.Loading,
        clicked: (ListenerType) -> Unit
    ) {

        applySelectedState(item)

        itemView.setOnClickListener { clicked(ListenerType.LinkToObjectLoading(item.id)) }
    }

    private fun applySelectedState(item: BlockView.LinkToObject.Loading) {
        container.isSelected = item.isSelected
    }

    fun processChangePayload(payloads: List<BlockViewDiffUtil.Payload>, item: BlockView) {
        check(item is BlockView.LinkToObject.Loading) { "Expected a link to object block, but was: $item" }
        payloads.forEach { payload ->
            if (payload.isSelectionChanged) {
                applySelectedState(item)
            }
        }
    }

    @Deprecated("Pre-nested-styling legacy.")
    override fun indentize(item: BlockView.Indentable) {
        // Do nothing.
    }
}