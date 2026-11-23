package io.aparok.app.di.feature.chats

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.core_utils.notifications.NotificationPermissionManager
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.chats.ChatEventChannel
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.domain.event.interactor.EventChannel
import io.aparok.app.domain.invite.SpaceInviteLinkStore
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.notifications.NotificationBuilder
import io.aparok.app.domain.notifications.ResetSpaceChatNotification
import io.aparok.app.domain.notifications.SetChatNotificationMode
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.spaces.ClearLastOpenedSpace
import io.aparok.app.domain.widgets.CreateWidget
import io.aparok.app.domain.widgets.DeleteWidget
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.feature_chats.presentation.ChatViewModel
import io.aparok.app.feature_chats.presentation.ChatViewModelFactory
import io.aparok.app.middleware.EventProxy
import io.aparok.app.presentation.common.PayloadDelegator
import io.aparok.app.presentation.navigation.backstack.BackHistoryDelegate
import io.aparok.app.presentation.navigation.backstack.NavigationBackStackInspector
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.util.defaultCopyFileToCacheDirectory
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.presentation.widgets.PinObjectAsWidgetDelegate
import io.aparok.app.ui.chats.ChatFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Component(
    dependencies = [ChatComponentDependencies::class],
    modules = [
        ChatContainerModule::class,
        ChatModule::class,
        ChatModule.Declarations::class
    ]
)
@PerScreen
interface ChatComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: ChatViewModel.Params.Default): Builder
        fun withDependencies(dependencies: ChatComponentDependencies): Builder
        fun build(): ChatComponent
    }
    fun inject(fragment: ChatFragment)
}

@Module
object ChatModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyFileToCache(
        context: Context
    ): CopyFileToCacheDirectory = defaultCopyFileToCacheDirectory(context)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideExitToVaultDelegate(
        spaceManager: SpaceManager,
        clearLastOpenedSpace: ClearLastOpenedSpace
    ) : ExitToVaultDelegate = ExitToVaultDelegate.Default(
        spaceManager = spaceManager,
        clearLastOpenedSpace = clearLastOpenedSpace
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun providePinObjectAsWidgetDelegate(
        spaceManager: SpaceManager,
        createWidget: CreateWidget,
        deleteWidget: DeleteWidget,
        showObject: GetObject,
        payloadDelegator: PayloadDelegator
    ) : PinObjectAsWidgetDelegate = PinObjectAsWidgetDelegate.Default(
        spaceManager = spaceManager,
        createWidget = createWidget,
        deleteWidget = deleteWidget,
        showObject = showObject,
        payloadDelegator = payloadDelegator
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideBackHistoryDelegate(
        inspector: NavigationBackStackInspector,
        searchObjects: SearchObjects,
        fieldParser: FieldParser,
        dispatchers: AppCoroutineDispatchers
    ): BackHistoryDelegate = BackHistoryDelegate.Default(
        inspector = inspector,
        searchObjects = searchObjects,
        fieldParser = fieldParser,
        dispatchers = dispatchers
    )

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ChatViewModelFactory
        ): ViewModelProvider.Factory
    }
}

interface ChatComponentDependencies : ComponentDependencies {
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun blockRepository(): BlockRepository
    fun authRepo(): AuthRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun userPermissionProvider(): UserPermissionProvider
    fun eventProxy(): EventProxy
    fun featureToggles(): FeatureToggles
    fun userSettings(): UserSettingsRepository
    fun chatEventChannel(): ChatEventChannel
    fun logger(): Logger
    fun members(): ActiveSpaceMemberSubscriptionContainer
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun context(): Context
    fun spaceManager(): SpaceManager
    fun notificationPermissionManager(): NotificationPermissionManager
    fun storelessSubscriptionContainer(): StorelessSubscriptionContainer
    fun notificationBuilder(): NotificationBuilder
    fun eventChannel(): EventChannel
    fun spaceLinkInfoStore(): SpaceInviteLinkStore
    fun setChatNotificationMode(): SetChatNotificationMode
    fun resetSpaceChatNotification(): ResetSpaceChatNotification
    fun payloadDelegator(): PayloadDelegator
    fun fieldParser(): FieldParser
    fun deepLinkResolver(): DeepLinkResolver
    fun navigationBackStackInspector(): NavigationBackStackInspector
}
