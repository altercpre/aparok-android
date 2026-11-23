package io.aparok.app.di.feature.participant

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.icon.SetDocumentImageIcon
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.RemoveSpaceMembers
import io.aparok.app.domain.multiplayer.SearchOneToOneChatByIdentity
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.membership.provider.MembershipProvider
import io.aparok.app.presentation.profile.ParticipantViewModel
import io.aparok.app.ui.profile.ParticipantFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [ParticipantComponentDependencies::class],
    modules = [
        ParticipantModule::class,
        ParticipantModule.Declarations::class
    ]
)
@PerScreen
interface ParticipantComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: ParticipantViewModel.VmParams,
            dependencies: ParticipantComponentDependencies
        ): ParticipantComponent
    }

    fun inject(fragment: ParticipantFragment)

}

@Module
object ParticipantModule {

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
    fun provideSetDocumentImageIconUseCase(
        repo: BlockRepository
    ): SetDocumentImageIcon = SetDocumentImageIcon(repo)

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
    fun provideSearchOneToOneChatByIdentity(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SearchOneToOneChatByIdentity = SearchOneToOneChatByIdentity(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRemoveSpaceMembers(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RemoveSpaceMembers = RemoveSpaceMembers(
        repo = repo,
        dispatchers = dispatchers
    )

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ParticipantViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface ParticipantComponentDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
    fun logger(): Logger
    fun localeProvider(): LocaleProvider
    fun userPermissionProvider(): UserPermissionProvider
    fun fieldsProvider(): FieldParser
    fun provideMembershipProvider(): MembershipProvider
    fun subEventChannel(): SubscriptionEventChannel
    fun provideConfigStorage(): ConfigStorage
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun spaceManager(): SpaceManager
}