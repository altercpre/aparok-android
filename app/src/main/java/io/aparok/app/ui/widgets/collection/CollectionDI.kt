package io.aparok.app.ui.widgets.collection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.interactor.Move
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.dashboard.interactor.SetObjectListIsFavorite
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.event.interactor.EventChannel
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.misc.DateTypeNameProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.OpenObject
import io.aparok.app.domain.objects.DeleteObjects
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.ObjectSearchSubscriptionContainer
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.templates.GetTemplates
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.widgets.WidgetDispatchEvent
import io.aparok.app.presentation.widgets.collection.ResourceProvider
import io.aparok.app.presentation.widgets.collection.CollectionViewModel
import io.aparok.app.ui.settings.RemoteFilesManageFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Component(
    dependencies = [CollectionDependencies::class],
    modules = [CollectionModule::class]
)
@PerScreen
interface CollectionComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: CollectionViewModel.VmParams,
            dependencies: CollectionDependencies
        ): CollectionComponent
    }

    fun inject(fragment: CollectionFragment)
    fun inject(fragment: RemoteFilesManageFragment)

}

@Module(includes = [CollectionModule.Declarations::class])
object CollectionModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun openObject(
        repo: BlockRepository,
        settingsRepository: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): OpenObject = OpenObject(
        repo = repo,
        dispatchers = dispatchers,
        settings = settingsRepository
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideMoveUseCase(
        repo: BlockRepository,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ): Move = Move(repo, appCoroutineDispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun objectSearchSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        store: ObjectStore,
        dispatchers: AppCoroutineDispatchers
    ): ObjectSearchSubscriptionContainer = ObjectSearchSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun interceptEvents(channel: EventChannel): InterceptEvents = InterceptEvents(
        context = Dispatchers.IO,
        channel = channel
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsFavorite(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsFavorite = SetObjectListIsFavorite(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun getDeleteObjects(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteObjects = DeleteObjects(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun getCreateObject(
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
    @Provides
    @PerScreen
    fun provideGetTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates = GetTemplates(
        repo = repo,
        spaceManager = spaceManager,
        dispatchers = dispatchers
    )

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun factory(factory: CollectionViewModel.Factory): ViewModelProvider.Factory

        @PerScreen
        @Binds
        fun bindContainer(container: StorelessSubscriptionContainer.Impl): StorelessSubscriptionContainer
    }
}

interface CollectionDependencies : ComponentDependencies {
    fun dispatcherWidgets(): Dispatcher<WidgetDispatchEvent>
    fun dispatcherPayload(): Dispatcher<Payload>
    fun context(): Context
    fun blockRepo(): BlockRepository
    fun authRepo(): AuthRepository
    fun config(): ConfigStorage
    fun urlBuilder(): UrlBuilder
    fun objectStore(): ObjectStore
    fun subscriptionEventChannel(): SubscriptionEventChannel
    fun analytics(): Analytics
    fun eventChannel(): EventChannel
    fun userSettingsRepository(): UserSettingsRepository
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun logger(): Logger
    fun dateProvider(): DateProvider
    fun dateTypeNameProvider(): DateTypeNameProvider
    fun analyticsHelperDelegate(): AnalyticSpaceHelperDelegate
    fun provideUserPermissionProvider(): UserPermissionProvider
    fun fieldParser(): FieldParser
    fun resourceProvider(): ResourceProvider
    fun spaceViews(): SpaceViewSubscriptionContainer
}