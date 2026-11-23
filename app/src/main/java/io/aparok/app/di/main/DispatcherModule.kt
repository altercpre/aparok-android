package io.aparok.app.di.main

import io.aparok.app.core_models.Payload
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.widgets.WidgetDispatchEvent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DispatcherModule {

    @JvmStatic
    @Provides
    @Singleton
    fun providePayloadDispatcher(): Dispatcher<Payload> = Dispatcher.Default()

    @JvmStatic
    @Provides
    @Singleton
    fun provideWidgetEventDispatcher(): Dispatcher<WidgetDispatchEvent> = Dispatcher.Default()
}