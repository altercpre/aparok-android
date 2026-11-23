package io.aparok.app.core_ui.features.editor.holders.upload

import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemBlockMediaPlaceholderBinding
import io.aparok.app.core_ui.extensions.drawable
import io.aparok.app.core_ui.features.editor.decoration.DecoratableCardViewHolder
import io.aparok.app.presentation.editor.editor.listener.ListenerType
import io.aparok.app.presentation.editor.editor.model.BlockView

class VideoUpload(
    binding: ItemBlockMediaPlaceholderBinding
) : MediaUpload(binding), DecoratableCardViewHolder {

    override fun uploadClick(target: String, clicked: (ListenerType) -> Unit) {
        clicked(ListenerType.Video.Upload(target))
    }

    override fun bind(item: BlockView.Upload, clicked: (ListenerType) -> Unit) {
        super.bind(item, clicked)
        binding.fileIcon.setImageDrawable(itemView.context.drawable(R.drawable.ic_video_inactive))
    }
}