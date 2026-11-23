package io.aparok.app.di.feature

import android.content.Context
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.di.feature.cover.UnsplashSubComponent
import io.aparok.app.di.feature.history.VersionHistoryComponent
import io.aparok.app.di.feature.relations.RelationAddToObjectSubComponent
import io.aparok.app.di.feature.relations.RelationCreateFromScratchForObjectBlockSubComponent
import io.aparok.app.di.feature.relations.RelationCreateFromScratchForObjectSubComponent
import io.aparok.app.domain.ai.TypeSuggestionEngine
import io.aparok.app.domain.auth.interactor.ClearLastOpenedObject
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.UpdateDivider
import io.aparok.app.domain.block.interactor.ClearBlockContent
import io.aparok.app.domain.block.interactor.ClearBlockStyle
import io.aparok.app.domain.block.interactor.CreateBlock
import io.aparok.app.domain.block.interactor.DuplicateBlock
import io.aparok.app.domain.block.interactor.MergeBlocks
import io.aparok.app.domain.block.interactor.Move
import io.aparok.app.domain.block.interactor.RemoveLinkMark
import io.aparok.app.domain.block.interactor.ReplaceBlock
import io.aparok.app.domain.block.interactor.SetLinkAppearance
import io.aparok.app.domain.block.interactor.SetObjectType
import io.aparok.app.domain.block.interactor.SplitBlock
import io.aparok.app.domain.block.interactor.TurnIntoDocument
import io.aparok.app.domain.block.interactor.TurnIntoStyle
import io.aparok.app.domain.block.interactor.UnlinkBlocks
import io.aparok.app.domain.block.interactor.UpdateAlignment
import io.aparok.app.domain.block.interactor.UpdateBackgroundColor
import io.aparok.app.domain.block.interactor.UpdateBlocksMark
import io.aparok.app.domain.block.interactor.UpdateCheckbox
import io.aparok.app.domain.block.interactor.UpdateFields
import io.aparok.app.domain.block.interactor.UpdateLinkMarks
import io.aparok.app.domain.block.interactor.UpdateText
import io.aparok.app.domain.block.interactor.UpdateTextColor
import io.aparok.app.domain.block.interactor.UpdateTextStyle
import io.aparok.app.domain.block.interactor.UploadBlock
import io.aparok.app.domain.block.interactor.sets.CreateObjectSet
import io.aparok.app.domain.block.interactor.sets.GetObjectTypes
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.clipboard.Clipboard
import io.aparok.app.domain.clipboard.Copy
import io.aparok.app.domain.clipboard.Paste
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.cover.SetDocCoverImage
import io.aparok.app.domain.chats.GetChatMessages
import io.aparok.app.domain.discussions.AddDiscussion
import io.aparok.app.domain.download.DownloadFile
import io.aparok.app.domain.download.Downloader
import io.aparok.app.domain.event.interactor.EventChannel
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.icon.SetDocumentImageIcon
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.networkmode.GetNetworkMode
import io.aparok.app.domain.`object`.ConvertObjectToCollection
import io.aparok.app.domain.`object`.ConvertObjectToSet
import io.aparok.app.domain.`object`.DuplicateObject
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.objects.options.GetOptions
import io.aparok.app.domain.page.CloseObject
import io.aparok.app.domain.page.CreateBlockLinkWithObject
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.page.CreateObjectAsMentionOrLink
import io.aparok.app.domain.page.OpenPage
import io.aparok.app.domain.page.Redo
import io.aparok.app.domain.page.Undo
import io.aparok.app.domain.page.UpdateTitle
import io.aparok.app.domain.page.bookmark.CreateBookmarkBlock
import io.aparok.app.domain.page.bookmark.SetupBookmark
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.relations.AddFileToObject
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.relations.SetRelationKey
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.spaces.ClearLastOpenedSpace
import io.aparok.app.domain.sets.FindObjectSetForType
import io.aparok.app.domain.table.CreateTable
import io.aparok.app.domain.table.CreateTableColumn
import io.aparok.app.domain.table.CreateTableRow
import io.aparok.app.domain.table.DeleteTableColumn
import io.aparok.app.domain.table.DeleteTableRow
import io.aparok.app.domain.table.DuplicateTableColumn
import io.aparok.app.domain.table.DuplicateTableRow
import io.aparok.app.domain.table.FillTableColumn
import io.aparok.app.domain.table.FillTableRow
import io.aparok.app.domain.table.MoveTableColumn
import io.aparok.app.domain.table.MoveTableRow
import io.aparok.app.domain.table.SetTableRowHeader
import io.aparok.app.domain.templates.ApplyTemplate
import io.aparok.app.domain.unsplash.DownloadUnsplashImage
import io.aparok.app.domain.unsplash.UnsplashRepository
import io.aparok.app.domain.workspace.FileLimitsEventChannel
import io.aparok.app.domain.workspace.InterceptFileLimitEvents
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.editor.DocumentExternalEventReducer
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.editor.EditorViewModel
import io.aparok.app.presentation.editor.EditorViewModelFactory
import io.aparok.app.presentation.navigation.backstack.BackHistoryDelegate
import io.aparok.app.presentation.navigation.backstack.NavigationBackStackInspector
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.editor.editor.Interactor
import io.aparok.app.presentation.editor.editor.Orchestrator
import io.aparok.app.presentation.editor.editor.pattern.DefaultPatternMatcher
import io.aparok.app.presentation.editor.editor.table.DefaultEditorTableDelegate
import io.aparok.app.presentation.editor.editor.table.EditorTableDelegate
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import io.aparok.app.presentation.editor.selection.SelectionStateHolder
import io.aparok.app.presentation.editor.toggle.ToggleStateHolder
import io.aparok.app.presentation.objects.LockedStateProvider
import io.aparok.app.presentation.relations.providers.DefaultObjectRelationProvider
import io.aparok.app.presentation.relations.providers.DefaultObjectValueProvider
import io.aparok.app.presentation.relations.providers.ObjectRelationListProvider
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider.Companion.INTRINSIC_PROVIDER_TYPE
import io.aparok.app.presentation.relations.providers.ObjectValueProvider
import io.aparok.app.presentation.templates.ObjectTypeTemplatesContainer
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.util.UrlHelper
import io.aparok.app.presentation.util.defaultCopyFileToCacheDirectory
import io.aparok.app.presentation.util.downloader.DebugTreeShareDownloader
import io.aparok.app.presentation.util.downloader.DocumentFileShareDownloader
import io.aparok.app.presentation.util.downloader.UriFileProvider
import io.aparok.app.presentation.widgets.collection.ResourceProvider
import io.aparok.app.providers.DefaultCoverImageHashProvider
import io.aparok.app.ui.editor.EditorFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import kotlinx.coroutines.Dispatchers

