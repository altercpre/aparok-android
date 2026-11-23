package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.interactor.CreateBlock
import io.aparok.app.domain.block.interactor.UpdateFields
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.collections.AddObjectToCollection
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.dashboard.interactor.SetObjectListIsFavorite
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.favorites.AddPersonalFavorite
import io.aparok.app.presentation.widgets.ObservePersonalFavoriteTargets
import io.aparok.app.domain.favorites.RemovePersonalFavorite
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.GetSpaceInviteLink
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.DuplicateObject
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.page.AddBackLinkToObject
import io.aparok.app.domain.page.CloseObject
import io.aparok.app.domain.page.OpenPage
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.relations.AddToFeaturedRelations
import io.aparok.app.domain.relations.DeleteRelationFromObject
import io.aparok.app.domain.relations.RemoveFromFeaturedRelations
import io.aparok.app.domain.templates.CreateTemplateFromObject
import io.aparok.app.domain.widgets.CreateWidget
import io.aparok.app.domain.widgets.DeleteWidget
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.domain.workspace.spaceIdOrNull
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.common.PayloadDelegator
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.objects.menu.ObjectMenuOptionsProvider
import io.aparok.app.presentation.objects.menu.ObjectMenuOptionsProviderImpl
import io.aparok.app.presentation.objects.menu.ObjectMenuViewModel
import io.aparok.app.presentation.objects.menu.ObjectSetMenuViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.util.downloader.DebugGoroutinesShareDownloader
import io.aparok.app.presentation.util.downloader.DebugTreeShareDownloader
import io.aparok.app.ui.editor.sheets.ObjectMenuFragment
import io.aparok.app.ui.sets.ObjectSetMenuFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull


@Subcomponent(modules = [ObjectMenuModuleBase::class, ObjectMenuModule::class])
@PerDialog
interface ObjectMenuComponent {
    @Subcomponent.Builder
    interface Builder {
        fun base(module: ObjectMenuModuleBase): Builder
        fun module(module: ObjectMenuModule): Builder
        fun build(): ObjectMenuComponent
    }

    fun inject(fragment: ObjectMenuFragment)
}

@Subcomponent(modules = [ObjectMenuModuleBase::class, ObjectSetMenuModule::class])
@PerDialog
interface ObjectSetMenuComponent {
    @Subcomponent.Builder
    interface Builder {
        fun base(module: ObjectMenuModuleBase): Builder
        fun module(module: ObjectSetMenuModule): Builder
        fun build(): ObjectSetMenuComponent
    }

    fun inject(fragment: ObjectSetMenuFragment)
}

@Module
object ObjectMenuModuleBase {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideAddBackLinkToObject(
        openPage: OpenPage,
        createBlock: CreateBlock,
        closeObject: CloseObject,
        dispatchers: AppCoroutineDispatchers
    ): AddBackLinkToObject = AddBackLinkToObject(openPage, createBlock, closeObject, dispatchers)
}

