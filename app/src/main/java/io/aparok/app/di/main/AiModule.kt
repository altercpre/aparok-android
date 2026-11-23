package io.aparok.app.di.main

import io.aparok.app.domain.ai.TypeSuggestionEngine
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Sovereign AI Module.
 * Type suggestions degrade cleanly to NoOp in the sovereign build.
 */
@Module
object AiModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideTypeSuggestionEngine(): TypeSuggestionEngine = TypeSuggestionEngine.NoOp
}