@Subcomponent(
    modules = [
        EditorSessionModule::class,
        EditorUseCaseModule::class,
    ]
)
@PerScreen
interface EditorSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: EditorViewModel.Params): Builder
        fun session(module: EditorSessionModule): Builder
        fun usecase(module: EditorUseCaseModule): Builder
        fun build(): EditorSubComponent
    }

    fun inject(fragment: EditorFragment)

    fun objectIconPickerComponent(): ObjectIconPickerComponent.Builder
    fun textBlockIconPickerComponent(): TextBlockIconPickerComponent.Builder

    // Relations

    fun objectRelationListComponent(): ObjectRelationListComponent.Builder
    fun relationAddToObjectComponent(): RelationAddToObjectSubComponent.Builder
    fun relationCreateFromScratchForObjectComponent(): RelationCreateFromScratchForObjectSubComponent.Builder
    fun relationCreateFromScratchForObjectBlockComponent(): RelationCreateFromScratchForObjectBlockSubComponent.Builder
    fun relationTextValueComponent(): RelationTextValueSubComponent.Builder
    fun editRelationDateComponent(): DefaultRelationDataValueSubComponent.Builder

    fun objectCoverComponent(): SelectCoverObjectSubComponent.Builder
    fun objectUnsplashComponent(): UnsplashSubComponent.Builder
    fun objectMenuComponent(): ObjectMenuComponent.Builder

    fun objectAppearanceSettingComponent(): ObjectAppearanceSettingSubComponent.Builder
    fun objectAppearanceIconComponent(): ObjectAppearanceIconSubComponent.Builder
    fun objectAppearancePreviewLayoutComponent(): ObjectAppearancePreviewLayoutSubComponent.Builder
    fun objectAppearanceChooseDescription(): ObjectAppearanceChooseDescriptionSubComponent.Builder

    fun setBlockTextValueComponent(): SetBlockTextValueSubComponent.Builder

    fun linkToObjectOrWebBuilder(): LinkToObjectOrWebSubComponent.Builder

    fun tagStatusObjectComponent(): TagOrStatusValueObjectComponent.Builder
    fun optionObjectComponent(): CreateOrEditOptionObjectComponent.Builder
    fun objectValueComponent(): ObjectValueObjectComponent.Builder
    fun versionHistoryComponent() : VersionHistoryComponent.Builder
}


