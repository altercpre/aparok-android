package io.aparok.app.di.feature.spaces

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.core_utils.notifications.NotificationPermissionManager
import io.aparok.app.device.DefaultGradientCollectionProvider
import io.aparok.app.device.share.debug.DebugSpaceDeviceFileContentSaver
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.interactor.GetAccount
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.clipboard.Clipboard
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.cover.GetCoverGradientCollection
import io.aparok.app.domain.debugging.DebugSpaceContentSaver
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.device.DeviceTokenStoringService
import io.aparok.app.domain.invite.SpaceInviteLinkStore
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.AppActionManager
import io.aparok.app.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.notifications.SetSpaceNotificationMode
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.ProfileSubscriptionManager
import io.aparok.app.domain.wallpaper.SetWallpaper
import io.aparok.app.domain.wallpaper.WallpaperStore
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.spaces.SpaceGradientProvider
import io.aparok.app.presentation.spaces.SpaceSettingsViewModel
import io.aparok.app.presentation.util.downloader.UriFileProvider
import io.aparok.app.ui.settings.space.SpaceSettingsFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SpaceSettingsDependencies::class],
    modules = [
        SpaceSettingsModule::class,
        SpaceSettingsModule.Bindings::class
    ]
)
@PerScreen
interface SpaceSettingsComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: SpaceSettingsViewModel.VmParams,
            dependencies: SpaceSettingsDependencies
        ) : SpaceSettingsComponent
    }

    fun inject(fragment: SpaceSettingsFragment)
}

@Module
object SpaceSettingsModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSpaceGradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetAccountUseCase(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetAccount = GetAccount(repo = repo, dispatcher = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetSpaceNotificationModeUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetSpaceNotificationMode = SetSpaceNotificationMode(repository, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetWallpaper(
        repo: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ) : SetWallpaper = SetWallpaper(
        repo = repo,
        store = WallpaperStore.Default,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetCoverGradientCollectionUseCase(
    ): GetCoverGradientCollection = GetCoverGradientCollection(DefaultGradientCollectionProvider)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchObjects(
        repository: BlockRepository
    ): SearchObjects = SearchObjects(repo = repository)

    @Module
    interface Bindings {

        @PerScreen
        @Binds
        fun bindSpaceDebugDeviceSharer(
            saver: DebugSpaceDeviceFileContentSaver
        ): DebugSpaceContentSaver
        @PerScreen
        @Binds
        fun factory(factory: SpaceSettingsViewModel.Factory): ViewModelProvider.Factory
    }
}

interface SpaceSettingsDependencies : ComponentDependencies {
    fun blockRepo(): BlockRepository
    fun auth(): AuthRepository
    fun appActions(): AppActionManager
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun settings(): UserSettingsRepository
    fun urlBuilder(): UrlBuilder
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun container(): StorelessSubscriptionContainer
    fun context(): Context
    fun userPermission(): UserPermissionProvider
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun activeSpaceMemberSubscriptionContainer(): ActiveSpaceMemberSubscriptionContainer
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun profileContainer(): ProfileSubscriptionManager
    fun uriFileProvider(): UriFileProvider
    fun notificationsPermissionManager(): NotificationPermissionManager
    fun deviceTokenStoreService(): DeviceTokenStoringService
    fun clipboard() : Clipboard
    fun logger(): Logger
    fun spaceInviteLinkStore() : SpaceInviteLinkStore
    fun stringResourceProvider(): StringResourceProvider
    fun fieldParser(): FieldParser
}