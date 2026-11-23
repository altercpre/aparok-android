package io.aparok.app.di.main


import android.content.Context
import io.aparok.app.domain.icon.DocumentEmojiIconProvider
import io.aparok.app.emojifier.data.DefaultDocumentEmojiIconProvider
import io.aparok.app.emojifier.suggest.EmojiSuggester
import io.aparok.app.emojifier.suggest.data.DefaultEmojiSuggestStorage
import io.aparok.app.emojifier.suggest.data.DefaultEmojiSuggester
import io.aparok.app.emojifier.suggest.data.EmojiSuggestStorage
import io.aparok.app.emojifier.suggest.data.EmojiSuggesterCache
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object EmojiModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideEmojiSuggester(
        cache: EmojiSuggesterCache,
        storage: EmojiSuggestStorage
    ): EmojiSuggester {
        return DefaultEmojiSuggester(
            cache = cache,
            storage = storage
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideEmojiSuggesterCache(): EmojiSuggesterCache {
        return EmojiSuggesterCache.DefaultCache()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideEmojiSuggestStorage(context: Context, gson: Gson): EmojiSuggestStorage {
        return DefaultEmojiSuggestStorage(context, gson)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideDocumentEmojiIconProvider(): DocumentEmojiIconProvider {
        return DefaultDocumentEmojiIconProvider()
    }
}