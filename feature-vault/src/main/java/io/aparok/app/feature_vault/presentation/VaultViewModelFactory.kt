package io.aparok.app.feature_vault.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
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
import io.aparok.app.domain.spaces.SaveCurrentSpace
import io.aparok.app.domain.spaces.SetHomepage
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
import javax.inject.Inject

class VaultViewModelFactory @Inject constructor(
    private val spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
    private val urlBuilder: UrlBuilder,
    private val spaceManager: SpaceManager,
    private val saveCurrentSpace: SaveCurrentSpace,
    private val analytics: Analytics,
    private val deepLinkToObjectDelegate: DeepLinkToObjectDelegate,
    private val appActionManager: AppActionManager,
    private val spaceInviteResolver: SpaceInviteResolver,
    private val profileContainer: ProfileSubscriptionManager,
    private val chatPreviewContainer: ChatPreviewContainer,
    private val chatsDetailsContainer: ChatsDetailsSubscriptionContainer,
    private val participantContainer: ParticipantSubscriptionContainer,
    private val pendingIntentStore: PendingIntentStore,
    private val stringResourceProvider: StringResourceProvider,
    private val dateProvider: DateProvider,
    private val fieldParser: FieldParser,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val setSpaceNotificationMode: SetSpaceNotificationMode,
    private val deleteSpace: DeleteSpace,
    private val userPermissionProvider: UserPermissionProvider,
    private val notificationPermissionManager: NotificationPermissionManager,
    private val unpinSpace: UnpinSpace,
    private val setSpaceOrder: SetSpaceOrder,
    private val getSpaceWallpapers: GetSpaceWallpapers,
    private val shouldShowCreateSpaceBadge: ShouldShowCreateSpaceBadge,
    private val setCreateSpaceBadgeSeen: SetCreateSpaceBadgeSeen,
    private val setVaultSearchHighlightSeen: SetVaultSearchHighlightSeen,
    private val appInfo: AppInfo,
    private val searchOneToOneChatByIdentity: SearchOneToOneChatByIdentity,
    private val createSpace: CreateSpace,
    private val deepLinkResolver: DeepLinkResolver,
    private val configStorage: ConfigStorage,
    private val osWidgetSpacesSync: OsWidgetSpacesSync,
    private val osWidgetDataViewSync: OsWidgetDataViewSync,
    private val networkModeProvider: NetworkModeProvider,
    private val getMembershipFeatures: GetMembershipFeatures,
    private val resolveSpaceHomepage: ResolveSpaceHomepage,
    private val setHomepage: SetHomepage,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = VaultViewModel(
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
        participantContainer = participantContainer,
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
        getSpaceWallpapers = getSpaceWallpapers,
        shouldShowCreateSpaceBadge = shouldShowCreateSpaceBadge,
        setCreateSpaceBadgeSeen = setCreateSpaceBadgeSeen,
        setVaultSearchHighlightSeen = setVaultSearchHighlightSeen,
        appInfo = appInfo,
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
    ) as T
}
