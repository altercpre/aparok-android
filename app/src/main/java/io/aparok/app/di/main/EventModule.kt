package io.aparok.app.di.main

import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.data.auth.account.AccountStatusDataChannel
import io.aparok.app.data.auth.account.AccountStatusRemoteChannel
import io.aparok.app.data.auth.event.ChatEventRemoteChannel
import io.aparok.app.data.auth.event.EventDataChannel
import io.aparok.app.data.auth.event.EventRemoteChannel
import io.aparok.app.data.auth.event.FileLimitsDataChannel
import io.aparok.app.data.auth.event.FileLimitsRemoteChannel
import io.aparok.app.data.auth.event.SubscriptionDataChannel
import io.aparok.app.data.auth.event.SubscriptionEventRemoteChannel
import io.aparok.app.data.auth.status.SyncAndP2PStatusEventsStore
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.account.AccountStatusChannel
import io.aparok.app.domain.chats.ChatEventChannel
import io.aparok.app.domain.event.interactor.EventChannel
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.workspace.FileLimitsEventChannel
import io.aparok.app.middleware.EventProxy
import io.aparok.app.middleware.interactor.AccountStatusMiddlewareChannel
import io.aparok.app.middleware.interactor.DefaultMiddlewareEventRegistrar
import io.aparok.app.middleware.interactor.EventHandler
import io.aparok.app.middleware.interactor.EventHandlerChannel
import io.aparok.app.middleware.interactor.EventHandlerChannelImpl
import io.aparok.app.middleware.interactor.FileLimitsMiddlewareChannel
import io.aparok.app.middleware.interactor.MiddlewareEventChannel
import io.aparok.app.middleware.interactor.MiddlewareEventRegistrar
import io.aparok.app.middleware.interactor.MiddlewareProtobufLogger
import io.aparok.app.middleware.interactor.MiddlewareSubscriptionEventChannel
import io.aparok.app.middleware.interactor.SyncAndP2PStatusEventsStoreImpl
import io.aparok.app.middleware.interactor.events.ChatEventMiddlewareChannel
import io.aparok.app.presentation.common.PayloadDelegator
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module(includes = [EventModule.Bindings::class])
object EventModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideEventChannel(
        channel: EventDataChannel
    ): EventChannel = channel

    @JvmStatic
    @Provides
    @Singleton
    fun provideEventDataChannel(
        remote: EventRemoteChannel
    ): EventDataChannel = EventDataChannel(remote)

    @JvmStatic
    @Provides
    @Singleton
    fun provideEventRemoteChannel(
        proxy: EventProxy,
        featureToggles: FeatureToggles
    ): EventRemoteChannel = MiddlewareEventChannel(events = proxy, featureToggles = featureToggles)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAccountStatusChannel(
        channel: AccountStatusDataChannel
    ) : AccountStatusChannel = channel

    @JvmStatic
    @Provides
    @Singleton
    fun provideAccountStatusDataChannel(
        channel: AccountStatusRemoteChannel
    ) = AccountStatusDataChannel(
        remote = channel
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideAccountStatusRemoteChannel(
        proxy: EventProxy
    ) : AccountStatusRemoteChannel = AccountStatusMiddlewareChannel(
        events = proxy
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSubscriptionEventChannel(
        channel: SubscriptionDataChannel
    ): SubscriptionEventChannel = channel

    @JvmStatic
    @Provides
    @Singleton
    fun provideSubscriptionEventDataChannel(
        remote: SubscriptionEventRemoteChannel
    ): SubscriptionDataChannel = SubscriptionDataChannel(remote)

    @JvmStatic
    @Provides
    @Singleton
    fun provideSubscriptionEventRemoteChannel(
        proxy: EventProxy,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): SubscriptionEventRemoteChannel = MiddlewareSubscriptionEventChannel(
        events = proxy,
        scope = scope
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileLimitsRemoteChannel(
        proxy: EventProxy
    ): FileLimitsRemoteChannel = FileLimitsMiddlewareChannel(events = proxy)

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileLimitsDataChannel(
        channel: FileLimitsRemoteChannel
    ): FileLimitsEventChannel = FileLimitsDataChannel(channel = channel)

    @JvmStatic
    @Provides
    @Singleton
    fun provideEventHandler(
        logger: MiddlewareProtobufLogger,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        channel: EventHandlerChannel,
        syncP2PStore: SyncAndP2PStatusEventsStore,
        registrar: MiddlewareEventRegistrar
    ): EventProxy = EventHandler(
        scope = scope,
        logger = logger,
        channel = channel,
        syncP2PStore = syncP2PStore,
        registrar = registrar
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideMiddlewareEventRegistrar(): MiddlewareEventRegistrar = DefaultMiddlewareEventRegistrar()

    @JvmStatic
    @Provides
    @Singleton
    fun provideSyncAndP2PStatusEventsSubscription(
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        channel: EventHandlerChannel
    ): SyncAndP2PStatusEventsStore = SyncAndP2PStatusEventsStoreImpl(
        scope = scope,
        channel = channel
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideDefaultEventChannel(): EventHandlerChannel = EventHandlerChannelImpl()

    //region Chats

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatEventChannel(
        channel: ChatEventRemoteChannel.Default
    ): ChatEventChannel = channel

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatEventDataChannel(
        remote: ChatEventRemoteChannel
    ): ChatEventRemoteChannel.Default = ChatEventRemoteChannel.Default(
        channel = remote
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatEventMWChannel(
        proxy: EventProxy
    ): ChatEventRemoteChannel = ChatEventMiddlewareChannel(
        proxy
    )

    //endregion

    @Module
    interface Bindings {

        @Binds
        @Singleton
        fun payloadDelegator(default: PayloadDelegator.Default): PayloadDelegator
    }
}