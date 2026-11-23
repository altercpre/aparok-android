package io.aparok.app.core_ui.features.editor.holders.placeholders

import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemBlockMediaPlaceholderBinding
import io.aparok.app.core_ui.extensions.drawable
import io.aparok.app.presentation.editor.editor.listener.ListenerType

class VideoPlaceholder(binding: ItemBlockMediaPlaceholderBinding) : MediaPlaceholder(binding) {

    override fun placeholderClick(target: String, clicked: (ListenerType) -> Unit) {
        clicked(ListenerType.Video.Placeholder(target))
    }

    override fun setup() {
        title.text = itemView.resources.getString(R.string.hint_upload_video)
        binding.fileIcon.setImageDrawable(itemView.context.drawable(R.drawable.ic_video))
    }
}