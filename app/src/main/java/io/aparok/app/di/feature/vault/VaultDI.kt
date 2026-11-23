package io.aparok.app.di.feature.vault

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.core_utils.notifications.NotificationPermissionManager
import io.aparok.app.core_utils.tools.AppInfo
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.chats.ChatEventChannel
import io.aparok.app.domain.chats.ChatPreviewContainer
import io.aparok.app.domain.chats.ChatsDetailsSubscriptionContainer
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.deeplink.PendingIntentStore
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.AppActionManager
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.domain.multiplayer.ParticipantSubscriptionContainer
import io.aparok.app.domain.multiplayer.SearchOneToOneChatByIdentity
import io.aparok.app.domain.multiplayer.SpaceInviteResolver
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.notifications.SetSpaceNotificationMode
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.ProfileSubscriptionManager
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.spaces.CreateSpace
import io.aparok.app.domain.wallpaper.GetSpaceWallpapers
import io.aparok.app.domain.network.NetworkModeProvider
import io.aparok.app.domain.payments.GetMembershipFeatures
import io.aparok.app.domain.widgets.OsWidgetDataViewSync
import io.aparok.app.domain.widgets.OsWidgetSpacesSync
import io.aparok.app.domain.workspace.DeepLinkToObjectDelegate
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.feature_vault.presentation.VaultViewModelFactory
import io.aparok.app.other.DefaultSpaceInviteResolver
import io.aparok.app.ui.vault.VaultFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Component(
    dependencies = [VaultComponentDependencies::class],
    modules = [
        VaultModule::class,
        VaultModule.Declarations::class
    ]
)
@PerScreen
interface VaultComponent {

    fun inject(fragment: VaultFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: VaultComponentDependencies): VaultComponent
    }
}

@Module
object VaultModule {
    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: VaultViewModelFactory
        ): ViewModelProvider.Factory

        @PerScreen
        @Binds
        fun deepLinkToObjectDelegate(
            default: DeepLinkToObjectDelegate.Default
        ): DeepLinkToObjectDelegate
    }

    @PerScreen
    @Provides
    fun provideSpaceInviteResolver(): SpaceInviteResolver = DefaultSpaceInviteResolver

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetSpaceNotificationModeUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetSpaceNotificationMode = SetSpaceNotificationMode(repository, dispatchers)
    
    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetSpaceWallpapers(
        userSettingsRepository: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetSpaceWallpapers = GetSpaceWallpapers(userSettingsRepository, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchOneToOneChatByIdentity(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SearchOneToOneChatByIdentity = SearchOneToOneChatByIdentity(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateSpace(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateSpace = CreateSpace(repo = repository, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetMembershipFeatures(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetMembershipFeatures = GetMembershipFeatures(repo = repository, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchObjects(
        repository: BlockRepository
    ): SearchObjects = SearchObjects(repo = repository)
}

interface VaultComponentDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun userSettingsRepository(): UserSettingsRepository
    fun spaceManager(): SpaceManager
    fun userPermissionProvider(): UserPermissionProvider
    fun auth(): AuthRepository
    fun appActionManager(): AppActionManager
    fun logger(): Logger
    fun awaitAccount(): AwaitAccountStartManager
    fun profileContainer(): ProfileSubscriptionManager
    fun pendingIntentStore(): PendingIntentStore
    fun stringResourceProvider(): StringResourceProvider
    fun dateProvider(): DateProvider
    fun fieldParser(): FieldParser
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun notificationPermissionManager(): NotificationPermissionManager
    fun provideChatEventChannel(): ChatEventChannel
    fun provideStorelessSubscriptionContainer(): StorelessSubscriptionContainer
    fun provideVaultChatPreviewContainer(): ChatPreviewContainer
    fun appInfo(): AppInfo
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun chatSubscriptionContainer(): ChatsDetailsSubscriptionContainer
    fun participantSubscriptionContainer(): ParticipantSubscriptionContainer
    fun deepLinkResolver(): DeepLinkResolver
    fun configStorage(): ConfigStorage
    fun osWidgetSpacesSync(): OsWidgetSpacesSync
    fun osWidgetDataViewSync(): OsWidgetDataViewSync
    fun networkModeProvider(): NetworkModeProvider
}
