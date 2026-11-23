package io.aparok.app.core_ui.features.editor.holders.text

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemBlockHeaderOneBinding
import io.aparok.app.core_ui.features.editor.decoration.EditorDecorationContainer
import io.aparok.app.core_ui.widgets.text.TextInputWidget
import io.aparok.app.core_utils.ext.dimen
import io.aparok.app.presentation.editor.editor.listener.ListenerType
import io.aparok.app.presentation.editor.editor.model.BlockView

class HeaderOne(
    val binding: ItemBlockHeaderOneBinding,
    clicked: (ListenerType) -> Unit,
) : Header<BlockView.Text.Header.One>(binding.root, clicked) {

    override val header: TextInputWidget = binding.headerOne
    override val content: TextInputWidget get() = header
    override val root: View = itemView
    override val selectionView: View = binding.selectionView

    private val mentionIconSize: Int
    private val mentionIconPadding: Int
    private val mentionCheckedIcon: Drawable?
    private val mentionUncheckedIcon: Drawable?
    private val mentionInitialsSize: Float

    override val decoratableContainer: EditorDecorationContainer
        get() = binding.decorationContainer

    override val contentTopMargin: Int = dimen(R.dimen.dp_24)

    init {
        setup()
        with(itemView.context) {
            mentionIconSize =
                resources.getDimensionPixelSize(R.dimen.mention_span_image_size_header_one)
            mentionIconPadding =
                resources.getDimensionPixelSize(R.dimen.mention_span_image_padding_header_one)
            mentionUncheckedIcon = ContextCompat.getDrawable(this, R.drawable.ic_task_0_h1)
            mentionCheckedIcon = ContextCompat.getDrawable(this, R.drawable.ic_task_1_h1)
            mentionInitialsSize =
                resources.getDimension(R.dimen.mention_span_initials_size_header_one)
        }
    }

    override fun getMentionIconSize(): Int = mentionIconSize
    override fun getMentionIconPadding(): Int = mentionIconPadding
    override fun getMentionCheckedIcon(): Drawable? = mentionCheckedIcon
    override fun getMentionUncheckedIcon(): Drawable? = mentionUncheckedIcon
    override fun getMentionInitialsSize(): Float = mentionInitialsSize
}