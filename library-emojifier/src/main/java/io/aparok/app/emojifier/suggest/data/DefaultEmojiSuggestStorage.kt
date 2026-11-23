package io.aparok.app.emojifier.suggest.data

import android.content.Context
import io.aparok.app.core_utils.ext.getJsonDataFromAsset
import io.aparok.app.emojifier.Emojifier.Config.EMOJI_FILE
import io.aparok.app.emojifier.suggest.model.EmojiModel
import io.aparok.app.emojifier.suggest.model.EmojiSuggest
import com.google.gson.Gson

class DefaultEmojiSuggestStorage(
    private val context: Context,
    private val gson: Gson
) : EmojiSuggestStorage {

    override suspend fun fetch(): List<EmojiSuggest> {
        val json = context.getJsonDataFromAsset(EMOJI_FILE)
        return if (json != null) {
            gson.fromJson(json, Array<EmojiModel>::class.java).toList()
        } else {
            emptyList()
        }
    }
}