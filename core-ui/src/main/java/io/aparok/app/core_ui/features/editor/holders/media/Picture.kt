package io.aparok.app.core_ui.features.editor.holders.media

import android.view.View
import android.widget.FrameLayout
import coil3.load
import io.aparok.app.core_ui.databinding.ItemBlockPictureBinding
import io.aparok.app.core_ui.features.editor.decoration.DecoratableCardViewHolder
import io.aparok.app.core_ui.features.editor.decoration.EditorDecorationContainer
import io.aparok.app.core_ui.features.editor.decoration.applySelectorOffset
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.invisible
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.presentation.editor.editor.listener.ListenerType
import io.aparok.app.presentation.editor.editor.model.BlockView
import timber.log.Timber

class Picture(val binding: ItemBlockPictureBinding) : Media(binding.root), DecoratableCardViewHolder {

    override val root: View = itemView
    override val container: View = root
    override val clickContainer: View = root
    private val image = binding.image
    private val error = binding.error
    private val loading = binding.progress

    override val decoratableContainer: EditorDecorationContainer = binding.decorationContainer
    override val decoratableCard: View = binding.card

    init {
        clickContainer.setOnTouchListener { v, e -> editorTouchProcessor.process(v, e) }
    }

    fun bind(item: BlockView.Media.Picture, clicked: (ListenerType) -> Unit) {
        super.bind(item, clicked)
        loading.visible()
        image.load(item.url) {
            listener(
                onSuccess = { _, _ ->
                    loading.gone()
                    error.invisible()
                },
                onError = { _, throwable ->
                    loading.gone()
                    error.visible()
                    Timber.w("Error while loading picture with url: ${item.url}")
                }
            )
        }
    }

    override fun onMediaBlockClicked(item: BlockView.Media, clicked: (ListenerType) -> Unit) {
        clicked(
            ListenerType.Picture.View(
                target = item.id,
                obj = item.targetObjectId
            )
        )
    }

    @Deprecated("Pre-nested-styling legacy.")
    override fun indentize(item: BlockView.Indentable) {
        // Do nothing.
    }

    override fun select(isSelected: Boolean) {
        binding.selected.isSelected = isSelected
    }

    override fun applyDecorations(decorations: List<BlockView.Decoration>) {
        super.applyDecorations(decorations)
        binding.selected.applySelectorOffset<FrameLayout.LayoutParams>(
            content = binding.card,
            res = itemView.resources
        )
    }

    companion object {
        const val LOADING_TIMEOUT_IN_MILLIS = 30000
    }
}