@Module
object ObjectMenuModule {
    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        duplicateObject: DuplicateObject,
        debugTreeShareDownloader: DebugTreeShareDownloader,
        addBackLinkToObject: AddBackLinkToObject,
        urlBuilder: UrlBuilder,
        storage: Editor.Storage,
        analytics: Analytics,
        dispatcher: Dispatcher<Payload>,
        updateFields: UpdateFields,
        featureToggles: FeatureToggles,
        delegator: Delegator<Action>,
        addObjectToCollection: AddObjectToCollection,
        createTemplateFromObject: CreateTemplateFromObject,
        setObjectDetails: SetObjectDetails,
        debugGoroutinesShareDownloader: DebugGoroutinesShareDownloader,
        createWidget: CreateWidget,
        spaceManager: SpaceManager,
        deepLinkResolver: DeepLinkResolver,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        payloadDelegator: PayloadDelegator,
        setObjectListIsFavorite: SetObjectListIsFavorite,
        setObjectIsArchived: SetObjectListIsArchived,
        fieldParser: FieldParser,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        getSpaceInviteLink: GetSpaceInviteLink,
        addToFeaturedRelations: AddToFeaturedRelations,
        removeFromFeaturedRelations: RemoveFromFeaturedRelations,
        userPermissionProvider: UserPermissionProvider,
        deleteRelationFromObject: DeleteRelationFromObject,
        objectMenuOptionsProvider: ObjectMenuOptionsProvider,
        showObject: GetObject,
        deleteWidget: DeleteWidget,
        addPersonalFavorite: AddPersonalFavorite,
        removePersonalFavorite: RemovePersonalFavorite
    ): ObjectMenuViewModel.Factory = ObjectMenuViewModel.Factory(
        setObjectIsArchived = setObjectIsArchived,
        duplicateObject = duplicateObject,
        debugTreeShareDownloader = debugTreeShareDownloader,
        addBackLinkToObject = addBackLinkToObject,
        urlBuilder = urlBuilder,
        storage = storage,
        analytics = analytics,
        dispatcher = dispatcher,
        updateFields = updateFields,
        delegator = delegator,
        menuOptionsProvider = objectMenuOptionsProvider,
        addObjectToCollection = addObjectToCollection,
        createTemplateFromObject = createTemplateFromObject,
        setObjectDetails = setObjectDetails,
        debugGoroutinesShareDownloader = debugGoroutinesShareDownloader,
        createWidget = createWidget,
        spaceManager = spaceManager,
        deepLinkResolver = deepLinkResolver,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        payloadDelegator = payloadDelegator,
        setObjectListIsFavorite = setObjectListIsFavorite,
        fieldParser = fieldParser,
        getSpaceInviteLink = getSpaceInviteLink,
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
        addToFeaturedRelations = addToFeaturedRelations,
        removeFromFeaturedRelations = removeFromFeaturedRelations,
        userPermissionProvider = userPermissionProvider,
        deleteRelationFromObject = deleteRelationFromObject,
        showObject = showObject,
        deleteWidget = deleteWidget,
        addPersonalFavorite = addPersonalFavorite,
        removePersonalFavorite = removePersonalFavorite
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideDeleteRelationFromObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationFromObject = DeleteRelationFromObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo,
        dispatchers
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @PerDialog
    @JvmStatic
    fun provideMenuOptionsProvider(
        storage: Editor.Storage,
        spaceManager: SpaceManager,
        observePersonalFavoriteTargets: ObservePersonalFavoriteTargets,
        userPermissionProvider: UserPermissionProvider
    ): ObjectMenuOptionsProvider {
        // From the state, not the config flow: a space can be active without a config
        // (SpaceManager.activate, used by quick capture to skip Workspace.Open), and the
        // config flow stays silent in that case — which would leave this menu, opened from
        // the quick-capture sheet, with no space at all.
        val spaceIdFlow = spaceManager.state()
            .mapNotNull { it.spaceIdOrNull() }
            .distinctUntilChanged()
        val personalFavoriteTargets = spaceIdFlow.flatMapLatest { space ->
            observePersonalFavoriteTargets(space).map { it.toSet() }
        }
        val canToggleChannelPin = spaceIdFlow.flatMapLatest { space ->
            userPermissionProvider.observe(space).map { it?.isOwnerOrEditor() == true }
        }
        return ObjectMenuOptionsProviderImpl(
            objectViewDetailsFlow = storage.details.stream(),
            hasObjectLayoutConflict = storage.hasLayoutOrRelationConflict.stream(),
            personalFavoriteTargets = personalFavoriteTargets,
            canToggleChannelPin = canToggleChannelPin
        )
    }

    @JvmStatic
    @Provides
    @PerDialog
    fun provideAddObjectToCollection(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): AddObjectToCollection = AddObjectToCollection(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideFavoriteUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsFavorite = SetObjectListIsFavorite(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerDialog
    fun provideArchiveUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerDialog
    fun addToFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): AddToFeaturedRelations =
        AddToFeaturedRelations(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerDialog
    fun removeFromFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): RemoveFromFeaturedRelations =
        RemoveFromFeaturedRelations(repo, dispatchers)
}

@Module
object ObjectSetMenuModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideUpdateFieldsUseCase(
        repo: BlockRepository
    ): UpdateFields = UpdateFields(repo)

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        addBackLinkToObject: AddBackLinkToObject,
        duplicateObject: DuplicateObject,
        delegator: Delegator<Action>,
        urlBuilder: UrlBuilder,
        analytics: Analytics,
        state: MutableStateFlow<ObjectState>,
        updateFields: UpdateFields,
        featureToggles: FeatureToggles,
        dispatcher: Dispatcher<Payload>,
        addObjectToCollection: AddObjectToCollection,
        debugGoroutinesShareDownloader: DebugGoroutinesShareDownloader,
        createWidget: CreateWidget,
        spaceManager: SpaceManager,
        deepLinkResolver: DeepLinkResolver,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        payloadDelegator: PayloadDelegator,
        setObjectListIsFavorite: SetObjectListIsFavorite,
        setObjectIsArchived: SetObjectListIsArchived,
        fieldParser: FieldParser,
        getSpaceInviteLink: GetSpaceInviteLink,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        addToFeaturedRelations: AddToFeaturedRelations,
        removeFromFeaturedRelations: RemoveFromFeaturedRelations,
        userPermissionProvider: UserPermissionProvider,
        deleteRelationFromObject: DeleteRelationFromObject,
        setObjectDetails: SetObjectDetails,
        objectMenuOptionsProvider: ObjectMenuOptionsProvider,
        showObject: GetObject,
        deleteWidget: DeleteWidget,
        addPersonalFavorite: AddPersonalFavorite,
        removePersonalFavorite: RemovePersonalFavorite
    ): ObjectSetMenuViewModel.Factory = ObjectSetMenuViewModel.Factory(
        setObjectListIsArchived = setObjectIsArchived,
        addBackLinkToObject = addBackLinkToObject,
        duplicateObject = duplicateObject,
        urlBuilder = urlBuilder,
        delegator = delegator,
        analytics = analytics,
        objectState = state,
        dispatcher = dispatcher,
        menuOptionsProvider = objectMenuOptionsProvider,
        addObjectToCollection = addObjectToCollection,
        debugGoroutinesShareDownloader = debugGoroutinesShareDownloader,
        createWidget = createWidget,
        spaceManager = spaceManager,
        deepLinkResolver = deepLinkResolver,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        payloadDelegator = payloadDelegator,
        setObjectListIsFavorite = setObjectListIsFavorite,
        fieldParser = fieldParser,
        getSpaceInviteLink = getSpaceInviteLink,
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
        addToFeaturedRelations = addToFeaturedRelations,
        removeFromFeaturedRelations = removeFromFeaturedRelations,
        userPermissionProvider = userPermissionProvider,
        deleteRelationFromObject = deleteRelationFromObject,
        updateFields = updateFields,
        setObjectDetails = setObjectDetails,
        showObject = showObject,
        deleteWidget = deleteWidget,
        addPersonalFavorite = addPersonalFavorite,
        removePersonalFavorite = removePersonalFavorite
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideDeleteRelationFromObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationFromObject = DeleteRelationFromObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideOpenPage(
        repo: BlockRepository,
        settings: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): OpenPage = OpenPage(
        repo = repo,
        settings = settings,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideFavoriteUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsFavorite = SetObjectListIsFavorite(repo = repo, dispatchers = dispatchers)

    @OptIn(ExperimentalCoroutinesApi::class)
    @JvmStatic
    @Provides
    @PerDialog
    fun provideMenuOptionsProvider(
        state: MutableStateFlow<ObjectState>,
        spaceManager: SpaceManager,
        observePersonalFavoriteTargets: ObservePersonalFavoriteTargets,
        userPermissionProvider: UserPermissionProvider
    ): ObjectMenuOptionsProvider {
        val objectViewDetailsFlow = state
            .flatMapLatest { currentState ->
                if (currentState is ObjectState.DataView) {
                    flowOf(currentState.details)
                } else {
                    emptyFlow()
                }
            }
            .distinctUntilChanged()

        val hasObjectLayoutConflictFlow = state
            .flatMapLatest { currentState ->
                if (currentState is ObjectState.DataView) {
                    flowOf(currentState.hasObjectLayoutConflict)
                } else {
                    emptyFlow()
                }
            }
            .distinctUntilChanged()

        // From the state, not the config flow: a space can be active without a config
        // (SpaceManager.activate, used by quick capture to skip Workspace.Open), and the
        // config flow stays silent in that case — which would leave this menu, opened from
        // the quick-capture sheet, with no space at all.
        val spaceIdFlow = spaceManager.state()
            .mapNotNull { it.spaceIdOrNull() }
            .distinctUntilChanged()
        val personalFavoriteTargets = spaceIdFlow.flatMapLatest { space ->
            observePersonalFavoriteTargets(space).map { it.toSet() }
        }
        val canToggleChannelPin = spaceIdFlow.flatMapLatest { space ->
            userPermissionProvider.observe(space).map { it?.isOwnerOrEditor() == true }
        }

        return ObjectMenuOptionsProviderImpl(
            objectViewDetailsFlow = objectViewDetailsFlow,
            hasObjectLayoutConflict = hasObjectLayoutConflictFlow,
            personalFavoriteTargets = personalFavoriteTargets,
            canToggleChannelPin = canToggleChannelPin
        )
    }

    @JvmStatic
    @Provides
    @PerDialog
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo,
        dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun addToFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): AddToFeaturedRelations =
        AddToFeaturedRelations(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerDialog
    fun removeFromFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): RemoveFromFeaturedRelations =
        RemoveFromFeaturedRelations(repo, dispatchers)
}