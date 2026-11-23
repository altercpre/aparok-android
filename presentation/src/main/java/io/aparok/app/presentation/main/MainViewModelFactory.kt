package io.aparok.app.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.tools.AppInfo
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.account.InterceptAccountStatus
import io.aparok.app.domain.auth.interactor.AppShutdown
import io.aparok.app.domain.auth.interactor.CheckAuthorizationStatus
import io.aparok.app.domain.auth.interactor.HasAccount
import io.aparok.app.domain.auth.interactor.Logout
import io.aparok.app.domain.auth.interactor.ResumeAccount
import io.aparok.app.domain.chats.ChatPreviewContainer
import io.aparok.app.domain.chats.ChatsDetailsSubscriptionContainer
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.ObserveShowSpacesIntroduction
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.DebugRunProfiler
import io.aparok.app.domain.deeplink.PendingIntentStore
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.launch.RemainingSpacesPreloader
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.domain.multiplayer.ParticipantSubscriptionContainer
import io.aparok.app.domain.objects.CrossSpaceObjectTypesContainer
import io.aparok.app.domain.multiplayer.SpaceInviteResolver
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.notifications.SystemNotificationService
import io.aparok.app.domain.spaces.ResolveSpaceHomepage
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.vault.SetSpacesIntroductionShown
import io.aparok.app.domain.wallpaper.ObserveSpaceWallpaper
import io.aparok.app.domain.workspace.DeepLinkToObjectDelegate
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.membership.provider.MembershipProvider
import io.aparok.app.presentation.notifications.NotificationActionDelegate
import io.aparok.app.presentation.notifications.NotificationsProvider
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class MainViewModelFactory @Inject constructor(
    private val resumeAccount: ResumeAccount,
    private val analytics: Analytics,
    private val interceptAccountStatus: InterceptAccountStatus,
    private val logout: Logout,
    private val checkAuthorizationStatus: CheckAuthorizationStatus,
    private val hasAccount: HasAccount,
    private val configStorage: ConfigStorage,
    private val localeProvider: LocaleProvider,
    private val notificationsProvider: NotificationsProvider,
    private val notificator: SystemNotificationService,
    private val notificationActionDelegate: NotificationActionDelegate,
    private val deepLinkToObjectDelegate: DeepLinkToObjectDelegate,
    private val awaitAccountStartManager: AwaitAccountStartManager,
    private val membershipProvider: MembershipProvider,
    private val globalSubscriptionManager: GlobalSubscriptionManager,
    private val spaceInviteResolver: SpaceInviteResolver,
    private val spaceManager: SpaceManager,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val pendingIntentStore: PendingIntentStore,
    private val observeSpaceWallpaper: ObserveSpaceWallpaper,
    private val urlBuilder: UrlBuilder,
    private val appShutdown: AppShutdown,
    private val scope: CoroutineScope,
    private val observeShowSpacesIntroduction: ObserveShowSpacesIntroduction,
    private val setSpacesIntroductionShown: SetSpacesIntroductionShown,
    private val appInfo: AppInfo,
    private val chatPreviewContainer: ChatPreviewContainer,
    private val chatsDetailsSubscriptionContainer: ChatsDetailsSubscriptionContainer,
    private val participantSubscriptionContainer: ParticipantSubscriptionContainer,
    private val crossSpaceObjectTypesContainer: CrossSpaceObjectTypesContainer,
    private val userSettingsRepository: UserSettingsRepository,
    private val resolveSpaceHomepage: ResolveSpaceHomepage,
    private val debugRunProfiler: DebugRunProfiler,
    private val preferredSpaceIdHolder: PreferredSpaceIdHolder,
    private val remainingSpacesPreloader: RemainingSpacesPreloader
    ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T = MainViewModel(
        resumeAccount = resumeAccount,
        analytics = analytics,
        interceptAccountStatus = interceptAccountStatus,
        logout = logout,
        checkAuthorizationStatus = checkAuthorizationStatus,
        hasAccount = hasAccount,
        configStorage = configStorage,
        localeProvider = localeProvider,
        notificationsProvider = notificationsProvider,
        notificator = notificator,
        notificationActionDelegate = notificationActionDelegate,
        deepLinkToObjectDelegate = deepLinkToObjectDelegate,
        awaitAccountStartManager = awaitAccountStartManager,
        membershipProvider = membershipProvider,
        globalSubscriptionManager = globalSubscriptionManager,
        spaceInviteResolver = spaceInviteResolver,
        spaceManager = spaceManager,
        spaceViews = spaceViews,
        pendingIntentStore = pendingIntentStore,
        observeSpaceWallpaper = observeSpaceWallpaper,
        urlBuilder = urlBuilder,
        appShutdown = appShutdown,
        scope = scope,
        observeShowSpacesIntroduction = observeShowSpacesIntroduction,
        setSpacesIntroductionShown = setSpacesIntroductionShown,
        appInfo = appInfo,
        chatPreviewContainer = chatPreviewContainer,
        chatsDetailsSubscriptionContainer = chatsDetailsSubscriptionContainer,
        participantSubscriptionContainer = participantSubscriptionContainer,
        crossSpaceObjectTypesContainer = crossSpaceObjectTypesContainer,
        userSettingsRepository = userSettingsRepository,
        resolveSpaceHomepage = resolveSpaceHomepage,
        debugRunProfiler = debugRunProfiler,
        preferredSpaceIdHolder = preferredSpaceIdHolder,
        remainingSpacesPreloader = remainingSpacesPreloader
    ) as T
}