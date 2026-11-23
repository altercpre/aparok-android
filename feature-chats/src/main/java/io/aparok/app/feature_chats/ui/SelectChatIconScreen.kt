package io.aparok.app.feature_chats.ui

import androidx.compose.runtime.Composable
import io.aparok.app.core_ui.widgets.EmojiPickerScreen
import io.aparok.app.presentation.picker.EmojiPickerView

@Composable
fun SelectChatIconScreen(
    views: List<EmojiPickerView> = emptyList(),
    onEmojiClicked: (String) -> Unit,
    onQueryChanged: (String) -> Unit
) {
    EmojiPickerScreen(
        views = views,
        onEmojiClicked = onEmojiClicked,
        onQueryChanged = onQueryChanged
    )
}
