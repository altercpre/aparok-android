package io.aparok.app.presentation.editor.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.icon.RemoveTextBlockIcon
import io.aparok.app.domain.icon.SetTextBlockEmoji
import io.aparok.app.domain.icon.SetTextBlockImage
import io.aparok.app.emojifier.data.EmojiProvider
import io.aparok.app.emojifier.suggest.EmojiSuggester
import io.aparok.app.presentation.util.Dispatcher

class TextBlockIconPickerViewModelFactory(
    private val setEmojiIcon: SetTextBlockEmoji,
    private val setImageIcon: SetTextBlockImage,
    private val removeDocumentIcon: RemoveTextBlockIcon,
    private val emojiSuggester: EmojiSuggester,
    private val emojiProvider: EmojiProvider,
    private val dispatcher: Dispatcher<Payload>,
    private val analytics: Analytics
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ObjectIconPickerViewModel(
            setEmojiIcon = setEmojiIcon,
            setImageIcon = setImageIcon,
            removeDocumentIcon = removeDocumentIcon,
            suggester = emojiSuggester,
            provider = emojiProvider,
            dispatcher = dispatcher,
            analytics = analytics
        ) as T
    }
}