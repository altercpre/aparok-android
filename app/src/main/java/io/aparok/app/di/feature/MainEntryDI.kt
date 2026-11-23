package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.account.AccountStatusChannel
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.account.InterceptAccountStatus
import io.aparok.app.domain.auth.interactor.AppShutdown
import io.aparok.app.domain.auth.interactor.CheckAuthorizationStatus
import io.aparok.app.domain.auth.interactor.HasAccount
import io.aparok.app.domain.auth.interactor.Logout
import io.aparok.app.domain.auth.interactor.ResumeAccount
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.chats.ChatPreviewContainer
import io.aparok.app.domain.chats.ChatsDetailsSubscriptionContainer
import io.aparok.app.domain.launch.RemainingSpacesPreloader
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.ObserveShowSpacesIntroduction
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.DebugRunProfiler
import io.aparok.app.domain.deeplink.PendingIntentStore
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.domain.multiplayer.ParticipantSubscriptionContainer
import io.aparok.app.domain.objects.CrossSpaceObjectTypesContainer
import io.aparok.app.domain.multiplayer.SpaceInviteResolver
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.notifications.SystemNotificationService
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.spaces.ResolveSpaceHomepage
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.theme.GetTheme
import io.aparok.app.domain.vault.SetSpacesIntroductionShown
import io.aparok.app.domain.wallpaper.ObserveSpaceWallpaper
import io.aparok.app.domain.workspace.DeepLinkToObjectDelegate
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.other.DefaultSpaceInviteResolver
import io.aparok.app.presentation.main.MainViewModelFactory
import io.aparok.app.presentation.membership.provider.MembershipProvider
import io.aparok.app.presentation.notifications.NotificationActionDelegate
import io.aparok.app.presentation.notifications.NotificationsProvider
import io.aparok.app.ui.main.MainActivity
import io.aparok.app.ui_settings.appearance.ThemeApplicator
import io.aparok.app.ui_settings.appearance.ThemeApplicatorImpl
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Subcomponent(
    modules = [MainEntryModule::class]
)
@PerScreen
interface MainEntrySubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainEntrySubComponent
        fun module(module: MainEntryModule): Builder
    }

    fun inject(activity: MainActivity)
}

@Module
object MainEntryModule {

    @Provides
    fun provideUnqualifiedScope(
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): CoroutineScope = scope

    @JvmStatic
    @PerScreen
    @Provides
    fun provideMainViewModelFactory(
        resumeAccount: ResumeAccount,
        analytics: Analytics,
        interceptAccountStatus: InterceptAccountStatus,
        logout: Logout,
        checkAuthorizationStatus: CheckAuthorizationStatus,
        hasAccount: HasAccount,
        configStorage: ConfigStorage,
        localeProvider: LocaleProvider,
        notificationsProvider: NotificationsProvider,
        notificator: SystemNotificationService,
        notificationActionDelegate: NotificationActionDelegate,
        deepLinkToObjectDelegate: DeepLinkToObjectDelegate,
        awaitAccountStartManager: AwaitAccountStartManager,
        membershipProvider: MembershipProvider,
        globalSubscriptionManager: GlobalSubscriptionManager,
        spaceInviteResolver: SpaceInviteResolver,
        spaceManager: SpaceManager,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        pendingIntentStore: PendingIntentStore,
        observeSpaceWallpaper: ObserveSpaceWallpaper,
        urlBuilder: UrlBuilder,
        appShutdown: AppShutdown,
        scope: CoroutineScope,
        observeShowSpacesIntroduction: ObserveShowSpacesIntroduction,
        setSpacesIntroductionShown: SetSpacesIntroductionShown,
        appInfo: io.aparok.app.core_utils.tools.AppInfo,
        chatPreviewContainer: ChatPreviewContainer,
        chatsDetailsSubscriptionContainer: ChatsDetailsSubscriptionContainer,
        participantSubscriptionContainer: ParticipantSubscriptionContainer,
        crossSpaceObjectTypesContainer: CrossSpaceObjectTypesContainer,
        userSettingsRepository: UserSettingsRepository,
        resolveSpaceHomepage: ResolveSpaceHomepage,
        debugRunProfiler: DebugRunProfiler,
        preferredSpaceIdHolder: PreferredSpaceIdHolder,
        remainingSpacesPreloader: RemainingSpacesPreloader
    ): MainViewModelFactory = MainViewModelFactory(
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
        spaceViews = spaceViewSubscriptionContainer,
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
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideResumeAccountUseCase(
        authRepository: AuthRepository,
        pathProvider: PathProvider,
        configStorage: ConfigStorage,
        initialParamsProvider: InitialParamsProvider,
        awaitAccountStartManager: AwaitAccountStartManager,
        spaceManager: SpaceManager,
        settingsRepository: UserSettingsRepository
    ): ResumeAccount = ResumeAccount(
        repository = authRepository,
        pathProvider = pathProvider,
        configStorage = configStorage,
        initialParamsProvider = initialParamsProvider,
        awaitAccountStartManager = awaitAccountStartManager,
        spaceManager = spaceManager,
        settings = settingsRepository
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSetupThemeUseCase(
        repo: UserSettingsRepository
    ): GetTheme = GetTheme(repo)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideThemeApplicator(): ThemeApplicator = ThemeApplicatorImpl()

    @JvmStatic
    @PerScreen
    @Provides
    fun provideInterceptAccountStatus(
        channel: AccountStatusChannel,
        dispatchers: AppCoroutineDispatchers
    ): InterceptAccountStatus = InterceptAccountStatus(
        channel = channel,
        dispatchers = dispatchers
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLogoutUseCase(
        repo: AuthRepository,
        provider: ConfigStorage,
        user: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager,
        awaitAccountStartManager: AwaitAccountStartManager,
        remainingSpacesPreloader: RemainingSpacesPreloader,
        preferredSpaceIdHolder: PreferredSpaceIdHolder
    ): Logout = Logout(
        repo = repo,
        user = user,
        config = provider,
        dispatchers = dispatchers,
        spaceManager = spaceManager,
        awaitAccountStartManager = awaitAccountStartManager,
        remainingSpacesPreloader = remainingSpacesPreloader,
        preferredSpaceIdHolder = preferredSpaceIdHolder
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideCheckAuthStatus(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): CheckAuthorizationStatus = CheckAuthorizationStatus(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideHasAccount(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): HasAccount = HasAccount(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideNotificationActionDelegate(
        default: NotificationActionDelegate.Default
    ) : NotificationActionDelegate = default

    @JvmStatic
    @PerScreen
    @Provides
    fun provideDeepLinkToObjectDelegate(
        default: DeepLinkToObjectDelegate.Default
    ): DeepLinkToObjectDelegate = default

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSpaceInviteResolver(): SpaceInviteResolver = DefaultSpaceInviteResolver

    @JvmStatic
    @PerScreen
    @Provides
    fun provideDebugRunProfiler(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): DebugRunProfiler = DebugRunProfiler(repo, dispatchers)
}