/**
 * Session-related dependencies, session being defined as active work with a document visible to our user.
 * Hence, these dependencies are stateful and therefore should not be shared between different sessions of the same document.
 * Consider the following navigation scenario: Document A > Document B > Document A'.
 * In this case, stateful dependencies should not be shared between A and A'.
 */
@Module
object EditorSessionModule {

    @JvmStatic
    @Provides
    fun provideToggler(): ToggleStateHolder = ToggleStateHolder.Default()

    @JvmStatic
    @Provides
    fun provideProxer(): Editor.Proxer = Editor.Proxer()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideStorage(): Editor.Storage = Editor.Storage()

    @JvmStatic
    @Provides
    @PerScreen
    fun lockedStateProvider(
        storage: Editor.Storage
    ) : LockedStateProvider = LockedStateProvider.EditorLockedStateProvider(
        storage = storage
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun relationListProvider(
        storage: Editor.Storage
    ) : ObjectRelationListProvider = ObjectRelationListProvider.EditorRelationListProvider(
        storage = storage
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileLimitEvents(
        channel: FileLimitsEventChannel,
        dispatchers: AppCoroutineDispatchers
    ) : InterceptFileLimitEvents = InterceptFileLimitEvents(channel, dispatchers)

    @JvmStatic
    @Provides
    fun providePageViewModelFactory(
        params: EditorViewModel.Params,
        permissions: UserPermissionProvider,
        openPage: OpenPage,
        closePage: CloseObject,
        interceptEvents: InterceptEvents,
        updateLinkMarks: UpdateLinkMarks,
        removeLinkMark: RemoveLinkMark,
        createObjectSet: CreateObjectSet,
        createBlockLinkWithObject: CreateBlockLinkWithObject,
        createObjectAsMentionOrLink: CreateObjectAsMentionOrLink,
        documentExternalEventReducer: DocumentExternalEventReducer,
        urlBuilder: UrlBuilder,
        renderer: DefaultBlockViewRenderer,
        orchestrator: Orchestrator,
        analytics: Analytics,
        dispatcher: Dispatcher<Payload>,
        delegator: Delegator<Action>,
        updateDetail: UpdateDetail,
        searchObjects: SearchObjects,
        getDefaultObjectType: GetDefaultObjectType,
        findObjectSetForType: FindObjectSetForType,
        copyFileToCacheDirectory: CopyFileToCacheDirectory,
        downloadUnsplashImage: DownloadUnsplashImage,
        setDocCoverImage: SetDocCoverImage,
        setDocImageIcon: SetDocumentImageIcon,
        createObject: CreateObject,
        storeOfRelations: StoreOfRelations,
        storeOfObjectTypes: StoreOfObjectTypes,
        objectToSet: ConvertObjectToSet,
        featureToggles: FeatureToggles,
        tableDelegate: EditorTableDelegate,
        spaceManager: SpaceManager,
        getObjectTypes: GetObjectTypes,
        objectToCollection: ConvertObjectToCollection,
        interceptFileLimitEvents: InterceptFileLimitEvents,
        addRelationToObject: AddRelationToObject,
        applyTemplate: ApplyTemplate,
        setObjectType: SetObjectType,
        templatesContainer: ObjectTypeTemplatesContainer,
        storelessSubscriptionContainer: StorelessSubscriptionContainer,
        dispatchers: AppCoroutineDispatchers,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        syncStatusProvider: SpaceSyncAndP2PStatusProvider,
        getNetworkMode: GetNetworkMode,
        clearLastOpenedObject: ClearLastOpenedObject,
        fieldParser: FieldParser,
        dateProvider: DateProvider,
        spaceViews: SpaceViewSubscriptionContainer,
        urlHelper: UrlHelper,
        addDiscussion: AddDiscussion,
        getChatMessages: GetChatMessages,
        backHistoryDelegate: BackHistoryDelegate,
        exitToVaultDelegate: ExitToVaultDelegate,
        typeSuggestionEngine: TypeSuggestionEngine
    ): EditorViewModelFactory = EditorViewModelFactory(
        params = params,
        permissions = permissions,
        openPage = openPage,
        closeObject = closePage,
        createBlockLinkWithObject = createBlockLinkWithObject,
        createObjectAsMentionOrLink = createObjectAsMentionOrLink,
        interceptEvents = interceptEvents,
        updateLinkMarks = updateLinkMarks,
        removeLinkMark = removeLinkMark,
        documentEventReducer = documentExternalEventReducer,
        urlBuilder = urlBuilder,
        renderer = renderer,
        orchestrator = orchestrator,
        analytics = analytics,
        dispatcher = dispatcher,
        delegator = delegator,
        updateDetail = updateDetail,
        searchObjects = searchObjects,
        getDefaultObjectType = getDefaultObjectType,
        findObjectSetForType = findObjectSetForType,
        createObjectSet = createObjectSet,
        copyFileToCacheDirectory = copyFileToCacheDirectory,
        downloadUnsplashImage = downloadUnsplashImage,
        setDocCoverImage = setDocCoverImage,
        setDocImageIcon = setDocImageIcon,
        createObject = createObject,
        storeOfRelations = storeOfRelations,
        storeOfObjectTypes = storeOfObjectTypes,
        objectToSet = objectToSet,
        featureToggles = featureToggles,
        tableDelegate = tableDelegate,
        spaceManager = spaceManager,
        getObjectTypes = getObjectTypes,
        objectToCollection = objectToCollection,
        interceptFileLimitEvents = interceptFileLimitEvents,
        addRelationToObject = addRelationToObject,
        applyTemplate = applyTemplate,
        setObjectType = setObjectType,
        templatesContainer = templatesContainer,
        dispatchers = dispatchers,
        storelessSubscriptionContainer = storelessSubscriptionContainer,
        getNetworkMode = getNetworkMode,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        clearLastOpenedObject = clearLastOpenedObject,
        syncStatusProvider = syncStatusProvider,
        fieldParser = fieldParser,
        dateProvider = dateProvider,
        spaceViews = spaceViews,
        urlHelper = urlHelper,
        addDiscussion = addDiscussion,
        getChatMessages = getChatMessages,
        backHistoryDelegate = backHistoryDelegate,
        exitToVaultDelegate = exitToVaultDelegate,
        typeSuggestionEngine = typeSuggestionEngine
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBackHistoryDelegate(
        inspector: NavigationBackStackInspector,
        searchObjects: SearchObjects,
        fieldParser: FieldParser,
        dispatchers: AppCoroutineDispatchers
    ): BackHistoryDelegate = BackHistoryDelegate.Default(
        inspector = inspector,
        searchObjects = searchObjects,
        fieldParser = fieldParser,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideExitToVaultDelegate(
        spaceManager: SpaceManager,
        clearLastOpenedSpace: ClearLastOpenedSpace
    ): ExitToVaultDelegate = ExitToVaultDelegate.Default(
        spaceManager = spaceManager,
        clearLastOpenedSpace = clearLastOpenedSpace
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTableDelegate(
        dispatcher: Dispatcher<Payload>,
        createTableColumn: CreateTableColumn,
        createTableRow: CreateTableRow,
        deleteTableColumn: DeleteTableColumn,
        deleteTableRow: DeleteTableRow,
        duplicateTableRow: DuplicateTableRow,
        duplicateTableColumn: DuplicateTableColumn,
        fillTableRow: FillTableRow,
        fillTableColumn: FillTableColumn,
        moveTableRow: MoveTableRow,
        moveTableColumn: MoveTableColumn,
        setTableRowHeader: SetTableRowHeader
    ): EditorTableDelegate = DefaultEditorTableDelegate(
        dispatcher = dispatcher,
        createTableColumn = createTableColumn,
        createTableRow = createTableRow,
        deleteTableColumn = deleteTableColumn,
        deleteTableRow = deleteTableRow,
        duplicateTableColumn = duplicateTableColumn,
        duplicateTableRow = duplicateTableRow,
        fillTableColumn = fillTableColumn,
        fillTableRow = fillTableRow,
        moveTableColumn = moveTableColumn,
        moveTableRow = moveTableRow,
        setTableRowHeader = setTableRowHeader
    )

    @JvmStatic
    @Provides
    fun provideDefaultBlockViewRenderer(
        urlBuilder: UrlBuilder,
        toggleStateHolder: ToggleStateHolder,
        coverImageHashProvider: CoverImageHashProvider,
        storeOfRelations: StoreOfRelations,
        storeOfObjectTypes: StoreOfObjectTypes,
        fieldParser: FieldParser,
        resourceProvider: ResourceProvider
    ): DefaultBlockViewRenderer = DefaultBlockViewRenderer(
        urlBuilder = urlBuilder,
        toggleStateHolder = toggleStateHolder,
        coverImageHashProvider = coverImageHashProvider,
        storeOfRelations = storeOfRelations,
        storeOfObjectTypes = storeOfObjectTypes,
        fieldParser = fieldParser,
        resourceProvider = resourceProvider
    )

    @JvmStatic
    @Provides
    fun provideCoverImageHashProvider(): CoverImageHashProvider = DefaultCoverImageHashProvider()

    @JvmStatic
    @Provides
    fun provideDocumentExternalEventReducer(): DocumentExternalEventReducer =
        DocumentExternalEventReducer()

    @JvmStatic
    @Provides
    fun providePatternMatcher(): DefaultPatternMatcher = DefaultPatternMatcher()

    @JvmStatic
    @Provides
    fun provideSelectionStateHolder(): SelectionStateHolder = SelectionStateHolder.Default()

    @JvmStatic
    @Provides
    fun provideMemory(
        selectionStateHolder: SelectionStateHolder
    ): Editor.Memory = Editor.Memory(
        selections = selectionStateHolder
    )

    @JvmStatic
    @Provides
    fun provideInteractor(
        storage: Editor.Storage,
        proxer: Editor.Proxer,
        memory: Editor.Memory,
        createBlock: CreateBlock,
        replaceBlock: ReplaceBlock,
        duplicateBlock: DuplicateBlock,
        updateTextColor: UpdateTextColor,
        updateBackgroundColor: UpdateBackgroundColor,
        splitBlock: SplitBlock,
        mergeBlocks: MergeBlocks,
        unlinkBlocks: UnlinkBlocks,
        updateDivider: UpdateDivider,
        updateTextStyle: UpdateTextStyle,
        turnInto: TurnIntoStyle,
        updateCheckbox: UpdateCheckbox,
        downloadFile: DownloadFile,
        updateText: UpdateText,
        uploadBlock: UploadBlock,
        updateFields: UpdateFields,
        updateAlignment: UpdateAlignment,
        setupBookmark: SetupBookmark,
        createBookmarkBlock: CreateBookmarkBlock,
        turnIntoDocument: TurnIntoDocument,
        createTable: CreateTable,
        fillTableRow: FillTableRow,
        matcher: DefaultPatternMatcher,
        move: Move,
        copy: Copy,
        paste: Paste,
        undo: Undo,
        redo: Redo,
        setRelationKey: SetRelationKey,
        analytics: Analytics,
        updateBlocksMark: UpdateBlocksMark,
        documentFileShareDownloader: DocumentFileShareDownloader,
        clearBlockContent: ClearBlockContent,
        clearBlockStyle: ClearBlockStyle,
        analyticsSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        spaceManager: SpaceManager
    ): Orchestrator = Orchestrator(
        stores = storage,
        createBlock = createBlock,
        replaceBlock = replaceBlock,
        proxies = proxer,
        duplicateBlock = duplicateBlock,
        updateBackgroundColor = updateBackgroundColor,
        updateTextColor = updateTextColor,
        uploadBlock = uploadBlock,
        splitBlock = splitBlock,
        mergeBlocks = mergeBlocks,
        unlinkBlocks = unlinkBlocks,
        undo = undo,
        redo = redo,
        updateTextStyle = updateTextStyle,
        updateCheckbox = updateCheckbox,
        updateDivider = updateDivider,
        memory = memory,
        downloadFile = downloadFile,
        documentFileShareDownloader = documentFileShareDownloader,
        turnIntoDocument = turnIntoDocument,
        textInteractor = Interactor.TextInteractor(
            proxies = proxer,
            stores = storage,
            matcher = matcher
        ),
        updateText = updateText,
        updateAlignment = updateAlignment,
        setupBookmark = setupBookmark,
        createBookmarkBlock = createBookmarkBlock,
        move = move,
        paste = paste,
        copy = copy,
        setRelationKey = setRelationKey,
        analytics = analytics,
        updateFields = updateFields,
        turnIntoStyle = turnInto,
        updateBlocksMark = updateBlocksMark,
        createTable = createTable,
        fillTableRow = fillTableRow,
        clearBlockContent = clearBlockContent,
        clearBlockStyle = clearBlockStyle,
        analyticsSpaceHelperDelegate = analyticsSpaceHelperDelegate,
        spaceManager = spaceManager
    )
}

/**
 * These dependencies are stateless and therefore should be shared between different sessions of the same document.
 * In other words, if document A is opened twice (as A and A'), use-cases will be shared between all its sessions.
 * Consider the following navigation scenario: Document A > Document B > Document A'
 * The same documents have the same use-case dependencies, in order to avoid instance-creation overhead.
 */
@Module
object EditorUseCaseModule {

    @JvmStatic
    @Provides
    @PerScreen
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
    @PerScreen
    fun provideClosePageUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CloseObject = CloseObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateBlockUseCase(
        repo: BlockRepository
    ): UpdateText = UpdateText(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateBlockUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateBlock = CreateBlock(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideInterceptEventsUseCase(
        channel: EventChannel
    ): InterceptEvents = InterceptEvents(
        channel = channel,
        context = Dispatchers.IO
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateCheckboxUseCase(
        repo: BlockRepository
    ): UpdateCheckbox = UpdateCheckbox(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUnlinkBlocksUseCase(
        repo: BlockRepository
    ): UnlinkBlocks = UnlinkBlocks(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateBlockUseCase(
        repo: BlockRepository
    ): DuplicateBlock = DuplicateBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateDivider(
        repo: BlockRepository
    ): UpdateDivider = UpdateDivider(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideMergeBlocksUseCase(
        repo: BlockRepository
    ): MergeBlocks = MergeBlocks(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSplitBlockUseCase(
        repo: BlockRepository
    ): SplitBlock = SplitBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetRelationKeyUseCase(
        repo: BlockRepository
    ): SetRelationKey = SetRelationKey(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUploadUrl(
        repo: BlockRepository
    ): UploadBlock = UploadBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateBlocksMark(
        repo: BlockRepository
    ): UpdateBlocksMark = UpdateBlocksMark(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateLinkMarks(): UpdateLinkMarks = UpdateLinkMarks()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRemoveLinkMark(): RemoveLinkMark = RemoveLinkMark()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateTextStyleUseCase(
        repo: BlockRepository
    ): UpdateTextStyle = UpdateTextStyle(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateTextColorUseCase(
        repo: BlockRepository
    ): UpdateTextColor = UpdateTextColor(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateBackgroundColorUseCase(
        repo: BlockRepository
    ): UpdateBackgroundColor = UpdateBackgroundColor(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDownloadFileUseCase(
        downloader: Downloader
    ): DownloadFile = DownloadFile(
        downloader = downloader,
        context = Dispatchers.Main
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateObjectUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateBlockLinkWithObject =
        CreateBlockLinkWithObject(
            repo = repo,
            dispatchers = dispatchers
        )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateObjectAsMentionOrLink(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager
    ): CreateObjectAsMentionOrLink = CreateObjectAsMentionOrLink(
        repo = repo,
        dispatchers = dispatchers,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateObject(
        repo: BlockRepository
    ): DuplicateObject = DuplicateObject(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideReplaceBlockUseCase(
        repo: BlockRepository
    ): ReplaceBlock = ReplaceBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateTitleUseCase(
        repo: BlockRepository
    ): UpdateTitle = UpdateTitle(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateAlignmentUseCase(
        repo: BlockRepository
    ): UpdateAlignment = UpdateAlignment(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetupBookmarkUseCase(
        repo: BlockRepository
    ): SetupBookmark = SetupBookmark(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateBookmarkUseCase(
        repo: BlockRepository
    ): CreateBookmarkBlock = CreateBookmarkBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideClipboardPasteUseCase(
        repo: BlockRepository,
        clipboard: Clipboard,
        matcher: Clipboard.UriMatcher
    ): Paste = Paste(
        repo = repo,
        clipboard = clipboard,
        matcher = matcher
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyUseCase(
        repo: BlockRepository,
        clipboard: Clipboard
    ): Copy = Copy(
        repo = repo,
        clipboard = clipboard
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTurnIntoDocumentUseCase(
        repo: BlockRepository
    ): TurnIntoDocument = TurnIntoDocument(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateFieldsUseCase(
        repo: BlockRepository
    ): UpdateFields = UpdateFields(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetLinkAppearance(
        repo: BlockRepository
    ): SetLinkAppearance = SetLinkAppearance(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateTableUseCase(
        repo: BlockRepository
    ): CreateTable = CreateTable(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTableRowFill(
        repo: BlockRepository
    ): FillTableRow = FillTableRow(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectToSet(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): ConvertObjectToSet = ConvertObjectToSet(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideConvertObjectToCollection(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): ConvertObjectToCollection = ConvertObjectToCollection(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    @Named(INTRINSIC_PROVIDER_TYPE)
    fun provideDefaultObjectRelationProvider(
        storage: Editor.Storage,
        storeOfRelations: StoreOfRelations
    ): ObjectRelationProvider = DefaultObjectRelationProvider(
        storage = storage,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @PerScreen
    @Named(INTRINSIC_PROVIDER_TYPE)
    fun provideDefaultObjectValueProvider(
        storage: Editor.Storage
    ): ObjectValueProvider = DefaultObjectValueProvider(storage.details)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDelegator(): Delegator<Action> = Delegator.Default()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTurnIntoUseCase(repo: BlockRepository): TurnIntoStyle = TurnIntoStyle(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateDetailUseCase(
        repository: BlockRepository
    ): UpdateDetail = UpdateDetail(repository)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddFileToObjectUseCase(
        repo: BlockRepository
    ): AddFileToObject = AddFileToObject(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetObjectType(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectType = SetObjectType(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideGetDefaultPageType(
        repo: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = repo,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun findObjectSetForType(
        repo: BlockRepository
    ): FindObjectSetForType = FindObjectSetForType(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateObjectSetUseCase(
        repo: BlockRepository
    ): CreateObjectSet = CreateObjectSet(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyFileToCache(
        context: Context
    ): CopyFileToCacheDirectory = defaultCopyFileToCacheDirectory(context)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDownload(repo: UnsplashRepository): DownloadUnsplashImage = DownloadUnsplashImage(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocCoverImageUseCase(
        repo: BlockRepository
    ): SetDocCoverImage = SetDocCoverImage(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocumentImageIconUseCase(
        repo: BlockRepository
    ): SetDocumentImageIcon = SetDocumentImageIcon(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun applyTemplates(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): ApplyTemplate =
        ApplyTemplate(
            repo = repo,
            dispatchers = dispatchers
        )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddRelationToObject(repo: BlockRepository, dispatchers: AppCoroutineDispatchers) =
        AddRelationToObject(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun providesDocumentFileShareDownloader(
        repo: BlockRepository,
        context: Context,
        fileProvider: UriFileProvider,
        dispatchers: AppCoroutineDispatchers
    ): DocumentFileShareDownloader = DocumentFileShareDownloader(
        repo = repo,
        context = context.applicationContext,
        uriFileProvider = fileProvider,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun providesDebugTreeShareDownloader(
        repo: BlockRepository,
        context: Context,
        fileProvider: UriFileProvider,
        dispatchers: AppCoroutineDispatchers
    ): DebugTreeShareDownloader = DebugTreeShareDownloader(
        repo = repo,
        context = context.applicationContext,
        uriFileProvider = fileProvider,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockListClearContent(
        repo: BlockRepository
    ): ClearBlockContent = ClearBlockContent(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockListClearStyle(
        repo: BlockRepository
    ): ClearBlockStyle = ClearBlockStyle(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableCreateColumn(
        repo: BlockRepository
    ): CreateTableColumn = CreateTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableCreateRow(
        repo: BlockRepository
    ): CreateTableRow = CreateTableRow(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableDeleteRow(
        repo: BlockRepository
    ): DeleteTableRow = DeleteTableRow(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableDeleteColumn(
        repo: BlockRepository
    ): DeleteTableColumn = DeleteTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableDuplicateColumn(
        repo: BlockRepository
    ): DuplicateTableColumn = DuplicateTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableDuplicateRow(
        repo: BlockRepository
    ): DuplicateTableRow = DuplicateTableRow(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableFillColumn(
        repo: BlockRepository
    ): FillTableColumn = FillTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableMoveColumn(
        repo: BlockRepository
    ): MoveTableColumn = MoveTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableMoveRow(
        repo: BlockRepository
    ): MoveTableRow = MoveTableRow(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableSetHeaderRow(
        repo: BlockRepository
    ): SetTableRowHeader = SetTableRowHeader(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun getOptions(repo: BlockRepository) = GetOptions(repo)

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
}