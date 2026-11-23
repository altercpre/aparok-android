package io.aparok.app.di.feature.search

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.chats.ChatsDetailsSubscriptionContainer
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.multiplayer.ParticipantSubscriptionContainer
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.objects.CrossSpaceObjectTypesContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.workspace.DeepLinkToObjectDelegate
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.feature_search.presentation.SearchViewModel
import io.aparok.app.ui.search.v2.SearchV2Fragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SearchV2Dependencies::class],
    modules = [
        SearchV2Module::class,
        SearchV2Module.Declarations::class
    ]
)
@PerScreen
interface SearchV2Component {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: SearchViewModel.VmParams,
            dependencies: SearchV2Dependencies
        ): SearchV2Component
    }

    fun inject(fragment: SearchV2Fragment)

    /** For embedded use (the chat attach-object sheet). */
    fun searchViewModel(): SearchViewModel
}

@Module
object SearchV2Module {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDeepLinkToObjectDelegate(
        default: DeepLinkToObjectDelegate.Default
    ): DeepLinkToObjectDelegate = default

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchViewModel(
        factory: SearchViewModel.Factory
    ): SearchViewModel = factory.create(SearchViewModel::class.java)

    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: SearchViewModel.Factory): ViewModelProvider.Factory
    }
}

interface SearchV2Dependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun userSettingsRepository(): UserSettingsRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun fieldParser(): FieldParser
    fun configStorage(): ConfigStorage
    fun spaceManager(): SpaceManager
    fun userPermissionProvider(): UserPermissionProvider
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun participants(): ParticipantSubscriptionContainer
    fun chatsDetails(): ChatsDetailsSubscriptionContainer
    fun crossSpaceObjectTypes(): CrossSpaceObjectTypesContainer
    fun storeOfObjectTypes(): StoreOfObjectTypes
}
