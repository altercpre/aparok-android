package io.aparok.app.emojifier.suggest.data

import io.aparok.app.emojifier.suggest.model.EmojiSuggest

interface EmojiSuggestStorage {
    suspend fun fetch(): List<EmojiSuggest>
}