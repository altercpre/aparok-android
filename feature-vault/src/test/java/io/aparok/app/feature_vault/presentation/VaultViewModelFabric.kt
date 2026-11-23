package io.aparok.app.feature_vault.presentation

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.notifications.NotificationPermissionManager
import io.aparok.app.core_utils.tools.AppInfo
import io.aparok.app.domain.chats.ChatPreviewContainer
import io.aparok.app.domain.chats.ChatsDetailsSubscriptionContainer
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.deeplink.PendingIntentStore
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
import io.aparok.app.domain.spaces.CreateSpace
import io.aparok.app.domain.spaces.DeleteSpace
import io.aparok.app.domain.spaces.ResolveSpaceHomepage
import io.aparok.app.domain.spaces.SetHomepage
import io.aparok.app.domain.spaces.SaveCurrentSpace
import io.aparok.app.domain.vault.SetCreateSpaceBadgeSeen
import io.aparok.app.domain.vault.SetSpaceOrder
import io.aparok.app.domain.vault.SetVaultSearchHighlightSeen
import io.aparok.app.domain.vault.ShouldShowCreateSpaceBadge
import io.aparok.app.domain.vault.UnpinSpace
import io.aparok.app.domain.wallpaper.GetSpaceWallpapers
import io.aparok.app.domain.network.NetworkModeProvider
import io.aparok.app.domain.payments.GetMembershipFeatures
import io.aparok.app.domain.widgets.OsWidgetDataViewSync
import io.aparok.app.domain.widgets.OsWidgetSpacesSync
import io.aparok.app.domain.workspace.DeepLinkToObjectDelegate
import io.aparok.app.domain.workspace.SpaceManager
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

object VaultViewModelFabric {
    fun create(
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer = mock(),
        urlBuilder: UrlBuilder = mock(),
        spaceManager: SpaceManager = mock(),
        saveCurrentSpace: SaveCurrentSpace = mock(),
        analytics: Analytics = mock(),
        deepLinkToObjectDelegate: DeepLinkToObjectDelegate = mock(),
        appActionManager: AppActionManager = mock(),
        spaceInviteResolver: SpaceInviteResolver = mock(),
        profileContainer: ProfileSubscriptionManager = mock {
            on { observe() }.thenReturn(flowOf(ObjectWrapper.Basic(emptyMap())))
        },
        chatPreviewContainer: ChatPreviewContainer = mock(),
        chatsDetailsContainer: ChatsDetailsSubscriptionContainer = mock {
            on { observe() }.thenReturn(flowOf(emptyList()))
        },
        pendingIntentStore: PendingIntentStore = mock(),
        stringResourceProvider: StringResourceProvider = mock(),
        dateProvider: DateProvider = mock(),
        fieldParser: FieldParser = mock(),
        storeOfObjectTypes: StoreOfObjectTypes = mock(),
        setSpaceNotificationMode: SetSpaceNotificationMode = mock(),
        deleteSpace: DeleteSpace = mock(),
        userPermissionProvider: UserPermissionProvider = mock(),
        notificationPermissionManager: NotificationPermissionManager = mock(),
        unpinSpace: UnpinSpace = mock(),
        setSpaceOrder: SetSpaceOrder = mock(),
        getSpaceWallpaper: GetSpaceWallpapers = mock(),
        searchOneToOneChatByIdentity: SearchOneToOneChatByIdentity = mock(),
        shouldShowCreateSpaceBadge: ShouldShowCreateSpaceBadge = mock {
            on { runBlocking { async(any()) } }.thenReturn(io.aparok.app.domain.base.Resultat.Success(false))
        },
        setCreateSpaceBadgeSeen: SetCreateSpaceBadgeSeen = mock(),
        setVaultSearchHighlightSeen: SetVaultSearchHighlightSeen = mock(),
        appInfo: AppInfo = mock {
            on { versionName }.thenReturn("1.0.0-test")
        },
        participantSubscriptionContainer: ParticipantSubscriptionContainer = mock(),
        configStorage: ConfigStorage = mock {
            on { getOrNull() }.thenReturn(null)
            on { getAccountId() }.thenReturn(null)
        },
        createSpace: CreateSpace = mock(),
        deepLinkResolver: DeepLinkResolver = mock(),
        osWidgetSpacesSync: OsWidgetSpacesSync = mock(),
        osWidgetDataViewSync: OsWidgetDataViewSync = mock(),
        networkModeProvider: NetworkModeProvider = mock(),
        getMembershipFeatures: GetMembershipFeatures = mock(),
        resolveSpaceHomepage: ResolveSpaceHomepage = mock(),
        setHomepage: SetHomepage = mock(),
        userSettingsRepository: UserSettingsRepository = mock {
            on { observeCompactModeEnabled() }.thenReturn(flowOf(false))
            on { observeQuickCaptureEnabled() }.thenReturn(flowOf(false))
            onBlocking { getVaultSortKeys() }.thenReturn(emptyMap())
        }
    ): VaultViewModel = VaultViewModel(
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
        urlBuilder = urlBuilder,
        spaceManager = spaceManager,
        saveCurrentSpace = saveCurrentSpace,
        analytics = analytics,
        deepLinkToObjectDelegate = deepLinkToObjectDelegate,
        appActionManager = appActionManager,
        spaceInviteResolver = spaceInviteResolver,
        profileContainer = profileContainer,
        chatPreviewContainer = chatPreviewContainer,
        chatsDetailsContainer = chatsDetailsContainer,
        pendingIntentStore = pendingIntentStore,
        stringResourceProvider = stringResourceProvider,
        dateProvider = dateProvider,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes,
        setSpaceNotificationMode = setSpaceNotificationMode,
        deleteSpace = deleteSpace,
        userPermissionProvider = userPermissionProvider,
        notificationPermissionManager = notificationPermissionManager,
        unpinSpace = unpinSpace,
        setSpaceOrder = setSpaceOrder,
        getSpaceWallpapers = getSpaceWallpaper,
        shouldShowCreateSpaceBadge = shouldShowCreateSpaceBadge,
        setCreateSpaceBadgeSeen = setCreateSpaceBadgeSeen,
        setVaultSearchHighlightSeen = setVaultSearchHighlightSeen,
        appInfo = appInfo,
        participantContainer = participantSubscriptionContainer,
        searchOneToOneChatByIdentity = searchOneToOneChatByIdentity,
        createSpace = createSpace,
        deepLinkResolver = deepLinkResolver,
        configStorage = configStorage,
        osWidgetSpacesSync = osWidgetSpacesSync,
        osWidgetDataViewSync = osWidgetDataViewSync,
        networkModeProvider = networkModeProvider,
        getMembershipFeatures = getMembershipFeatures,
        resolveSpaceHomepage = resolveSpaceHomepage,
        setHomepage = setHomepage,
        userSettingsRepository = userSettingsRepository
    )
}
