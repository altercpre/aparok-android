package io.aparok.app.di.feature.settings

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.interactor.Logout
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.launch.RemainingSpacesPreloader
import io.aparok.app.domain.misc.AppActionManager
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.ui.settings.LogoutWarningFragment
import io.aparok.app.ui_settings.account.LogoutWarningViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [LogoutWarningModule::class])
@PerScreen
interface LogoutWarningSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: LogoutWarningModule): Builder
        fun build(): LogoutWarningSubComponent
    }

    fun inject(fragment: LogoutWarningFragment)
}

@Module
object LogoutWarningModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewModelFactory(
        logout: Logout,
        analytics: Analytics,
        appActionManager: AppActionManager,
        globalSubscriptionManager: GlobalSubscriptionManager
    ): LogoutWarningViewModel.Factory = LogoutWarningViewModel.Factory(
        logout = logout,
        analytics = analytics,
        appActionManager = appActionManager,
        globalSubscriptionManager = globalSubscriptionManager
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
        config = provider,
        user = user,
        dispatchers = dispatchers,
        spaceManager = spaceManager,
        awaitAccountStartManager = awaitAccountStartManager,
        remainingSpacesPreloader = remainingSpacesPreloader,
        preferredSpaceIdHolder = preferredSpaceIdHolder
    )
}