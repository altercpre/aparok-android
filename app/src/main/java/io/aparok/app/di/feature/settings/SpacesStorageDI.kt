package io.aparok.app.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.interactor.GetAccount
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.workspace.FileLimitsEventChannel
import io.aparok.app.domain.workspace.SpacesUsageInfo
import io.aparok.app.domain.workspace.InterceptFileLimitEvents
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.membership.provider.MembershipProvider
import io.aparok.app.presentation.settings.SpacesStorageViewModel
import io.aparok.app.presentation.settings.SpacesStorageViewModelFactory
import io.aparok.app.ui.settings.SpacesStorageFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@PerScreen
@Component(
    dependencies = [SpacesStorageDependencies::class],
    modules = [
        SpacesStorageModule::class,
        SpacesStorageModule.Declarations::class
    ]
)
interface SpacesStorageComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: SpacesStorageViewModel.VmParams,
            dependency: SpacesStorageDependencies
        ): SpacesStorageComponent
    }

    fun inject(fragment: SpacesStorageFragment)
}

@Module
object SpacesStorageModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideStoreLessSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): StorelessSubscriptionContainer = StorelessSubscriptionContainer.Impl(
        repo = repo,
        channel = channel,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSpaceUsage(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SpacesUsageInfo = SpacesUsageInfo(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileLimitEvents(
        channel: FileLimitsEventChannel,
        dispatchers: AppCoroutineDispatchers
    ) : InterceptFileLimitEvents = InterceptFileLimitEvents(channel, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetAccountUseCase(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetAccount = GetAccount(repo = repo, dispatcher = dispatchers)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: SpacesStorageViewModelFactory): ViewModelProvider.Factory
    }
}

interface SpacesStorageDependencies : ComponentDependencies {
    fun blockRepo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun configStorage(): ConfigStorage
    fun channel(): SubscriptionEventChannel
    fun fileEventsChannel(): FileLimitsEventChannel
    fun authRepo(): AuthRepository
    fun logger(): Logger
    fun spaceManager(): SpaceManager
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun membershipProvider(): MembershipProvider
    fun userPermissionProvider(): UserPermissionProvider
}