package io.aparok.app.di.main

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import io.aparok.app.app.AnytypeNotificationService
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.notifications.NotificationPermissionManager
import io.aparok.app.core_utils.notifications.NotificationPermissionManagerImpl
import io.aparok.app.data.auth.event.NotificationsDateChannel
import io.aparok.app.data.auth.event.NotificationsRemoteChannel
import io.aparok.app.device.NotificationBuilderImpl
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.chats.ChatsDetailsSubscriptionContainer
import io.aparok.app.domain.chats.PushKeyChannel
import io.aparok.app.domain.chats.PushKeySpaceViewChannel
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.notifications.NotificationBuilder
import io.aparok.app.domain.notifications.PushKeyProvider
import io.aparok.app.domain.notifications.SystemNotificationService
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.workspace.NotificationsChannel
import io.aparok.app.middleware.EventProxy
import io.aparok.app.middleware.interactor.NotificationsMiddlewareChannel
import io.aparok.app.presentation.notifications.NotificationsProvider
import io.aparok.app.presentation.notifications.PushKeyProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

@Module
object NotificationsModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificationsRemoteChannel(
        proxy: EventProxy
    ): NotificationsRemoteChannel = NotificationsMiddlewareChannel(
        eventsProxy = proxy
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificationsChannel(
        channel: NotificationsRemoteChannel
    ): NotificationsChannel = NotificationsDateChannel(
        channel = channel
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificator(
        context: Context
    ): SystemNotificationService = AnytypeNotificationService(context = context)

    @JvmStatic
    @Singleton
    @Provides
    fun provideNotificationsProvider(
        dispatchers: AppCoroutineDispatchers,
        @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        awaitAccountStartManager: AwaitAccountStartManager,
        notificationsChannel: NotificationsChannel
    ): NotificationsProvider = NotificationsProvider.Default(
        dispatchers = dispatchers,
        scope = scope,
        notificationsChannel = notificationsChannel,
        awaitAccountStartManager = awaitAccountStartManager,
    )

    @JvmStatic
    @Provides
    @Singleton
    fun providePushKeyProvider(
        @Named("encrypted") sharedPreferences: SharedPreferences,
        dispatchers: AppCoroutineDispatchers,
        @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        channel: PushKeyChannel,
        json: Json
    ): PushKeyProvider = PushKeyProviderImpl(
        sharedPreferences = sharedPreferences,
        dispatchers = dispatchers,
        scope = scope,
        channel = channel,
        json = json
    )

    @JvmStatic
    @Provides
    @Singleton
    fun providePushKeyChannel(
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
    ): PushKeyChannel = PushKeySpaceViewChannel(spaceViewSubscriptionContainer)

    @Provides
    @Singleton
    @JvmStatic
    fun provideNotificationPermissionManager(
        @Named("default") prefs: SharedPreferences,
        context: Context
    ): NotificationPermissionManager {
        return NotificationPermissionManagerImpl(
            sharedPreferences = prefs,
            context = context
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificationBuilder(
        context: Context,
        notificationManager: NotificationManager,
        stringResourceProvider: StringResourceProvider,
        urlBuilder: UrlBuilder,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        chatsDetailsSubscriptionContainer: ChatsDetailsSubscriptionContainer
    ): NotificationBuilder = NotificationBuilderImpl(
        context = context,
        notificationManager = notificationManager,
        resourceProvider = stringResourceProvider,
        urlBuilder = urlBuilder,
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
        chatsDetailsSubscriptionContainer = chatsDetailsSubscriptionContainer
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificationManager(
        context: Context
    ): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}