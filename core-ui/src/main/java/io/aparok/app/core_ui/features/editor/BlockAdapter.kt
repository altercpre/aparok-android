package io.aparok.app.core_ui.features.editor

import android.os.Build
import android.os.Build.VERSION_CODES.N
import android.os.Build.VERSION_CODES.N_MR1
import android.text.Editable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_models.Id
import io.aparok.app.core_ui.BuildConfig
import io.aparok.app.core_ui.databinding.ItemBlockBookmarkBinding
import io.aparok.app.core_ui.databinding.ItemBlockBulletedBinding
import io.aparok.app.core_ui.databinding.ItemBlockCalloutBinding
import io.aparok.app.core_ui.databinding.ItemBlockCheckboxBinding
import io.aparok.app.core_ui.databinding.ItemBlockCodeSnippetBinding
import io.aparok.app.core_ui.databinding.ItemBlockDataViewDefaultBinding
import io.aparok.app.core_ui.databinding.ItemBlockDataViewEmptyDataBinding
import io.aparok.app.core_ui.databinding.ItemBlockDataViewEmptySourceBinding
import io.aparok.app.core_ui.databinding.ItemBlockDescriptionBinding
import io.aparok.app.core_ui.databinding.ItemBlockDividerDotsBinding
import io.aparok.app.core_ui.databinding.ItemBlockDividerLineBinding
import io.aparok.app.core_ui.databinding.ItemBlockEmbedBinding
import io.aparok.app.core_ui.databinding.ItemBlockFeaturedRelationsBinding
import io.aparok.app.core_ui.databinding.ItemBlockFileBinding
import io.aparok.app.core_ui.databinding.ItemBlockHeaderOneBinding
import io.aparok.app.core_ui.databinding.ItemBlockHeaderThreeBinding
import io.aparok.app.core_ui.databinding.ItemBlockHeaderTwoBinding
import io.aparok.app.core_ui.databinding.ItemBlockHighlightBinding
import io.aparok.app.core_ui.databinding.ItemBlockLatexBinding
import io.aparok.app.core_ui.databinding.ItemBlockMediaErrorBinding
import io.aparok.app.core_ui.databinding.ItemBlockMediaPlaceholderBinding
import io.aparok.app.core_ui.databinding.ItemBlockNumberedBinding
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkArchiveBinding
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkBinding
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkCardMediumIconBinding
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkCardMediumIconCoverBinding
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkCardSmallIconBinding
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkCardSmallIconCoverBinding
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkDeleteBinding
import io.aparok.app.core_ui.databinding.ItemBlockObjectLinkLoadingBinding
import io.aparok.app.core_ui.databinding.ItemBlockOpenFileBinding
import io.aparok.app.core_ui.databinding.ItemBlockPictureBinding
import io.aparok.app.core_ui.databinding.ItemBlockRelationCheckboxBinding
import io.aparok.app.core_ui.databinding.ItemBlockRelationDefaultBinding
import io.aparok.app.core_ui.databinding.ItemBlockRelationDeletedBinding
import io.aparok.app.core_ui.databinding.ItemBlockRelationFileBinding
import io.aparok.app.core_ui.databinding.ItemBlockRelationObjectBinding
import io.aparok.app.core_ui.databinding.ItemBlockRelationPlaceholderBinding
import io.aparok.app.core_ui.databinding.ItemBlockRelationStatusBinding
import io.aparok.app.core_ui.databinding.ItemBlockRelationTagBinding
import io.aparok.app.core_ui.databinding.ItemBlockTableBinding
import io.aparok.app.core_ui.databinding.ItemBlockTextBinding
import io.aparok.app.core_ui.databinding.ItemBlockTitleBinding
import io.aparok.app.core_ui.databinding.ItemBlockTitleFileBinding
import io.aparok.app.core_ui.databinding.ItemBlockTitleImageBinding
import io.aparok.app.core_ui.databinding.ItemBlockTitleProfileBinding
import io.aparok.app.core_ui.databinding.ItemBlockTitleTodoBinding
import io.aparok.app.core_ui.databinding.ItemBlockTitleVideoBinding
import io.aparok.app.core_ui.databinding.ItemBlockTocBinding
import io.aparok.app.core_ui.databinding.ItemBlockToggleBinding
import io.aparok.app.core_ui.databinding.ItemBlockUnsupportedBinding
import io.aparok.app.core_ui.databinding.ItemBlockVideoBinding
import io.aparok.app.core_ui.features.editor.BlockViewDiffUtil.Payload
import io.aparok.app.core_ui.features.editor.decoration.DecoratableViewHolder
import io.aparok.app.core_ui.features.editor.holders.dataview.DataViewBlockDefaultHolder
import io.aparok.app.core_ui.features.editor.holders.dataview.DataViewBlockDeleteHolder
import io.aparok.app.core_ui.features.editor.holders.dataview.DataViewBlockEmptyDataHolder
import io.aparok.app.core_ui.features.editor.holders.dataview.DataViewBlockEmptySourceHolder
import io.aparok.app.core_ui.features.editor.holders.error.BookmarkError
import io.aparok.app.core_ui.features.editor.holders.error.FileError
import io.aparok.app.core_ui.features.editor.holders.error.PictureError
import io.aparok.app.core_ui.features.editor.holders.error.VideoError
import io.aparok.app.core_ui.features.editor.holders.ext.setup
import io.aparok.app.core_ui.features.editor.holders.ext.setupPlaceholder
import io.aparok.app.core_ui.features.editor.holders.`interface`.TextHolder
import io.aparok.app.core_ui.features.editor.holders.media.Bookmark
import io.aparok.app.core_ui.features.editor.holders.media.File
import io.aparok.app.core_ui.features.editor.holders.media.Picture
import io.aparok.app.core_ui.features.editor.holders.media.Video
import io.aparok.app.core_ui.features.editor.holders.other.Code
import io.aparok.app.core_ui.features.editor.holders.other.DividerDots
import io.aparok.app.core_ui.features.editor.holders.other.DividerLine
import io.aparok.app.core_ui.features.editor.holders.other.Latex
import io.aparok.app.core_ui.features.editor.holders.other.LinkToObject
import io.aparok.app.core_ui.features.editor.holders.other.LinkToObjectArchive
import io.aparok.app.core_ui.features.editor.holders.other.LinkToObjectCardMediumIcon
import io.aparok.app.core_ui.features.editor.holders.other.LinkToObjectCardMediumIconCover
import io.aparok.app.core_ui.features.editor.holders.other.LinkToObjectCardSmallIcon
import io.aparok.app.core_ui.features.editor.holders.other.LinkToObjectCardSmallIconCover
import io.aparok.app.core_ui.features.editor.holders.other.LinkToObjectDelete
import io.aparok.app.core_ui.features.editor.holders.other.LinkToObjectLoading
import io.aparok.app.core_ui.features.editor.holders.other.TableOfContents
import io.aparok.app.core_ui.features.editor.holders.other.Title
import io.aparok.app.core_ui.features.editor.holders.other.Unsupported
import io.aparok.app.core_ui.features.editor.holders.placeholders.BookmarkPlaceholder
import io.aparok.app.core_ui.features.editor.holders.placeholders.EmbedPlaceholder
import io.aparok.app.core_ui.features.editor.holders.placeholders.FilePlaceholder
import io.aparok.app.core_ui.features.editor.holders.placeholders.PicturePlaceholder
import io.aparok.app.core_ui.features.editor.holders.placeholders.VideoPlaceholder
import io.aparok.app.core_ui.features.editor.holders.relations.FeaturedRelationListViewHolder
import io.aparok.app.core_ui.features.editor.holders.relations.RelationBlockViewHolder
import io.aparok.app.core_ui.features.editor.holders.text.Bulleted
import io.aparok.app.core_ui.features.editor.holders.text.Callout
import io.aparok.app.core_ui.features.editor.holders.text.Checkbox
import io.aparok.app.core_ui.features.editor.holders.text.Description
import io.aparok.app.core_ui.features.editor.holders.text.HeaderOne
import io.aparok.app.core_ui.features.editor.holders.text.HeaderThree
import io.aparok.app.core_ui.features.editor.holders.text.HeaderTwo
import io.aparok.app.core_ui.features.editor.holders.text.Highlight
import io.aparok.app.core_ui.features.editor.holders.text.Numbered
import io.aparok.app.core_ui.features.editor.holders.text.Paragraph
import io.aparok.app.core_ui.features.editor.holders.text.Text
import io.aparok.app.core_ui.features.editor.holders.text.Toggle
import io.aparok.app.core_ui.features.editor.holders.upload.BookmarkUpload
import io.aparok.app.core_ui.features.editor.holders.upload.FileUpload
import io.aparok.app.core_ui.features.editor.holders.upload.OpenFile
import io.aparok.app.core_ui.features.editor.holders.upload.OpenImage
import io.aparok.app.core_ui.features.editor.holders.upload.PictureUpload
import io.aparok.app.core_ui.features.editor.holders.upload.VideoUpload
import io.aparok.app.core_ui.features.table.holders.TableBlockHolder
import io.aparok.app.core_ui.tools.ClipboardInterceptor
import io.aparok.app.core_ui.tools.DefaultTextWatcher
import io.aparok.app.core_ui.tools.LockableFocusChangeListener
import io.aparok.app.core_utils.ext.imm
import io.aparok.app.core_utils.ext.typeOf
import io.aparok.app.core_utils.text.OnNewLineActionListener
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.editor.editor.KeyPressedEvent
import io.aparok.app.presentation.mapper.mark
import io.aparok.app.presentation.editor.editor.listener.ListenerType
import io.aparok.app.presentation.editor.editor.listener.ListenerType.LongClick
import io.aparok.app.presentation.editor.editor.mention.MentionEvent
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.editor.editor.model.Focusable
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_BOOKMARK
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_BOOKMARK_ERROR
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_BOOKMARK_PLACEHOLDER
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_BOOKMARK_UPLOAD
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_BULLET
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_CALLOUT
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_CHECKBOX
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_CODE_SNIPPET
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_DATA_VIEW_DEFAULT
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_DATA_VIEW_EMPTY_DATA
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_DATA_VIEW_EMPTY_SOURCE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_DATA_VIEW_SOURCE_DELETED
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_DESCRIPTION
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_DIVIDER_DOTS
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_DIVIDER_LINE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_FEATURED_RELATION
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_FILE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_FILE_ERROR
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_FILE_PLACEHOLDER
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_FILE_TITLE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_FILE_UPLOAD
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_HEADER_ONE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_HEADER_THREE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_HEADER_TWO
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_HIGHLIGHT
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_LATEX
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_EMBED
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_NUMBERED
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_OBJECT_LINK_ARCHIVE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_OBJECT_LINK_CARD_MEDIUM_ICON
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_OBJECT_LINK_CARD_MEDIUM_ICON_COVER
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_OBJECT_LINK_CARD_SMALL_ICON
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_OBJECT_LINK_CARD_SMALL_ICON_COVER
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_OBJECT_LINK_DEFAULT
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_OBJECT_LINK_DELETED
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_OBJECT_LINK_LOADING
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_BUTTON_OPEN_FILE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_BUTTON_OPEN_IMAGE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_IMAGE_TITLE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_PARAGRAPH
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_PICTURE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_PICTURE_ERROR
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_PICTURE_PLACEHOLDER
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_PICTURE_UPLOAD
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_PROFILE_TITLE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_CHECKBOX
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_DATE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_DEFAULT
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_DELETED
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_FILE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_OBJECT
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_PLACEHOLDER
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_STATUS
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_RELATION_TAGS
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_TABLE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_TITLE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_TOC
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_TODO_TITLE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_TOGGLE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_UNSUPPORTED
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO_ERROR
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO_PLACEHOLDER
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO_TITLE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO_UPLOAD
import io.aparok.app.presentation.editor.editor.slash.SlashEvent
import io.aparok.app.core_models.ui.ObjectIcon
import java.util.Queue
import timber.log.Timber

