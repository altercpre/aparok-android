package io.aparok.app.di.feature.gallery

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.data.auth.event.EventProcessImportDateChannel
import io.aparok.app.data.auth.event.EventProcessImportRemoteChannel
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.workspace.EventProcessImportChannel
import io.aparok.app.gallery_experience.viewmodel.GalleryInstallationViewModel
import io.aparok.app.gallery_experience.viewmodel.GalleryInstallationViewModelFactory
import io.aparok.app.middleware.EventProxy
import io.aparok.app.middleware.interactor.EventProcessImportMiddlewareChannel
import io.aparok.app.presentation.spaces.SpaceGradientProvider
import io.aparok.app.ui.gallery.GalleryInstallationFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [GalleryInstallationComponentDependencies::class],
    modules = [
        GalleryInstallationModule::class,
        GalleryInstallationModule.Declarations::class
    ]
)
@PerScreen
interface GalleryInstallationComponent {

    @Component.Builder
    interface Builder {
        fun withDependencies(dependencies: GalleryInstallationComponentDependencies): Builder

        @BindsInstance
        fun withParams(params: GalleryInstallationViewModel.ViewModelParams): Builder
        fun build(): GalleryInstallationComponent
    }

    fun inject(fragment: GalleryInstallationFragment)
}

@Module
object GalleryInstallationModule {

    @Provides
    @PerScreen
    fun provideEventProcessRemoteChannel(
        proxy: EventProxy
    ): EventProcessImportRemoteChannel = EventProcessImportMiddlewareChannel(events = proxy)

    @Provides
    @PerScreen
    fun provideEventProcessDateChannel(
        channel: EventProcessImportRemoteChannel
    ): EventProcessImportChannel = EventProcessImportDateChannel(channel = channel)

    @Provides
    @PerScreen
    fun provideGradientProvider(): SpaceGradientProvider {
        return SpaceGradientProvider.Default
    }

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: GalleryInstallationViewModelFactory
        ): ViewModelProvider.Factory

    }
}

interface GalleryInstallationComponentDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun userPermissionProvider(): UserPermissionProvider
    fun eventProxy(): EventProxy
    fun configStorage(): ConfigStorage
}