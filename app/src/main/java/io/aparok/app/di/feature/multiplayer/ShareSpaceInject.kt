package io.aparok.app.di.feature.multiplayer

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.clipboard.Clipboard
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.TechSpaceProvider
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.invite.SpaceInviteLinkStore
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.ProfileSubscriptionManager
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.membership.provider.MembershipProvider
import io.aparok.app.presentation.multiplayer.ShareSpaceViewModel
import io.aparok.app.presentation.spaces.SpaceGradientProvider
import io.aparok.app.ui.multiplayer.ShareSpaceFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [ShareSpaceDependencies::class],
    modules = [
        ShareSpaceModule::class,
        ShareSpaceModule.Declarations::class
    ]
)
@PerDialog
interface ShareSpaceComponent {
    @Component.Builder
    interface Builder {
        fun withDependencies(dependencies: ShareSpaceDependencies): Builder
        @BindsInstance
        fun withParams(params: ShareSpaceViewModel.VmParams): Builder
        fun build(): ShareSpaceComponent
    }

    fun inject(fragment: ShareSpaceFragment)
}

@Module
object ShareSpaceModule {

    @PerDialog
    @Provides
    fun gradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: ShareSpaceViewModel.Factory): ViewModelProvider.Factory
    }
}

interface ShareSpaceDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun auth() : AuthRepository
    fun urlBuilder(): UrlBuilder
    fun spaceManager(): SpaceManager
    fun dispatchers(): AppCoroutineDispatchers
    fun container(): StorelessSubscriptionContainer
    fun config(): ConfigStorage
    fun techSpaceProvider(): TechSpaceProvider
    fun permissions(): UserPermissionProvider
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun provideMembershipProvider(): MembershipProvider
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun clipboard(): Clipboard
    fun logger(): Logger
    fun spaceInviteLinkStore(): SpaceInviteLinkStore
    fun stringResourceProvider(): StringResourceProvider
    fun profileContainer(): ProfileSubscriptionManager
}