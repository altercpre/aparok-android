package io.aparok.app.di.feature.sharing

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.interactor.CreateBlock
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.chats.AddChatMessage
import io.aparok.app.domain.collections.AddObjectToCollection
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.device.FileSharer
import io.aparok.app.domain.media.UploadFile
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.objects.CreateBookmarkObject
import io.aparok.app.domain.objects.CreateObjectFromUrl
import io.aparok.app.domain.objects.CreatePrefilledNote
import io.aparok.app.domain.page.AddBackLinkToObject
import io.aparok.app.domain.page.CloseObject
import io.aparok.app.domain.page.OpenPage
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.sharing.SharingViewModel
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * DI Component for the redesigned sharing extension.
 * Provides dependencies for SharingViewModel which handles three flows:
 * - Flow 1: Chat Space (direct message sending)
 * - Flow 2: Data Space without chat (object creation)
 * - Flow 3: Data Space with chat (hybrid)
 */
@Component(
    dependencies = [SharingDependencies::class],
    modules = [
        SharingModule::class,
        SharingModule.Declarations::class
    ]
)
@PerModal
interface SharingComponent {
    @Component.Factory
    interface Factory {
        fun create(dependency: SharingDependencies): SharingComponent
    }

    /**
     * Provides the ViewModel factory for creating SharingViewModel instances.
     * Used by MainActivity's Compose overlay for the pure Compose modal sheet.
     */
    fun viewModelFactory(): ViewModelProvider.Factory
}

@Module
object SharingModule {

    @Provides
    @PerModal
    fun provideCreateBookmarkObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateBookmarkObject = CreateBookmarkObject(repo)

    @Provides
    @PerModal
    fun provideCreatePrefilledNote(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreatePrefilledNote = CreatePrefilledNote(repo, dispatchers)

    @Provides
    @PerModal
    fun provideCreateObjectFromUrl(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateObjectFromUrl = CreateObjectFromUrl(repo, dispatchers)

    @Provides
    @PerModal
    fun provideAddChatMessage(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): AddChatMessage = AddChatMessage(repo, dispatchers)

    @Provides
    @PerModal
    fun provideUploadFile(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): UploadFile = UploadFile(repo, dispatchers)

    @Provides
    @PerModal
    fun provideSearchObjects(
        repo: BlockRepository
    ): SearchObjects = SearchObjects(repo)

    @Provides
    @PerModal
    fun provideOpenPage(
        repo: BlockRepository,
        settings: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): OpenPage = OpenPage(
        repo = repo,
        settings = settings,
        dispatchers = dispatchers
    )

    @Provides
    @PerModal
    fun provideCloseObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CloseObject = CloseObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @Provides
    @PerModal
    fun provideCreateBlock(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateBlock = CreateBlock(
        repo = repo,
        dispatchers = dispatchers
    )

    @Provides
    @PerModal
    fun provideAddBackLinkToObject(
        openPage: OpenPage,
        createBlock: CreateBlock,
        closeObject: CloseObject,
        dispatchers: AppCoroutineDispatchers
    ): AddBackLinkToObject = AddBackLinkToObject(
        openPage = openPage,
        createBlock = createBlock,
        closeObject = closeObject,
        dispatchers = dispatchers
    )

    @Provides
    @PerModal
    fun provideAddObjectToCollection(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): AddObjectToCollection = AddObjectToCollection(repo, dispatchers)

    @Module
    interface Declarations {
        @PerModal
        @Binds
        fun factory(factory: SharingViewModel.Factory): ViewModelProvider.Factory
    }
}

/**
 * Dependencies required by the SharingComponent.
 * These are provided by the parent component (MainComponent).
 */
interface SharingDependencies : ComponentDependencies {
    fun blockRepo(): BlockRepository
    fun spaceManager(): SpaceManager
    fun dispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun awaitAccountStartedManager(): AwaitAccountStartManager
    fun analytics(): Analytics
    fun fileSharer(): FileSharer
    fun permissions(): UserPermissionProvider
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun fieldParser(): FieldParser
    fun userSettingsRepository(): UserSettingsRepository
}
