package io.aparok.app.di.feature.home

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.core_utils.notifications.NotificationPermissionManager
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.bin.EmptyBin
import io.aparok.app.domain.block.interactor.Move
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.chats.ChatEventChannel
import io.aparok.app.domain.chats.ChatPreviewContainer
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.device.FileSharer
import io.aparok.app.domain.event.interactor.EventChannel
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.domain.favorites.PersonalFavoritesRepository
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.media.UploadFile
import io.aparok.app.domain.misc.AppActionManager
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import io.aparok.app.domain.notifications.SetSpaceNotificationMode
import io.aparok.app.domain.multiplayer.ParticipantSubscriptionContainer
import io.aparok.app.domain.multiplayer.SpaceInviteResolver
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.`object`.OpenObject
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.objects.ObjectWatcher
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.page.CloseObject
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.HasInstanceOfObjectTypeSubscriptionContainer
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.spaces.ClearLastOpenedSpace
import io.aparok.app.domain.spaces.SetHomepage
import io.aparok.app.domain.templates.GetTemplates
import io.aparok.app.domain.workspace.DeepLinkToObjectDelegate
import io.aparok.app.domain.workspace.NotificationsChannel
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.other.DefaultSpaceInviteResolver
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.PayloadDelegator
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.home.HomeScreenViewModel
import io.aparok.app.presentation.home.HomeScreenVmParams
import io.aparok.app.presentation.home.Unsubscriber
import io.aparok.app.presentation.spaces.SpaceGradientProvider
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.presentation.widgets.DefaultObjectViewReducer
import io.aparok.app.presentation.widgets.WidgetActiveViewStateHolder
import io.aparok.app.presentation.widgets.WidgetDispatchEvent
import io.aparok.app.presentation.widgets.WidgetSessionStateHolder
import io.aparok.app.providers.DefaultCoverImageHashProvider
import io.aparok.app.ui.home.WidgetOverlayFragment
import io.aparok.app.ui.home.WidgetsScreenFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Component(
    dependencies = [HomeScreenDependencies::class],
    modules = [
        HomeScreenModule::class,
        HomeScreenModule.Declarations::class
    ]
)
@PerScreen
interface HomeScreenComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: HomeScreenVmParams,
            dependencies: HomeScreenDependencies
        ): HomeScreenComponent
    }

    fun inject(fragment: WidgetsScreenFragment)
    fun inject(fragment: WidgetOverlayFragment)
}

@Module
object HomeScreenModule {

    @Provides
    fun provideUnqualifiedScope(
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): CoroutineScope = scope

    @JvmStatic
    @Provides
    @PerScreen
    fun openObject(
        repo: BlockRepository,
        settings: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): OpenObject = OpenObject(
        repo = repo,
        dispatchers = dispatchers,
        settings = settings
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun closeObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CloseObject = CloseObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun setObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun move(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ) : Move = Move(
        repo = repo,
        appCoroutineDispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun emptyBin(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager
    ) : EmptyBin = EmptyBin(
        repo = repo,
        dispatchers = dispatchers,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        settings: UserSettingsRepository,
    ): GetObject = GetObject(
        repo = repo,
        dispatchers = dispatchers,
        settings = settings
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun createObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        getDefaultEditorType: GetDefaultObjectType,
        spaceManager: SpaceManager
    ): CreateObject = CreateObject(
        repo = repo,
        dispatchers = dispatchers,
        getDefaultObjectType = getDefaultEditorType
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ) : GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers,
    ) : GetTemplates = GetTemplates(
        repo = repo,
        spaceManager = spaceManager,
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
    @Provides
    @PerScreen
    fun gradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @JvmStatic
    @Provides
    @PerScreen
    fun coverHashProvider() : CoverImageHashProvider = DefaultCoverImageHashProvider()

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSpaceInviteResolver(): SpaceInviteResolver = DefaultSpaceInviteResolver

    @JvmStatic
    @Provides
    @PerScreen
    fun setHomepage(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetHomepage = SetHomepage(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideExitToVaultDelegate(
        spaceManager: SpaceManager,
        clearLastOpenedSpace: ClearLastOpenedSpace
    ) : ExitToVaultDelegate = ExitToVaultDelegate.Default(
        spaceManager = spaceManager,
        clearLastOpenedSpace = clearLastOpenedSpace
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUploadFile(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): UploadFile = UploadFile(repo = repo, dispatchers = dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun factory(factory: HomeScreenViewModel.Factory): ViewModelProvider.Factory

        @PerScreen
        @Binds
        fun container(container: StorelessSubscriptionContainer.Impl): StorelessSubscriptionContainer

        @PerScreen
        @Binds
        fun holder(holder: WidgetActiveViewStateHolder.Impl): WidgetActiveViewStateHolder

        @PerScreen
        @Binds
        fun unsubscriber(impl: Unsubscriber.Impl) : Unsubscriber

        @PerScreen
        @Binds
        fun widgetSessionStateHolder(
            holder: WidgetSessionStateHolder.Impl
        ): WidgetSessionStateHolder

        @PerScreen
        @Binds
        fun objectWatcherReducer(
            default: DefaultObjectViewReducer
        ): ObjectWatcher.Reducer

        @PerScreen
        @Binds
        fun deepLinkToObjectDelegate(
            default: DeepLinkToObjectDelegate.Default
        ) : DeepLinkToObjectDelegate
    }
}

interface HomeScreenDependencies : ComponentDependencies {
    fun dispatcherWidgets(): Dispatcher<WidgetDispatchEvent>
    fun dispatcherPayload(): Dispatcher<Payload>
    fun blockRepo(): BlockRepository
    fun personalFavoritesRepo(): PersonalFavoritesRepository
    fun authRepo(): AuthRepository
    fun userRepo(): UserSettingsRepository
    fun config(): ConfigStorage
    fun urlBuilder(): UrlBuilder
    fun objectStore(): ObjectStore
    fun subscriptionEventChannel(): SubscriptionEventChannel
    fun analytics(): Analytics
    fun eventChannel(): EventChannel
    fun dispatchers(): AppCoroutineDispatchers
    fun appActionManager(): AppActionManager
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun hasInstanceOfObjectTypeSubscriptionContainer(): HasInstanceOfObjectTypeSubscriptionContainer
    fun dateProvider(): DateProvider
    fun logger(): Logger
    fun spaceManager(): SpaceManager
    fun userPermissionProvider(): UserPermissionProvider
    fun notificationChannel(): NotificationsChannel
    fun activeSpaceMembers() : ActiveSpaceMemberSubscriptionContainer
    fun analyticSpaceHelperDelegate(): AnalyticSpaceHelperDelegate
    fun storeOfRelations(): StoreOfRelations
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun participantSubscriptionContainer(): ParticipantSubscriptionContainer
    fun featureToggles(): FeatureToggles
    fun payloadDelegator(): PayloadDelegator
    fun fieldParser(): FieldParser
    fun notificationPermissionManager(): NotificationPermissionManager
    fun provideChatEventChannel(): ChatEventChannel
    fun provideChatPreviewContainer(): ChatPreviewContainer
    fun clipboard(): io.aparok.app.domain.clipboard.Clipboard
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun stringResProvider() : StringResourceProvider
    fun setSpaceNotificationMode(): SetSpaceNotificationMode
    fun fileSharer(): FileSharer
}