/**
 * Adapter for rendering list of blocks.
 * @property blocks mutable list of blocks
 * @see BlockView
 * @see BlockViewHolder
 * @see BlockViewDiffUtil
 */
class BlockAdapter(
    private var restore: Queue<Editor.Restore>,
    initialBlock: List<BlockView>,
    private val onDescriptionChanged: (BlockView.Description) -> Unit = {},
    private val onTextBlockTextChanged: (BlockView.Text) -> Unit = {},
    private val onTextChanged: (String, Editable) -> Unit = { _, _ -> },
    private val onTitleBlockTextChanged: (Id, String) -> Unit = { _, _ -> },
    private val onTitleTextInputClicked: () -> Unit = {},
    private val onSelectionChanged: (String, IntRange) -> Unit = { _, _ -> },
    private val onCellSelectionChanged: (Id, IntRange) -> Unit = { _, _ -> },
    private val onCheckboxClicked: (BlockView.Text.Checkbox) -> Unit = {},
    private val onTitleCheckboxClicked: (BlockView.Title.Todo) -> Unit = {},
    private val onFocusChanged: (Id, Boolean) -> Unit = { _, _ -> },
    private val onEmptyBlockBackspaceClicked: (String) -> Unit = {},
    private val onNonEmptyBlockBackspaceClicked: (String, Editable) -> Unit = { _, _ -> },
    private val onSplitLineEnterClicked: (String, Editable, IntRange) -> Unit = { _, _, _-> },
    private val onSplitDescription: (Id, Editable, IntRange) -> Unit = { _, _, _-> },
    private val onTextInputClicked: (String) -> Unit = {},
    val onClickListener: (ListenerType) -> Unit = {},
    private val onPageIconClicked: () -> Unit = {},
    private val onCoverClicked: () -> Unit = {},
    private val onTogglePlaceholderClicked: (String) -> Unit = {},
    private val onToggleClicked: (String) -> Unit = {},
    private val clipboardInterceptor: ClipboardInterceptor,
    private val onMentionEvent: (MentionEvent) -> Unit = {},
    private val onSlashEvent: (SlashEvent) -> Unit = {},
    private val onBackPressedCallback: () -> Boolean,
    private val onKeyPressedEvent: (KeyPressedEvent) -> Unit = {},
    /**
     * True when Tab and Shift+Tab from a hardware keyboard move the block.
     * When false, Tab keeps the default behavior: it moves the focus to the next view.
     */
    private val isTabKeyEnabled: Boolean = false,
    private val onDragAndDropTrigger: (RecyclerView.ViewHolder, event: MotionEvent?) -> Boolean = { _, _ -> true},
    private val onDragListener: View.OnDragListener,
    private val lifecycle: Lifecycle,
    private val dragAndDropSelector: DragAndDropSelector,
) : RecyclerView.Adapter<BlockViewHolder>(),
    ItemProviderAdapter<BlockView>,
    DragAndDropSelector by dragAndDropSelector {

    private var blocks: List<BlockView> = initialBlock
    val views: List<BlockView> get() = blocks

    private var isInDragAndDropMode = false

    override fun selectDraggedViewHolder(position: Int) {
        isInDragAndDropMode = true
        dragAndDropSelector.selectDraggedViewHolder(position)
    }

    override fun unSelectDraggedViewHolder() {
        dragAndDropSelector.unSelectDraggedViewHolder()
        isInDragAndDropMode = false
    }

    /**
     * While a block-list update is being applied, RecyclerView can transiently hand
     * focus to a neighboring row when the focused row is removed or rebound (e.g. the
     * trailing-placeholder/identity-fork swaps replace the focused row wholesale).
     * That is a system focus transfer, not a user caret move — reporting it to the
     * ViewModel would be recorded as deliberate user focus (see userFocus in
     * EditorViewModel) and hijack the caret, e.g. into the end of the previous block.
     * Set to true before dispatching a list update and back to false after the
     * resulting layout pass.
     */
    var suppressFocusCallbacks: Boolean = false

    private fun dispatchFocusChanged(id: Id, hasFocus: Boolean) {
        if (suppressFocusCallbacks) {
            Timber.d("Focus change for $id suppressed while a list update is being applied")
        } else {
            onFocusChanged(id, hasFocus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val holder = when (viewType) {
            HOLDER_PARAGRAPH -> {
                Paragraph(
                    ItemBlockTextBinding.inflate(inflater, parent, false),
                    clicked = onClickListener
                )
            }
            HOLDER_TITLE -> {
                Title.Document(
                    ItemBlockTitleBinding.inflate(inflater, parent, false)
                ).apply {
                    with(content) {
                        enableEnterKeyDetector(
                            onEnterClicked = { range ->
                                onTitleEnterKeyListener(
                                    views = views,
                                    textView = content,
                                    range = range,
                                    onKeyPressedEvent = onKeyPressedEvent
                                )
                            }
                        )
                        selectionWatcher = { selection ->
                            val pos = bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                val view = views[pos]
                                if (view is BlockView.Title.Basic) {
                                    view.cursor = selection.last
                                }
                                onSelectionChanged(view.id, selection)
                            }
                        }
                        addTextChangedListener(
                            DefaultTextWatcher { editable ->
                                val pos = bindingAdapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    val view = views[pos]
                                    check(view is BlockView.Title.Basic)
                                    view.text = editable.toString()
                                    onTitleBlockTextChanged(view.id, editable.toString())
                                }
                            }
                        )
                    }
                }
            }
            HOLDER_PROFILE_TITLE -> {
                Title.Profile(
                    ItemBlockTitleProfileBinding.inflate(inflater, parent, false)
                ).apply {
                    with(content) {
                        enableEnterKeyDetector(
                            onEnterClicked = { range ->
                                onTitleEnterKeyListener(
                                    views = views,
                                    textView = content,
                                    range = range,
                                    onKeyPressedEvent = onKeyPressedEvent
                                )
                            }
                        )
                        selectionWatcher = { selection ->
                            val pos = bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                val view = views[pos]
                                if (view is BlockView.Title.Profile) {
                                    view.cursor = selection.last
                                }
                                onSelectionChanged(view.id, selection)
                            }
                        }
                        addTextChangedListener(
                            DefaultTextWatcher { editable ->
                                val pos = bindingAdapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    val view = views[pos]
                                    check(view is BlockView.Title.Profile)
                                    val text = editable.toString()
                                    view.text = text
                                    onTitleBlockTextChanged(view.id, text)
                                    onTitleTextChanged(text)
                                }
                            }
                        )
                    }
                }
            }
            HOLDER_FILE_TITLE -> {
                Title.File(
                    ItemBlockTitleFileBinding.inflate(inflater, parent, false)
                ).apply {
                    with(content) {
                        enableEnterKeyDetector(
                            onEnterClicked = { range ->
                                onTitleEnterKeyListener(
                                    views = views,
                                    textView = content,
                                    range = range,
                                    onKeyPressedEvent = onKeyPressedEvent
                                )
                            }
                        )
                        selectionWatcher = { selection ->
                            val pos = bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                val view = views[pos]
                                if (view is BlockView.Title.File) {
                                    view.cursor = selection.last
                                }
                                onSelectionChanged(view.id, selection)
                            }
                        }
                        addTextChangedListener(
                            DefaultTextWatcher { editable ->
                                val pos = bindingAdapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    val view = views[pos]
                                    check(view is BlockView.Title.File)
                                    view.text = editable.toString()
                                    onTitleBlockTextChanged(view.id, editable.toString())
                                }
                            }
                        )
                    }
                }
            }

            HOLDER_IMAGE_TITLE -> {
                Title.Image(
                    ItemBlockTitleImageBinding.inflate(inflater, parent, false)
                ).apply {
                    with(content) {
                        enableEnterKeyDetector(
                            onEnterClicked = { range ->
                                onTitleEnterKeyListener(
                                    views = views,
                                    textView = content,
                                    range = range,
                                    onKeyPressedEvent = onKeyPressedEvent
                                )
                            }
                        )
                        selectionWatcher = { selection ->
                            val pos = bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                val view = views[pos]
                                if (view is BlockView.Title.Image) {
                                    view.cursor = selection.last
                                }
                                onSelectionChanged(view.id, selection)
                            }
                        }
                        addTextChangedListener(
                            DefaultTextWatcher { editable ->
                                val pos = bindingAdapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    val view = views[pos]
                                    check(view is BlockView.Title.Image)
                                    view.text = editable.toString()
                                    onTitleBlockTextChanged(view.id, editable.toString())
                                }
                            }
                        )
                    }
                }
            }

            HOLDER_VIDEO_TITLE -> Title.Video(
                ItemBlockTitleVideoBinding.inflate(inflater, parent, false)
            ).apply {
                with(content) {
                    enableEnterKeyDetector(
                        onEnterClicked = { range ->
                            onTitleEnterKeyListener(
                                views = views,
                                textView = content,
                                range = range,
                                onKeyPressedEvent = onKeyPressedEvent
                            )
                        }
                    )
                    selectionWatcher = { selection ->
                        val pos = bindingAdapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            val view = views[pos]
                            if (view is BlockView.Title.Video) {
                                view.cursor = selection.last
                            }
                            onSelectionChanged(view.id, selection)
                        }
                    }
                    addTextChangedListener(
                        DefaultTextWatcher { editable ->
                            val pos = bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                val view = views[pos]
                                check(view is BlockView.Title.Video)
                                view.text = editable.toString()
                                onTitleBlockTextChanged(view.id, editable.toString())
                            }
                        }
                    )
                }
            }

            HOLDER_TODO_TITLE -> {
                Title.Todo(
                    ItemBlockTitleTodoBinding.inflate(inflater, parent, false)
                ).apply {
                    checkbox.setOnClickListener {
                        if (!isLocked) {
                            val view = views[bindingAdapterPosition]
                            check(view is BlockView.Title.Todo)
                            view.isChecked = !view.isChecked
                            checkbox.isSelected = view.isChecked
                            onTitleCheckboxClicked(view)
                        }
                    }
                    with(content) {
                        enableEnterKeyDetector(
                            onEnterClicked = { range ->
                                onTitleEnterKeyListener(
                                    views = views,
                                    textView = content,
                                    range = range,
                                    onKeyPressedEvent = onKeyPressedEvent
                                )
                            }
                        )
                        selectionWatcher = { selection ->
                            val pos = bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                val view = views[pos]
                                if (view is BlockView.Title.Todo) {
                                    view.cursor = selection.last
                                }
                                onSelectionChanged(view.id, selection)
                            }
                        }
                        addTextChangedListener(
                            DefaultTextWatcher { editable ->
                                val pos = bindingAdapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    val view = views[pos]
                                    check(view is BlockView.Title.Todo)
                                    view.text = editable.toString()
                                    onTitleBlockTextChanged(view.id, editable.toString())
                                }
                            }
                        )
                    }
                }
            }
            HOLDER_HEADER_ONE -> {
                HeaderOne(
                    binding = ItemBlockHeaderOneBinding.inflate(inflater, parent, false),
                    clicked = onClickListener,
                )
            }
            HOLDER_HEADER_TWO -> {
                HeaderTwo(
                    binding = ItemBlockHeaderTwoBinding.inflate(inflater, parent, false),
                    clicked = onClickListener,
                )
            }
            HOLDER_HEADER_THREE -> {
                HeaderThree(
                    binding = ItemBlockHeaderThreeBinding.inflate(inflater, parent, false),
                    clicked = onClickListener,
                )
            }
            HOLDER_CODE_SNIPPET -> {
                Code(
                    ItemBlockCodeSnippetBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_CHECKBOX -> {
                Checkbox(
                    binding = ItemBlockCheckboxBinding.inflate(
                        inflater, parent, false
                    ),
                    clicked = onClickListener
                ).apply {
                    setCheckboxClickListener(onCheckboxClicked)
                }
            }
            HOLDER_BULLET -> {
                Bulleted(
                    binding = ItemBlockBulletedBinding.inflate(
                        inflater, parent, false
                    ), clicked = onClickListener
                )
            }
            HOLDER_NUMBERED -> {
                Numbered(
                    binding = ItemBlockNumberedBinding.inflate(
                        inflater, parent, false
                    ), clicked = onClickListener
                )
            }
            HOLDER_TOGGLE -> {
                Toggle(
                    binding = ItemBlockToggleBinding.inflate(
                        inflater, parent, false
                    ),
                    clicked = onClickListener
                ).apply {
                    setupToggle(onToggleClicked, onTogglePlaceholderClicked)
                }
            }
            HOLDER_DESCRIPTION -> {
                Description(
                    ItemBlockDescriptionBinding.inflate(inflater, parent, false)
                ).apply {
                    with(content) {
                        addTextChangedListener(
                            DefaultTextWatcher { editable ->
                                val pos = bindingAdapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    val view = views[pos]
                                    check(view is BlockView.Description)
                                    view.text = editable.toString()
                                    onDescriptionChanged(view)
                                }
                            }
                        )
                        setOnFocusChangeListener { _, hasFocus ->
                            val pos = bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                dispatchFocusChanged(blocks[pos].id, hasFocus)
                            }
                        }
                        setOnEditorActionListener(
                            OnNewLineActionListener(
                                onEnter = { tv ->
                                    withBlock<BlockView.Description> { item ->
                                        onSplitDescription(
                                            item.id,
                                            tv.editableText,
                                            tv.selectionStart..tv.selectionEnd
                                        )
                                    }
                                })
                        )
                        selectionWatcher = { selection ->
                            val pos = bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                val view = views[pos]
                                if (view is BlockView.Description) {
                                    view.cursor = selection.last
                                }
                                onSelectionChanged(view.id, selection)
                            }
                        }
                    }
                }
            }
            HOLDER_FILE -> {
                File(
                    ItemBlockFileBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_FILE_PLACEHOLDER -> {
                FilePlaceholder(
                    ItemBlockMediaPlaceholderBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_FILE_UPLOAD -> {
                FileUpload(
                    ItemBlockMediaPlaceholderBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_FILE_ERROR -> {
                FileError(
                    ItemBlockMediaErrorBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_BUTTON_OPEN_FILE -> {
                OpenFile(ItemBlockOpenFileBinding.inflate(inflater, parent, false))
            }
            HOLDER_BUTTON_OPEN_IMAGE -> {
                OpenImage(ItemBlockOpenFileBinding.inflate(inflater, parent, false))
            }
            HOLDER_VIDEO -> {
                Video(
                    ItemBlockVideoBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_VIDEO_PLACEHOLDER -> {
                VideoPlaceholder(
                    ItemBlockMediaPlaceholderBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_VIDEO_UPLOAD -> {
                VideoUpload(
                    ItemBlockMediaPlaceholderBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_VIDEO_ERROR -> {
                VideoError(
                    ItemBlockMediaErrorBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_OBJECT_LINK_DEFAULT -> {
                LinkToObject(
                    ItemBlockObjectLinkBinding.inflate(inflater, parent, false)
                ).apply {
                    objectIcon.checkbox.setOnClickListener {
                        val pos = bindingAdapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            val view = views[pos] as BlockView.LinkToObject.Default.Text
                            onLinkToObjectCheckedIconClicked(
                                target = view.id,
                                icon = view.icon
                            )
                        }
                    }
                }
            }
            HOLDER_OBJECT_LINK_CARD_SMALL_ICON -> {
                LinkToObjectCardSmallIcon(
                    binding = ItemBlockObjectLinkCardSmallIconBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).apply {
                    objectIconView.checkbox.setOnClickListener {
                        val pos = bindingAdapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            val view = views[pos] as BlockView.LinkToObject.Default.Card.SmallIcon
                            onLinkToObjectCheckedIconClicked(
                                target = view.id,
                                icon = view.icon
                            )
                        }
                    }
                }
            }
            HOLDER_OBJECT_LINK_CARD_MEDIUM_ICON -> {
                LinkToObjectCardMediumIcon(
                    binding = ItemBlockObjectLinkCardMediumIconBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).apply {
                    objectIconView.checkbox.setOnClickListener {
                        val pos = bindingAdapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            val view = views[pos] as BlockView.LinkToObject.Default.Card.MediumIcon
                            onLinkToObjectCheckedIconClicked(
                                target = view.id,
                                icon = view.icon
                            )
                        }
                    }
                }
            }
            HOLDER_OBJECT_LINK_CARD_SMALL_ICON_COVER -> {
                LinkToObjectCardSmallIconCover(
                    binding = ItemBlockObjectLinkCardSmallIconCoverBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).apply {
                    objectIconView.checkbox.setOnClickListener {
                        val pos = bindingAdapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            val view =
                                views[pos] as BlockView.LinkToObject.Default.Card.SmallIconCover
                            onLinkToObjectCheckedIconClicked(
                                target = view.id,
                                icon = view.icon
                            )
                        }
                    }
                }
            }
            HOLDER_OBJECT_LINK_CARD_MEDIUM_ICON_COVER -> {
                LinkToObjectCardMediumIconCover(
                    binding = ItemBlockObjectLinkCardMediumIconCoverBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).apply {
                    objectIconView.checkbox.setOnClickListener {
                        val pos = bindingAdapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            val view =
                                views[pos] as BlockView.LinkToObject.Default.Card.MediumIconCover
                            onLinkToObjectCheckedIconClicked(
                                target = view.id,
                                icon = view.icon
                            )
                        }
                    }
                }
            }
            HOLDER_OBJECT_LINK_ARCHIVE -> {
                LinkToObjectArchive(
                    ItemBlockObjectLinkArchiveBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_OBJECT_LINK_DELETED -> {
                LinkToObjectDelete(
                    ItemBlockObjectLinkDeleteBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_OBJECT_LINK_LOADING -> {
                LinkToObjectLoading(
                    ItemBlockObjectLinkLoadingBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_BOOKMARK -> {
                Bookmark(
                    ItemBlockBookmarkBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_BOOKMARK_PLACEHOLDER -> {
                BookmarkPlaceholder(
                    ItemBlockMediaPlaceholderBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_BOOKMARK_ERROR -> {
                BookmarkError(
                    ItemBlockMediaErrorBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_BOOKMARK_UPLOAD -> {
                BookmarkUpload(
                    ItemBlockMediaPlaceholderBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_PICTURE -> {
                Picture(
                    ItemBlockPictureBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_PICTURE_PLACEHOLDER -> {
                PicturePlaceholder(
                    ItemBlockMediaPlaceholderBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_PICTURE_UPLOAD -> {
                PictureUpload(
                    ItemBlockMediaPlaceholderBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_PICTURE_ERROR -> {
                PictureError(
                    ItemBlockMediaErrorBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_DIVIDER_LINE -> {
                DividerLine(
                    ItemBlockDividerLineBinding.inflate(
                        inflater, parent, false
                    )
                )
            }
            HOLDER_DIVIDER_DOTS -> {
                DividerDots(
                    ItemBlockDividerDotsBinding.inflate(
                        inflater, parent, false
                    )
                )
            }
            HOLDER_HIGHLIGHT -> {
                Highlight(
                    binding = ItemBlockHighlightBinding.inflate(
                        inflater, parent, false
                    ), clicked = onClickListener
                )
            }
            HOLDER_CALLOUT -> {
                Callout(
                    binding = ItemBlockCalloutBinding.inflate(inflater, parent, false),
                    clicked = onClickListener
                )
            }
            HOLDER_RELATION_DEFAULT -> {
                val binding = ItemBlockRelationDefaultBinding.inflate(inflater, parent, false)
                RelationBlockViewHolder.Default(binding).setup(this)
            }
            HOLDER_RELATION_DATE -> {
                val binding = ItemBlockRelationDefaultBinding.inflate(inflater, parent, false)
                RelationBlockViewHolder.Date(binding).setup(this)
            }
            HOLDER_RELATION_PLACEHOLDER -> {
                RelationBlockViewHolder.Placeholder(
                    ItemBlockRelationPlaceholderBinding.inflate(inflater, parent, false)
                ).setupPlaceholder(this)
            }
            HOLDER_RELATION_DELETED -> {
                val binding = ItemBlockRelationDeletedBinding.inflate(inflater, parent, false)
                RelationBlockViewHolder.Deleted(binding).setup(this)
            }
            HOLDER_RELATION_STATUS -> {
                val binding = ItemBlockRelationStatusBinding.inflate(inflater, parent, false)
                RelationBlockViewHolder.Status(binding).setup(this)
            }
            HOLDER_RELATION_TAGS -> {
                RelationBlockViewHolder.Tags(
                    ItemBlockRelationTagBinding.inflate(inflater, parent, false)
                ).setup(this)
            }
            HOLDER_RELATION_OBJECT -> {
                RelationBlockViewHolder.Object(
                    ItemBlockRelationObjectBinding.inflate(inflater, parent, false)
                ).setup(this)
            }
            HOLDER_RELATION_FILE -> {
                RelationBlockViewHolder.File(
                    ItemBlockRelationFileBinding.inflate(inflater, parent, false)
                ).setup(this)
            }
            HOLDER_RELATION_CHECKBOX -> {
                val binding = ItemBlockRelationCheckboxBinding.inflate(inflater, parent, false)
                RelationBlockViewHolder.Checkbox(binding).setup(this)
            }
            HOLDER_FEATURED_RELATION -> {
                FeaturedRelationListViewHolder(
                    ItemBlockFeaturedRelationsBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_LATEX -> Latex(
                ItemBlockLatexBinding.inflate(inflater, parent, false)
            ).apply {
                //latexView.isLongClickable = true
                latexView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val pos = bindingAdapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            onClickListener(ListenerType.Latex(blocks[pos].id))
                        }
                        v.performClick()
                        true
                    } else {
                        false
                    }
                }
            }
            HOLDER_EMBED -> EmbedPlaceholder(
                ItemBlockEmbedBinding.inflate(inflater, parent, false)
            )
            HOLDER_TOC -> {
                TableOfContents(
                    binding = ItemBlockTocBinding.inflate(inflater, parent, false),
                    onDragAndDropTrigger = onDragAndDropTrigger
                )
            }
            HOLDER_UNSUPPORTED -> Unsupported(
                ItemBlockUnsupportedBinding.inflate(inflater, parent, false)
            )
            HOLDER_TABLE -> TableBlockHolder(
                binding = ItemBlockTableBinding.inflate(inflater, parent, false),
                clickListener = onClickListener,
                onTextBlockTextChanged = onTextBlockTextChanged,
                onMentionEvent = onMentionEvent,
                onCellSelectionChanged = onCellSelectionChanged,
                onFocusChanged = ::dispatchFocusChanged,
                clipboardInterceptor = clipboardInterceptor,
                onDragAndDropTrigger = onDragAndDropTrigger
            )
            HOLDER_DATA_VIEW_EMPTY_SOURCE -> {
                DataViewBlockEmptySourceHolder(
                    ItemBlockDataViewEmptySourceBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_DATA_VIEW_EMPTY_DATA -> {
                DataViewBlockEmptyDataHolder(
                    ItemBlockDataViewEmptyDataBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_DATA_VIEW_DEFAULT -> {
                DataViewBlockDefaultHolder(
                    ItemBlockDataViewDefaultBinding.inflate(inflater, parent, false)
                )
            }
            HOLDER_DATA_VIEW_SOURCE_DELETED -> {
                DataViewBlockDeleteHolder(
                    ItemBlockDataViewEmptyDataBinding.inflate(inflater, parent, false)
                )
            }
            else -> throw IllegalStateException("Unexpected view type: $viewType")
        }

        if (holder is Text<*>) {
            holder.content.setOnDragListener(onDragListener)
            holder.content.editorTouchProcessor.onLongClick = {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onClickListener(LongClick(target = blocks[pos].id))
                }
            }
            holder.content.editorTouchProcessor.onDragAndDropTrigger = {
                onDragAndDropTrigger(holder, it)
            }
            holder.content.setOnClickListener { view ->
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onTextInputClicked(blocks[pos].id)
                }
            }
            if (isTabKeyEnabled) {
                holder.content.tabKeyWatcher = { isShift ->
                    holder.withBlock<BlockView.Text> { item ->
                        onKeyPressedEvent(
                            KeyPressedEvent.OnTabKeyEvent(
                                target = item.id,
                                text = item.text,
                                marks = item.marks.map { it.mark() },
                                isShift = isShift
                            )
                        )
                    }
                }
            }
            holder.content.selectionWatcher = { selection ->
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val view = views[pos]
                    if (view is BlockView.Text) {
                        view.cursor = selection.last
                    }
                    onSelectionChanged(view.id, selection)
                }
            }
            holder.setupViewHolder(
                onTextChanged = { editable ->
                    holder.withBlock<BlockView.Text> { item ->
                        item.apply {
                            text = editable.toString()
                            marks = editable.marks()
                        }
                        onTextBlockTextChanged(item)
                    }
                },
                onEmptyBlockBackspaceClicked = onEmptyBlockBackspaceClicked,
                onSplitLineEnterClicked = onSplitLineEnterClicked,
                onNonEmptyBlockBackspaceClicked = onNonEmptyBlockBackspaceClicked,
                onMentionEvent = onMentionEvent,
                onSlashEvent = onSlashEvent,
                onBackPressedCallback = onBackPressedCallback,
            )
        } else {
            if (holder !is SupportCustomTouchProcessor) {
                when (holder) {
                    is RelationBlockViewHolder -> {
                        val touchSlop = ViewConfiguration.get(holder.itemView.context).scaledTouchSlop
                        val processor = EditorTouchProcessor(
                            touchSlop = touchSlop,
                            fallback = { holder.itemView.onTouchEvent(it) },
                            onLongClick = {
                                val pos = holder.bindingAdapterPosition
                                if (pos != RecyclerView.NO_POSITION) {
                                    onClickListener(LongClick(target = blocks[pos].id))
                                }
                            },
                            onDragAndDropTrigger = { onDragAndDropTrigger(holder, it) }
                        )
                        holder.itemView.setOnTouchListener { v, e -> processor.process(v, e) }
                    }
                    is Code -> {
                        holder.editorTouchProcessor.onLongClick = {
                            val pos = holder.bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                onClickListener(LongClick(target = blocks[pos].id))
                            }
                        }
                        holder.editorTouchProcessor.onDragAndDropTrigger = {
                            onDragAndDropTrigger(holder, it)
                        }
                        holder.setupViewHolder(
                            onTextChanged = onTextChanged,
                            onSelectionChanged = onSelectionChanged,
                            onFocusChanged = ::dispatchFocusChanged
                        )
                    }
                    is Title -> {}
                    else -> {
                        holder.itemView.setOnLongClickListener {
                            val pos = holder.bindingAdapterPosition
                            if (pos != RecyclerView.NO_POSITION) {
                                onClickListener(LongClick(target = blocks[pos].id))
                            }
                            true
                        }
                    }
                }
            } else {
                holder.editorTouchProcessor.onLongClick = {
                    val pos = holder.bindingAdapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        onClickListener(LongClick(target = blocks[pos].id))
                    }
                }
                holder.editorTouchProcessor.onDragAndDropTrigger = {
                    onDragAndDropTrigger(holder, it)
                }
            }
            holder.itemView.setOnDragListener(onDragListener)
        }

        if (holder is TextHolder) {
            holder.content.onFocusChangeListener = LockableFocusChangeListener { hasFocus ->
                holder.content.isCursorVisible = hasFocus
                // Suppressed focus changes must not touch the view model either:
                // a transient system focus transfer during a list update would
                // otherwise stamp isFocused onto the wrong block.
                if (!suppressFocusCallbacks) {
                    val pos = holder.bindingAdapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        val item = views[pos]
                        if (item is Focusable) {
                            item.isFocused = hasFocus
                        }
                        onFocusChanged(item.id, hasFocus)
                    }
                }
            }
        }

        return holder
    }

    override fun getItemViewType(position: Int) = blocks[position].getViewType()

    override fun getItemCount(): Int = blocks.size

    override fun onBindViewHolder(
        holder: BlockViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (isInDragAndDropMode) trySetDesiredAppearanceForDraggedItem(holder, position)
        when {
            payloads.isEmpty() -> onBindViewHolder(holder, position)
            else -> {
                if (holder is BlockViewHolder.IndentableHolder) {
                    holder.processIndentChange(blocks[position], payloads.typeOf())
                }
                when (holder) {
                    is Paragraph -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener
                        )
                    }
                    is Bulleted -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener
                        )
                    }
                    is Checkbox -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener
                        )
                    }
                    is Title.Document -> {
                        holder.processPayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Title
                        )
                    }
                    is Title.Profile -> {
                        holder.processPayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Title.Profile
                        )
                    }
                    is Title.Todo -> {
                        holder.processPayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Title.Todo
                        )
                    }
                    is Title.File -> {
                        holder.processPayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Title.File
                        )
                    }
                    is Title.Video -> {
                        holder.processPayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Title.Video
                        )
                    }

                    is Title.Image -> {
                        holder.processPayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Title.Image
                        )
                    }

                    is Numbered -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener,
                        )
                    }
                    is HeaderOne -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener
                        )
                    }
                    is HeaderTwo -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener,
                        )
                    }
                    is HeaderThree -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener,
                        )
                    }
                    is Toggle -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener
                        )
                    }
                    is Highlight -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener
                        )
                    }
                    is Callout -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position],
                            clicked = onClickListener
                        )
                    }
                    is File -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is FilePlaceholder -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is FileError -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is FileUpload -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is Picture -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is PicturePlaceholder -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is PictureError -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is PictureUpload -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is Video -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is VideoPlaceholder -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is VideoError -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is VideoUpload -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is LinkToObject -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is LinkToObjectCardSmallIcon -> {
                        holder.processChangePayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.LinkToObject.Default.Card.SmallIcon
                        )
                    }
                    is LinkToObjectCardMediumIcon -> {
                        holder.processChangePayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.LinkToObject.Default.Card.MediumIcon
                        )
                    }
                    is LinkToObjectCardSmallIconCover -> {
                        holder.processChangePayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.LinkToObject.Default.Card.SmallIconCover
                        )
                    }
                    is LinkToObjectCardMediumIconCover -> {
                        holder.processChangePayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.LinkToObject.Default.Card.MediumIconCover
                        )
                    }
                    is LinkToObjectArchive -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is LinkToObjectDelete -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is LinkToObjectLoading -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is Bookmark -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is BookmarkPlaceholder -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is BookmarkError -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position]
                        )
                    }
                    is BookmarkUpload -> {
                        val item = blocks[position] as BlockView.Upload.Bookmark
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = item
                        )
                        holder.setUrl(item.url)
                    }
                    is Code -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Code,
                            onTextChanged = onTextChanged,
                            onSelectionChanged = onSelectionChanged
                        )
                    }
                    is DividerLine -> onBindViewHolder(holder, position)
                    is DividerDots -> onBindViewHolder(holder, position)
                    is RelationBlockViewHolder -> {
                        when (holder) {
                            is RelationBlockViewHolder.Placeholder -> {
                                onBindViewHolder(holder, position)
                            }
                            is RelationBlockViewHolder.Deleted -> {
                                onBindViewHolder(holder, position)
                            }
                            else -> {
                                holder.processChangePayload(
                                    payloads = payloads.typeOf(),
                                    block = blocks[position] as BlockView.Relation.Related
                                )
                            }
                        }
                    }
                    is Description -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Description
                        )
                    }
                    is Latex -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Latex
                        )
                    }
                    is EmbedPlaceholder -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Embed,
                            clicked = onClickListener
                        )
                    }
                    is TableOfContents -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.TableOfContents
                        )
                    }
                    is TableBlockHolder -> {
                        holder.processChangePayload(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.Table
                        )
                    }
                    is DataViewBlockDefaultHolder -> {
                        holder.processChangePayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.DataView.Default
                        )
                    }
                    is DataViewBlockEmptySourceHolder -> {
                        holder.processChangePayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.DataView.EmptySource
                        )
                    }
                    is DataViewBlockEmptyDataHolder -> {
                        holder.processChangePayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.DataView.EmptyData
                        )
                    }
                    is DataViewBlockDeleteHolder -> {
                        holder.processChangePayloads(
                            payloads = payloads.typeOf(),
                            item = blocks[position] as BlockView.DataView.Deleted
                        )
                    }
                    is Unsupported -> {
                        holder.bind(item = blocks[position] as BlockView.Unsupported)
                    }
                    else -> {
                        Timber.e("Unexpected view holder: $holder")
                    }
                }
                checkIfDecorationChanged(holder, payloads.typeOf(), position)
            }
        }

        if (restore.isNotEmpty()) {
            val block = blocks[position]
            val command = restore.poll()
            if (command is Editor.Restore.Selection) {
                if (block.id == command.target) {
                    if (holder is TextHolder) {
                        holder.content.post {
                            val textLength = holder.content.text?.length ?: 0
                            val start = command.range.first.coerceIn(0, textLength)
                            val end = command.range.last.coerceIn(0, textLength)
                            
                            // Ensure start <= end to prevent StringIndexOutOfBoundsException
                            if (start <= end && textLength > 0) {
                                holder.content.setSelection(start, end)
                            } else if (start > end) {
                                // Log error and fix invalid range by setting cursor to the smaller valid position
                                timber.log.Timber.e("Invalid selection range: start=$start > end=$end, textLength=$textLength, fixing to cursor position")
                                val cursorPos = minOf(start, end).coerceIn(0, textLength)
                                holder.content.setSelection(cursorPos)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        if (isInDragAndDropMode) trySetDesiredAppearanceForDraggedItem(holder, position)
        when (holder) {
            is Paragraph -> holder.bind(blocks[position] as BlockView.Text.Paragraph)
            is HeaderOne -> holder.bind(blocks[position] as BlockView.Text.Header.One)
            is HeaderTwo -> holder.bind(blocks[position] as BlockView.Text.Header.Two)
            is HeaderThree -> holder.bind(blocks[position] as BlockView.Text.Header.Three)
            is Checkbox -> holder.bind(blocks[position] as BlockView.Text.Checkbox)
            is Bulleted -> holder.bind(blocks[position] as BlockView.Text.Bulleted)
            is Numbered -> holder.bind(blocks[position] as BlockView.Text.Numbered)
            is Toggle -> holder.bind(item = blocks[position] as BlockView.Text.Toggle)
            is Highlight -> holder.bind(blocks[position] as BlockView.Text.Highlight)
            is Callout -> holder.bind(blocks[position] as BlockView.Text.Callout)
            is Title.Document -> {
                holder.apply {
                    bind(
                        item = blocks[position] as BlockView.Title.Basic,
                        onPageIconClicked = onPageIconClicked,
                        onCoverClicked = onCoverClicked,
                        click = onClickListener
                    )
                    setTextInputClickListener {
                        if (Build.VERSION.SDK_INT == N || Build.VERSION.SDK_INT == N_MR1) {
                            content.context.imm()
                                .showSoftInput(content, InputMethodManager.SHOW_FORCED)
                        }
                        onTitleTextInputClicked()
                    }
                    holder.content.clipboardInterceptor = clipboardInterceptor
                }
            }
            is Title.Todo -> {
                holder.apply {
                    bind(
                        item = blocks[position] as BlockView.Title.Todo,
                        onPageIconClicked = onPageIconClicked,
                        onCoverClicked = onCoverClicked,
                        click = onClickListener
                    )
                    setTextInputClickListener {
                        if (Build.VERSION.SDK_INT == N || Build.VERSION.SDK_INT == N_MR1) {
                            content.context.imm()
                                .showSoftInput(content, InputMethodManager.SHOW_FORCED)
                        }
                        onTitleTextInputClicked()
                    }
                    holder.content.clipboardInterceptor = clipboardInterceptor
                }
            }
            is Title.Profile -> {
                holder.apply {
                    bind(
                        item = blocks[position] as BlockView.Title.Profile,
                        onProfileIconClicked = onClickListener,
                        onCoverClicked = onCoverClicked,
                        click = onClickListener
                    )
                    setTextInputClickListener {
                        if (Build.VERSION.SDK_INT == N || Build.VERSION.SDK_INT == N_MR1) {
                            content.context.imm()
                                .showSoftInput(content, InputMethodManager.SHOW_FORCED)
                        }
                        onTitleTextInputClicked()
                    }
                    holder.content.clipboardInterceptor = clipboardInterceptor
                }
            }
            is Title.File -> {
                holder.apply {
                    bind(item = blocks[position] as BlockView.Title.File)
                }
            }

            is Title.Video -> {
                holder.apply {
                    bind(
                        item = blocks[position] as BlockView.Title.Video,
                        onPlayClicked = {
                            onClickListener(ListenerType.Header.Video)
                        }
                    )
                }
            }

            is Title.Image -> {
                holder.apply {
                    bind(
                        item = blocks[position] as BlockView.Title.Image,
                        clicked = onClickListener
                    )
                }
            }
            is Code -> {
                holder.bind(
                    item = blocks[position] as BlockView.Code,
                    clicked = onClickListener,
                    onTextInputClicked = onTextInputClicked
                )
            }
            is Description -> {
                holder.bind(blocks[position] as BlockView.Description)
            }
            is File -> {
                holder.bind(
                    item = blocks[position] as BlockView.Media.File,
                    clicked = onClickListener
                )
            }
            is FileError -> {
                holder.bind(
                    item = blocks[position] as BlockView.Error.File,
                    clicked = onClickListener
                )
            }
            is FileUpload -> {
                holder.bind(
                    item = blocks[position] as BlockView.Upload.File,
                    clicked = onClickListener
                )
            }
            is FilePlaceholder -> {
                holder.bind(
                    item = blocks[position] as BlockView.MediaPlaceholder.File,
                    clicked = onClickListener
                )
            }
            is Video -> {
                holder.bind(
                    item = blocks[position] as BlockView.Media.Video,
                    clicked = onClickListener
                )
            }
            is VideoUpload -> {
                holder.bind(
                    item = blocks[position] as BlockView.Upload.Video,
                    clicked = onClickListener
                )
            }
            is VideoPlaceholder -> {
                holder.bind(
                    item = blocks[position] as BlockView.MediaPlaceholder.Video,
                    clicked = onClickListener
                )
            }
            is VideoError -> {
                holder.bind(
                    item = blocks[position] as BlockView.Error.Video,
                    clicked = onClickListener
                )
            }
            is LinkToObject -> {
                holder.bind(
                    item = blocks[position] as BlockView.LinkToObject.Default.Text,
                    clicked = onClickListener
                )
            }
            is LinkToObjectCardSmallIcon -> {
                holder.bind(
                    item = blocks[position] as BlockView.LinkToObject.Default.Card.SmallIcon,
                    clicked = onClickListener
                )
            }
            is LinkToObjectCardMediumIcon -> {
                holder.bind(
                    item = blocks[position] as BlockView.LinkToObject.Default.Card.MediumIcon,
                    clicked = onClickListener
                )
            }
            is LinkToObjectCardSmallIconCover -> {
                holder.bind(
                    item = blocks[position] as BlockView.LinkToObject.Default.Card.SmallIconCover,
                    clicked = onClickListener
                )
            }
            is LinkToObjectCardMediumIconCover -> {
                holder.bind(
                    item = blocks[position] as BlockView.LinkToObject.Default.Card.MediumIconCover,
                    clicked = onClickListener
                )
            }
            is LinkToObjectArchive -> {
                holder.bind(
                    item = blocks[position] as BlockView.LinkToObject.Archived,
                    clicked = onClickListener
                )
            }
            is LinkToObjectDelete -> {
                holder.bind(
                    item = blocks[position] as BlockView.LinkToObject.Deleted,
                    clicked = onClickListener
                )
            }
            is LinkToObjectLoading -> {
                holder.bind(
                    item = blocks[position] as BlockView.LinkToObject.Loading,
                    clicked = onClickListener
                )
            }
            is Bookmark -> {
                holder.bind(
                    item = blocks[position] as BlockView.Media.Bookmark,
                    clicked = onClickListener
                )
            }
            is BookmarkPlaceholder -> {
                holder.bind(
                    item = blocks[position] as BlockView.MediaPlaceholder.Bookmark,
                    clicked = onClickListener
                )
            }
            is BookmarkError -> {
                val item = blocks[position] as BlockView.Error.Bookmark
                holder.bind(
                    item = item,
                    clicked = onClickListener
                )
            }
            is BookmarkUpload -> {
                val item = blocks[position] as BlockView.Upload.Bookmark
                holder.bind(
                    item = item,
                    clicked = onClickListener
                )
                holder.setUrl(item.url)
            }
            is Picture -> {
                holder.bind(
                    item = blocks[position] as BlockView.Media.Picture,
                    clicked = onClickListener
                )
            }
            is PicturePlaceholder -> {
                holder.bind(
                    item = blocks[position] as BlockView.MediaPlaceholder.Picture,
                    clicked = onClickListener
                )
            }
            is PictureError -> {
                holder.bind(
                    item = blocks[position] as BlockView.Error.Picture,
                    clicked = onClickListener
                )
            }
            is PictureUpload -> {
                holder.bind(
                    item = blocks[position] as BlockView.Upload.Picture,
                    clicked = onClickListener
                )
            }
            is DividerLine -> {
                holder.bind(
                    item = blocks[position] as BlockView.DividerLine,
                    clicked = onClickListener
                )
            }
            is DividerDots -> {
                holder.bind(
                    item = blocks[position] as BlockView.DividerDots,
                    clicked = onClickListener
                )
            }
            is RelationBlockViewHolder.Placeholder -> {
                val item = (blocks[position] as BlockView.Relation.Placeholder)
                holder.bind(item = item)
            }
            is RelationBlockViewHolder.Deleted -> {
                val item = (blocks[position] as BlockView.Relation.Deleted)
                holder.bind(item = item)
            }
            is RelationBlockViewHolder.Default -> {
                val item = (blocks[position] as BlockView.Relation.Related)
                holder.bind(item = item.view)
                holder.bindHolder(item)
            }
            is RelationBlockViewHolder.Date -> {
                val item = (blocks[position] as BlockView.Relation.Related)
                holder.bind(item = item.view)
                holder.bindHolder(item)
            }
            is RelationBlockViewHolder.Status -> {
                val item = (blocks[position] as BlockView.Relation.Related)
                holder.bind(item = item.view)
                holder.bindHolder(item)
            }
            is RelationBlockViewHolder.Tags -> {
                val item = (blocks[position] as BlockView.Relation.Related)
                holder.bind(item = item.view)
                holder.bindHolder(item)
            }
            is RelationBlockViewHolder.Object -> {
                val item = (blocks[position] as BlockView.Relation.Related)
                holder.bind(item = item.view)
                holder.bindHolder(item)
            }
            is RelationBlockViewHolder.File -> {
                val item = (blocks[position] as BlockView.Relation.Related)
                holder.bind(item = item.view)
                holder.bindHolder(item)
            }
            is RelationBlockViewHolder.Checkbox -> {
                val item = (blocks[position] as BlockView.Relation.Related)
                holder.bind(item = item.view)
                holder.bindHolder(item)
            }
            is FeaturedRelationListViewHolder -> {
                holder.bind(
                    item = blocks[position] as BlockView.FeaturedRelation,
                    click = onClickListener
                )
            }
            is Latex -> {
                holder.bind(item = blocks[position] as BlockView.Latex)
            }
            is EmbedPlaceholder -> {
                holder.bind(
                    item = blocks[position] as BlockView.Embed,
                    clicked = onClickListener
                )
            }
            is TableOfContents -> {
                holder.bind(
                    item = blocks[position] as BlockView.TableOfContents,
                    clicked = onClickListener
                )
            }
            is Unsupported -> {
                holder.bind(item = blocks[position] as BlockView.Unsupported)
            }
            is TableBlockHolder -> {
                holder.bind(item = blocks[position] as BlockView.Table)
            }
            is DataViewBlockEmptySourceHolder -> {
                holder.bind(
                    item = blocks[position] as BlockView.DataView.EmptySource,
                    clicked = onClickListener
                )
            }
            is DataViewBlockEmptyDataHolder -> {
                holder.bind(
                    item = blocks[position] as BlockView.DataView.EmptyData,
                    clicked = onClickListener
                )
            }
            is DataViewBlockDefaultHolder -> {
                holder.bind(
                    item = blocks[position] as BlockView.DataView.Default,
                    clicked = onClickListener
                )
            }
            is DataViewBlockDeleteHolder -> {
                holder.bind(
                    item = blocks[position] as BlockView.DataView.Deleted,
                    clicked = onClickListener
                )
            }
            is OpenFile -> {
                holder.bind(
                    item = blocks[position] as BlockView.ButtonOpenFile.FileButton,
                    click = onClickListener
                )
            }
            is OpenImage -> {
                holder.bind(
                    item = blocks[position] as BlockView.ButtonOpenFile.ImageButton,
                    click = onClickListener
                )
            }
        }

        if (holder is Text<*>) {

            val block = blocks[position]

            if (block is BlockView.Alignable) {
                block.alignment?.let {
                    holder.setAlignment(alignment = it)
                }
            }

            holder.content.clipboardInterceptor = clipboardInterceptor
        }

        if (holder is DecoratableViewHolder) {
            val block = blocks[position]
            check(block is BlockView.Decoratable)
            holder.applyDecorations(decorations = block.decorations)
        }
    }

    // Bug workaround for losing text selection ability, see:
    // https://code.google.com/p/android/issues/detail?id=208169
    override fun onViewAttachedToWindow(holder: BlockViewHolder) {
        super.onViewAttachedToWindow(holder)
        // Never toggle a focused widget: setEnabled(false) hides the soft input while it
        // is active, and setEnabled(true) calls InputMethodManager.restartInput(), which
        // terminates any in-progress IME composition. A holder coming back from the
        // recycled-view pool has already had its focus cleared, so the workaround still
        // applies wherever it was actually needed.
        if (holder is TextHolder && !holder.content.hasFocus()) {
            holder.content.isEnabled = false
            holder.content.isEnabled = true
        }
    }

    override fun onViewRecycled(holder: BlockViewHolder) {
        super.onViewRecycled(holder)
        if (holder is TableBlockHolder) {
            holder.cleanup()
        }
    }

    fun updateWithDiffUtil(items: List<BlockView>) {
        if (BuildConfig.DEBUG) {
            Timber.d("----------Blocks dispatched to adapter---------------------")
        }
        val result = DiffUtil.calculateDiff(BlockViewDiffUtil(old = blocks, new = items))
        blocks = items
        result.dispatchUpdatesTo(this)
    }

    /**
     * Applies a diff result calculated in advance (e.g. on a background thread).
     * Must be called on the main thread with a [result] calculated
     * for old = current [views] and new = [items].
     */
    fun applyDiffResult(items: List<BlockView>, result: DiffUtil.DiffResult) {
        if (BuildConfig.DEBUG) {
            Timber.d("----------Blocks dispatched to adapter---------------------")
        }
        blocks = items
        result.dispatchUpdatesTo(this)
    }

    private fun checkIfDecorationChanged(
        holder: BlockViewHolder,
        payloads: List<Payload>,
        position: Int
    ) {
        if (holder is DecoratableViewHolder && payloads.any { p -> p.isDecorationChanged }) {
            val block = blocks[position]
            check(block is BlockView.Decoratable)
            holder.onDecorationsChanged(decorations = block.decorations)
        }
    }

    override fun provide(pos: Int): BlockView {
        return blocks[pos]
    }

    private fun onLinkToObjectCheckedIconClicked(target: Id, icon: ObjectIcon) {
        if (icon is ObjectIcon.Task) {
            onClickListener(
                ListenerType.LinkToObjectCheckboxUpdate(
                    target = target,
                    isChecked = icon.isChecked
                )
            )
        }
    }
}