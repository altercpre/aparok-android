package io.aparok.app.core_ui.uitests

import android.text.Editable
import androidx.lifecycle.Lifecycle
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Url
import io.aparok.app.core_ui.BlockAdapterTest
import io.aparok.app.core_ui.features.editor.BlockAdapter
import io.aparok.app.core_ui.features.editor.DragAndDropAdapterDelegate
import io.aparok.app.core_ui.features.editor.EditorDragAndDropListener
import io.aparok.app.core_ui.tools.ClipboardInterceptor
import io.aparok.app.presentation.editor.editor.model.BlockView
import java.util.*

private val clipboardInterceptor: ClipboardInterceptor = object : ClipboardInterceptor {
    override fun onClipboardAction(action: ClipboardInterceptor.Action) {}
    override fun onBookmarkPasted(url: Url) {}
}

fun givenAdapter(
    views: List<BlockView>,
    onSplitLineEnterClicked: (String, Editable, IntRange) -> Unit = { _, _, _ -> },
    onFocusChanged: (String, Boolean) -> Unit = { _, _ -> },
    onTitleCheckboxClicked: (BlockView.Title.Todo) -> Unit = {},
    onTitleBlockTextChanged: (Id, String) -> Unit = { _, _ -> },
    onTextChanged: (String, Editable) -> Unit = { _, _ -> },
    onToggleClicked: (String) -> Unit = {},
    onNonEmptyBlockBackspaceClicked: (String, Editable) -> Unit = { _, _ -> },
    onEmptyBlockBackspaceClicked: (String) -> Unit = {},
    onCheckboxClicked: (BlockView.Text.Checkbox) -> Unit = {},
    lifecycle: Lifecycle = BlockAdapterTest.TestLifecycle(),
): BlockAdapter {
    return BlockAdapter(
        restore = LinkedList(),
        initialBlock = views,
        onNonEmptyBlockBackspaceClicked = onNonEmptyBlockBackspaceClicked,
        onEmptyBlockBackspaceClicked = onEmptyBlockBackspaceClicked,
        onSplitLineEnterClicked = onSplitLineEnterClicked,
        onSplitDescription = { _, _, _ -> },
        onTextChanged = onTextChanged,
        onCheckboxClicked = onCheckboxClicked,
        onTitleCheckboxClicked = onTitleCheckboxClicked,
        onFocusChanged = onFocusChanged,
        onSelectionChanged = { _, _ -> },
        onTextInputClicked = {},
        onPageIconClicked = {},
        onTogglePlaceholderClicked = {},
        onToggleClicked = onToggleClicked,
        onTextBlockTextChanged = {},
        onTitleBlockTextChanged = onTitleBlockTextChanged,
        onTitleTextInputClicked = {},
        onClickListener = {},
        clipboardInterceptor = clipboardInterceptor,
        onMentionEvent = {},
        onBackPressedCallback = { false },
        onCoverClicked = {},
        onSlashEvent = {},
        onKeyPressedEvent = {},
        onDragListener = EditorDragAndDropListener(
            onDragEnded = { _, _ -> },
            onDragExited = {},
            onDragLocation = { _, _ -> },
            onDrop = { _, _ -> },
            onDragStart = {}
        ),
        onDragAndDropTrigger = { _, _ -> false },
        dragAndDropSelector = DragAndDropAdapterDelegate(),
        lifecycle = lifecycle,
        onCellSelectionChanged = { _, _ ->  }
    )
}