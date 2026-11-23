package io.aparok.app.di.feature.auth

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.account.DateHelper
import io.aparok.app.domain.account.RestoreAccount
import io.aparok.app.domain.auth.interactor.Logout
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.launch.RemainingSpacesPreloader
import io.aparok.app.domain.misc.AppActionManager
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.ext.DefaultDateHelper
import io.aparok.app.presentation.auth.account.DeletedAccountViewModel
import io.aparok.app.ui.auth.account.DeletedAccountFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [DeletedAccountDependencies::class],
    modules = [
        DeletedAccountModule::class,
        DeletedAccountModule.Declarations::class
    ]
)
@PerScreen
interface DeletedAccountComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: DeletedAccountDependencies): DeletedAccountComponent
    }

    fun inject(fragment: DeletedAccountFragment)
}

@Module
object DeletedAccountModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRestoreAccount(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): RestoreAccount = RestoreAccount(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideLogoutUseCase(
        repo: AuthRepository,
        provider: ConfigStorage,
        dispatchers: AppCoroutineDispatchers,
        user: UserSettingsRepository,
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
    fun provideDateHelper(): DateHelper = DefaultDateHelper()

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: DeletedAccountViewModel.Factory
        ): ViewModelProvider.Factory

    }
}

interface DeletedAccountDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun appActionManager(): AppActionManager
    fun dispatchers(): AppCoroutineDispatchers
    fun configStorage(): ConfigStorage
    fun authRepository(): AuthRepository
    fun userSettingsRepository(): UserSettingsRepository
    fun spaceManager(): SpaceManager
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun userPermissionProvider(): UserPermissionProvider
    fun globalSubscriptionManager(): GlobalSubscriptionManager
    fun remainingSpacesPreloader(): RemainingSpacesPreloader
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}