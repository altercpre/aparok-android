package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.all_content.RestoreAllContentState
import io.aparok.app.domain.all_content.UpdateAllContentState
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.workspace.RemoveObjectsFromWorkspace
import io.aparok.app.feature_allcontent.presentation.AllContentViewModel
import io.aparok.app.feature_allcontent.presentation.AllContentViewModelFactory
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.ui.allcontent.AllContentFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [AllContentDependencies::class],
    modules = [
        AllContentModule::class,
        AllContentModule.Declarations::class
    ]
)
@PerScreen
interface AllContentComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: AllContentViewModel.VmParams,
            dependencies: AllContentDependencies
        ): AllContentComponent
    }

    fun inject(fragment: AllContentFragment)
}

@Module
object AllContentModule {

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
    fun provideUpdateAllContentState(
        dispatchers: AppCoroutineDispatchers,
        userSettingsRepository: UserSettingsRepository
    ): UpdateAllContentState = UpdateAllContentState(
        dispatchers = dispatchers,
        settings = userSettingsRepository
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRestoreAllContentState(
        dispatchers: AppCoroutineDispatchers,
        userSettingsRepository: UserSettingsRepository
    ): RestoreAllContentState = RestoreAllContentState(
        dispatchers = dispatchers,
        settings = userSettingsRepository
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun createObject(
        repo: BlockRepository,
        getDefaultObjectType: GetDefaultObjectType,
        dispatchers: AppCoroutineDispatchers
    ): CreateObject = CreateObject(
        repo = repo,
        getDefaultObjectType = getDefaultObjectType,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @Provides
    @PerScreen
    @JvmStatic
    fun removeObjectFromWorkspace(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RemoveObjectsFromWorkspace = RemoveObjectsFromWorkspace(repo, dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: AllContentViewModelFactory
        ): ViewModelProvider.Factory

    }
}

interface AllContentDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
    fun subEventChannel(): SubscriptionEventChannel
    fun logger(): Logger
    fun localeProvider(): LocaleProvider
    fun userPermissionProvider(): UserPermissionProvider
    fun searchObjects(): SearchObjects
    fun fieldsProvider(): FieldParser
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun provideSpaceSyncAndP2PStatusProvider(): SpaceSyncAndP2PStatusProvider
}