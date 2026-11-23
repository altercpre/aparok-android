package io.aparok.app.di.feature.settings

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.core_utils.notifications.NotificationPermissionManager
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.account.DeleteAccount
import io.aparok.app.domain.auth.interactor.Logout
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.launch.RemainingSpacesPreloader
import io.aparok.app.domain.debugging.DebugSpace
import io.aparok.app.domain.icon.RemoveObjectIcon
import io.aparok.app.domain.icon.SetDocumentImageIcon
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.multiplayer.GenerateOneToOneChatLink
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.networkmode.GetNetworkMode
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.search.ProfileSubscriptionManager
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.spaces.SpaceGradientProvider
import io.aparok.app.ui.settings.ProfileSettingsFragment
import io.aparok.app.ui_settings.account.ProfileSettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
@PerScreen
interface ProfileSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: ProfileModule): Builder
        fun build(): ProfileSubComponent
    }

    fun inject(fragment: ProfileSettingsFragment)
}

@Module(includes = [ProfileModule.Bindings::class])
object ProfileModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewModelFactory(
        analytics: Analytics,
        storelessSubscriptionContainer: StorelessSubscriptionContainer,
        setObjectDetails: SetObjectDetails,
        configStorage: ConfigStorage,
        urlBuilder: UrlBuilder,
        setDocumentImageIcon: SetDocumentImageIcon,
        getNetworkMode: GetNetworkMode,
        profileSubscriptionManager: ProfileSubscriptionManager,
        removeObjectIcon: RemoveObjectIcon,
        notificationPermissionManager: NotificationPermissionManager,
        userPermissionProvider: UserPermissionProvider,
        generateOneToOneChatLink: GenerateOneToOneChatLink,
        userSettingsRepository: UserSettingsRepository
    ): ProfileSettingsViewModel.Factory = ProfileSettingsViewModel.Factory(
        analytics = analytics,
        container = storelessSubscriptionContainer,
        setObjectDetails = setObjectDetails,
        configStorage = configStorage,
        urlBuilder = urlBuilder,
        setDocumentImageIcon = setDocumentImageIcon,
        getNetworkMode = getNetworkMode,
        profileSubscriptionManager = profileSubscriptionManager,
        removeObjectIcon = removeObjectIcon,
        notificationPermissionManager = notificationPermissionManager,
        userPermissionProvider = userPermissionProvider,
        generateOneToOneChatLink = generateOneToOneChatLink,
        userSettingsRepository = userSettingsRepository
    )

    @Provides
    @PerScreen
    fun provideSpaceGradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @Provides
    @PerScreen
    fun provideDebugSync(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager
    ): DebugSpace = DebugSpace(
        repo = repo,
        dispatchers = dispatchers,
        spaceManager = spaceManager
    )

    @Provides
    @PerScreen
    fun provideRemoveObjectIcon(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RemoveObjectIcon = RemoveObjectIcon(repo, dispatchers)

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
        config = provider,
        user = user,
        dispatchers = dispatchers,
        spaceManager = spaceManager,
        awaitAccountStartManager = awaitAccountStartManager,
        remainingSpacesPreloader = remainingSpacesPreloader,
        preferredSpaceIdHolder = preferredSpaceIdHolder
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun deleteAccount(repo: AuthRepository): DeleteAccount = DeleteAccount(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo,
        dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocumentImageIcon(
        repo: BlockRepository
    ): SetDocumentImageIcon = SetDocumentImageIcon(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGenerateOneToOneChatLink(
        dispatchers: AppCoroutineDispatchers
    ): GenerateOneToOneChatLink = GenerateOneToOneChatLink(
        dispatchers = dispatchers
    )

    @Module
    interface Bindings {
        // Add bindings if needed
    }
}