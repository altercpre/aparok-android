package io.aparok.app.feature_chats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.notifications.NotificationPermissionManager
import io.aparok.app.domain.auth.interactor.GetAccount
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.chats.AddChatMessage
import io.aparok.app.domain.chats.ChatContainer
import io.aparok.app.domain.chats.DeleteChatMessage
import io.aparok.app.domain.chats.EditChatMessage
import io.aparok.app.domain.chats.ToggleChatMessageReaction
import io.aparok.app.domain.invite.GetCurrentInviteAccessLevel
import io.aparok.app.domain.invite.SpaceInviteLinkStore
import io.aparok.app.domain.media.DiscardPreloadedFile
import io.aparok.app.domain.media.PreloadFile
import io.aparok.app.domain.media.UploadFile
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.domain.misc.GetLinkPreview
import io.aparok.app.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.notifications.NotificationBuilder
import io.aparok.app.domain.notifications.SetChatNotificationMode
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.CreateObjectFromUrl
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.spaces.SetSpaceDetails
import io.aparok.app.feature_chats.tools.ClearChatsTempFolder
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.navigation.backstack.BackHistoryDelegate
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.presentation.widgets.PinObjectAsWidgetDelegate
import javax.inject.Inject

class ChatViewModelFactory @Inject constructor(
    private val params: ChatViewModel.Params.Default,
    private val chatContainer: ChatContainer,
    private val addChatMessage: AddChatMessage,
    private val editChatMessage: EditChatMessage,
    private val deleteChatMessage: DeleteChatMessage,
    private val toggleChatMessageReaction: ToggleChatMessageReaction,
    private val members: ActiveSpaceMemberSubscriptionContainer,
    private val getAccount: GetAccount,
    private val urlBuilder: UrlBuilder,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val dispatchers: AppCoroutineDispatchers,
    private val uploadFile: UploadFile,
    private val preloadFile: PreloadFile,
    private val discardPreloadedFile: DiscardPreloadedFile,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val copyFileToCacheDirectory: CopyFileToCacheDirectory,
    private val exitToVaultDelegate: ExitToVaultDelegate,
    private val getLinkPreview: GetLinkPreview,
    private val createObjectFromUrl: CreateObjectFromUrl,
    private val notificationPermissionManager: NotificationPermissionManager,
    private val spacePermissionProvider: UserPermissionProvider,
    private val notificationBuilder: NotificationBuilder,
    private val clearChatsTempFolder: ClearChatsTempFolder,
    private val createObject: CreateObject,
    private val getObject: GetObject,
    private val analytics: Analytics,
    private val spaceInviteLinkStore: SpaceInviteLinkStore,
    private val getCurrentInviteAccessLevel: GetCurrentInviteAccessLevel,
    private val pinObjectAsWidgetDelegate: PinObjectAsWidgetDelegate,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val setObjectDetails: SetObjectDetails,
    private val setSpaceDetails: SetSpaceDetails,
    private val setChatNotificationMode: SetChatNotificationMode,
    private val fieldParser: FieldParser,
    private val deepLinkResolver: DeepLinkResolver,
    private val backHistoryDelegate: BackHistoryDelegate
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ChatViewModel(
        vmParams = params,
        chatContainer = chatContainer,
        addChatMessage = addChatMessage,
        editChatMessage = editChatMessage,
        deleteChatMessage = deleteChatMessage,
        toggleChatMessageReaction = toggleChatMessageReaction,
        members = members,
        getAccount = getAccount,
        urlBuilder = urlBuilder,
        spaceViews = spaceViews,
        dispatchers = dispatchers,
        uploadFile = uploadFile,
        storeOfObjectTypes = storeOfObjectTypes,
        copyFileToCacheDirectory = copyFileToCacheDirectory,
        exitToVaultDelegate = exitToVaultDelegate,
        getLinkPreview = getLinkPreview,
        createObjectFromUrl = createObjectFromUrl,
        notificationPermissionManager = notificationPermissionManager,
        spacePermissionProvider = spacePermissionProvider,
        notificationBuilder = notificationBuilder,
        clearChatsTempFolder = clearChatsTempFolder,
        createObject = createObject,
        getObject = getObject,
        analytics = analytics,
        preloadFile = preloadFile,
        discardPreloadedFile = discardPreloadedFile,
        spaceInviteLinkStore = spaceInviteLinkStore,
        getCurrentInviteAccessLevel = getCurrentInviteAccessLevel,
        pinObjectAsWidgetDelegate = pinObjectAsWidgetDelegate,
        setObjectListIsArchived = setObjectListIsArchived,
        setObjectDetails = setObjectDetails,
        setSpaceDetails = setSpaceDetails,
        setChatNotificationMode = setChatNotificationMode,
        fieldParser = fieldParser,
        deepLinkResolver = deepLinkResolver,
        backHistoryDelegate = backHistoryDelegate
    ) as T
}
