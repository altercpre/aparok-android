package io.aparok.app.presentation.editor.editor

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.ObjectViewDetails
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.NetworkModeConfig
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.Response
import io.aparok.app.core_models.Struct
import io.aparok.app.core_models.StubObjectType
import io.aparok.app.core_models.StubSpaceView
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.multiplayer.SpaceUxType
import io.aparok.app.core_models.primitives.ParsedProperties
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeId
import io.aparok.app.core_models.primitives.TypeKey
import io.aparok.app.core_models.restrictions.ObjectRestriction
import io.aparok.app.domain.ai.TypeSuggestionEngine
import io.aparok.app.domain.auth.interactor.ClearLastOpenedObject
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.base.Result
import io.aparok.app.domain.base.Resultat
import io.aparok.app.domain.block.UpdateDivider
import io.aparok.app.domain.block.interactor.ClearBlockContent
import io.aparok.app.domain.block.interactor.ClearBlockStyle
import io.aparok.app.domain.block.interactor.CreateBlock
import io.aparok.app.domain.block.interactor.DuplicateBlock
import io.aparok.app.domain.block.interactor.MergeBlocks
import io.aparok.app.domain.block.interactor.Move
import io.aparok.app.domain.block.interactor.RemoveLinkMark
import io.aparok.app.domain.block.interactor.ReplaceBlock
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
import io.aparok.app.domain.clipboard.Copy
import io.aparok.app.domain.clipboard.Paste
import io.aparok.app.domain.config.Gateway
import io.aparok.app.domain.cover.SetDocCoverImage
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.download.DownloadFile
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.icon.SetDocumentImageIcon
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.misc.UrlBuilderImpl
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.networkmode.GetNetworkMode
import io.aparok.app.domain.`object`.ConvertObjectToCollection
import io.aparok.app.domain.`object`.ConvertObjectToSet
import io.aparok.app.domain.`object`.SetObjectInternalFlags
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.DefaultStoreOfObjectTypes
import io.aparok.app.domain.objects.DefaultStoreOfRelations
import io.aparok.app.domain.objects.GetDateObjectByTimestamp
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.page.CloseObject
import io.aparok.app.domain.page.CreateBlockLinkWithObject
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.page.CreateObjectAsMentionOrLink
import io.aparok.app.domain.page.OpenPage
import io.aparok.app.domain.page.Redo
import io.aparok.app.domain.page.Undo
import io.aparok.app.domain.page.bookmark.CreateBookmarkBlock
import io.aparok.app.domain.page.bookmark.SetupBookmark
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.primitives.FieldParserImpl
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.relations.SetRelationKey
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.SearchObjects
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
import io.aparok.app.domain.templates.GetTemplates
import io.aparok.app.domain.unsplash.DownloadUnsplashImage
import io.aparok.app.domain.unsplash.UnsplashRepository
import io.aparok.app.domain.workspace.InterceptFileLimitEvents
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.editor.DocumentExternalEventReducer
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.editor.EditorViewModel
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.editor.editor.pattern.DefaultPatternMatcher
import io.aparok.app.presentation.editor.editor.table.DefaultEditorTableDelegate
import io.aparok.app.presentation.editor.editor.table.EditorTableDelegate
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import io.aparok.app.presentation.editor.selection.SelectionStateHolder
import io.aparok.app.presentation.editor.toggle.ToggleStateHolder
import io.aparok.app.presentation.home.UserPermissionProviderStub
import io.aparok.app.presentation.templates.ObjectTypeTemplatesContainer
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.util.dispatchers
import io.aparok.app.presentation.util.downloader.DocumentFileShareDownloader
import io.aparok.app.presentation.widgets.collection.ResourceProvider
import io.aparok.app.test_utils.MockDataFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

open class EditorPresentationTestSetup {

    val root: Id = MockDataFactory.randomString()
    val defaultSpace: Id = MockDataFactory.randomUuid()

    @Mock
    lateinit var openPage: OpenPage

    @Mock
    lateinit var closePage: CloseObject

    @Mock
    lateinit var interceptEvents: InterceptEvents

    @Mock
    lateinit var createBlock: CreateBlock

    @Mock
    lateinit var updateText: UpdateText

    @Mock
    lateinit var updateCheckbox: UpdateCheckbox

    @Mock
    lateinit var duplicateBlock: DuplicateBlock

    @Mock
    lateinit var unlinkBlocks: UnlinkBlocks

    @Mock
    lateinit var updateTextStyle: UpdateTextStyle

    @Mock
    lateinit var updateTextColor: UpdateTextColor

    @Mock
    lateinit var clearLastOpenedObject: ClearLastOpenedObject

    @Mock
    lateinit var updateBlocksMark: UpdateBlocksMark

    @Mock
    lateinit var updateDivider: UpdateDivider

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var updateLinkMark: UpdateLinkMarks

    @Mock
    lateinit var removeLinkMark: RemoveLinkMark

    @Mock
    lateinit var mergeBlocks: MergeBlocks

    @Mock
    lateinit var splitBlock: SplitBlock

    @Mock
    lateinit var createBlockLinkWithObject: CreateBlockLinkWithObject

    @Mock
    lateinit var updateAlignment: UpdateAlignment

    @Mock
    lateinit var updateBackgroundColor: UpdateBackgroundColor

    @Mock
    lateinit var downloadFile: DownloadFile

    @Mock
    lateinit var documentFileShareDownloader: DocumentFileShareDownloader

    @Mock
    lateinit var uploadBlock: UploadBlock

    @Mock
    lateinit var updateFields: UpdateFields

    @Mock
    lateinit var paste: Paste

    @Mock
    lateinit var copy: Copy

    @Mock
    lateinit var getDateObjectByTimestamp: GetDateObjectByTimestamp

    @Mock
    lateinit var undo: Undo

    @Mock
    lateinit var redo: Redo

    @Mock
    lateinit var setupBookmark: SetupBookmark

    @Mock
    lateinit var resourceProvider: ResourceProvider

    @Mock
    lateinit var createBookmarkBlock: CreateBookmarkBlock

    @Mock
    lateinit var setRelationKey: SetRelationKey

    @Mock
    lateinit var createObjectAsMentionOrLink: CreateObjectAsMentionOrLink

    @Mock
    lateinit var replaceBlock: ReplaceBlock

    @Mock
    lateinit var move: Move

    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var turnIntoDocument: TurnIntoDocument

    @Mock
    lateinit var turnIntoStyle: TurnIntoStyle

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var unsplashRepo: UnsplashRepository

    @Mock
    lateinit var coverImageHashProvider: CoverImageHashProvider

    @Mock
    lateinit var setObjectType: SetObjectType

    @Mock
    lateinit var searchObjects: SearchObjects

    @Mock
    lateinit var applyTemplate: ApplyTemplate

    @Mock
    lateinit var getDefaultObjectType: GetDefaultObjectType

    @Mock
    lateinit var findObjectSetForType: FindObjectSetForType

    @Mock
    lateinit var createObjectSet: CreateObjectSet

    @Mock
    lateinit var copyFileToCacheDirectory: CopyFileToCacheDirectory

    @Mock
    lateinit var createObject: CreateObject

    @Mock
    lateinit var getTemplates: GetTemplates

    @Mock
    lateinit var createTable: CreateTable

    @Mock
    lateinit var fillTableRow: FillTableRow

    @Mock
    lateinit var spacedViews: SpaceViewSubscriptionContainer

    @Mock
    lateinit var convertObjectToCollection: ConvertObjectToCollection

    lateinit var tableDelegate: EditorTableDelegate

    lateinit var dispatcher: Dispatcher<Payload>

    @Mock
    lateinit var templatesContainer: ObjectTypeTemplatesContainer

    protected val builder: UrlBuilder get() = UrlBuilderImpl(gateway)

    private lateinit var updateDetail: UpdateDetail
    private lateinit var downloadUnsplashImage: DownloadUnsplashImage
    private lateinit var setDocCoverImage: SetDocCoverImage
    private lateinit var setDocImageIcon: SetDocumentImageIcon
    private lateinit var objectToSet: ConvertObjectToSet
    private lateinit var clearBlockContent: ClearBlockContent
    private lateinit var clearBlockStyle: ClearBlockStyle

    @Mock
    lateinit var createTableColumn: CreateTableColumn

    @Mock
    lateinit var createTableRow: CreateTableRow

    @Mock
    lateinit var deleteTableColumn: DeleteTableColumn

    @Mock
    lateinit var deleteTableRow: DeleteTableRow

    @Mock
    lateinit var duplicateTableRow: DuplicateTableRow

    @Mock
    lateinit var duplicateTableColumn: DuplicateTableColumn

    @Mock
    lateinit var fillTableColumn: FillTableColumn

    @Mock
    lateinit var moveTableRow: MoveTableRow

    @Mock
    lateinit var moveTableColumn: MoveTableColumn

    @Mock
    lateinit var addRelationToObject: AddRelationToObject

    @Mock
    lateinit var setTableRowHeader: SetTableRowHeader

    open lateinit var orchestrator: Orchestrator

    private val delegator = Delegator.Default<Action>()

    protected val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    protected val storeOfObjectTypes: StoreOfObjectTypes = DefaultStoreOfObjectTypes()

    @Mock
    lateinit var getObjectTypes: GetObjectTypes

    @Mock
    lateinit var interceptFileLimitEvents: InterceptFileLimitEvents

    @Mock
    lateinit var setObjectInternalFlags: SetObjectInternalFlags

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var storelessSubscriptionContainer: StorelessSubscriptionContainer

    @Mock
    lateinit var analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate

    @Mock
    lateinit var getNetworkMode: GetNetworkMode

    @Mock
    lateinit var spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var logger: Logger

    var permissions: UserPermissionProvider = UserPermissionProviderStub()

    val objTypeUniqueKey = "objTypeUniqueKey-${MockDataFactory.randomString()}"

    val objType = StubObjectType(
        id = MockDataFactory.randomUuid(),
        uniqueKey = objTypeUniqueKey,
    )

    open fun buildViewModel(urlBuilder: UrlBuilder = builder): EditorViewModel {

        runBlocking {
            storeOfObjectTypes.merge(
                types = listOf(objType)
            )
        }

        val storage = Editor.Storage()
        val proxies = Editor.Proxer()
        val memory = Editor.Memory(
            selections = SelectionStateHolder.Default()
        )
        objectToSet = ConvertObjectToSet(repo, dispatchers)
        updateDetail = UpdateDetail(repo)
        setDocCoverImage = SetDocCoverImage(repo)
        setDocImageIcon = SetDocumentImageIcon(repo)
        downloadUnsplashImage = DownloadUnsplashImage(unsplashRepo)
        clearBlockContent = ClearBlockContent(repo)
        clearBlockStyle = ClearBlockStyle(repo)

        orchestrator = Orchestrator(
            createBlock = createBlock,
            replaceBlock = replaceBlock,
            updateTextColor = updateTextColor,
            duplicateBlock = duplicateBlock,
            downloadFile = downloadFile,
            documentFileShareDownloader = documentFileShareDownloader,
            undo = undo,
            redo = redo,
            updateText = updateText,
            updateCheckbox = updateCheckbox,
            updateTextStyle = updateTextStyle,
            updateBackgroundColor = updateBackgroundColor,
            mergeBlocks = mergeBlocks,
            uploadBlock = uploadBlock,
            splitBlock = splitBlock,
            unlinkBlocks = unlinkBlocks,
            updateDivider = updateDivider,
            memory = memory,
            stores = storage,
            proxies = proxies,
            textInteractor = Interactor.TextInteractor(
                proxies = proxies,
                stores = storage,
                matcher = DefaultPatternMatcher()
            ),
            updateAlignment = updateAlignment,
            setupBookmark = setupBookmark,
            createBookmarkBlock = createBookmarkBlock,
            paste = paste,
            copy = copy,
            move = move,
            turnIntoDocument = turnIntoDocument,
            analytics = analytics,
            updateFields = updateFields,
            setRelationKey = setRelationKey,
            turnIntoStyle = turnIntoStyle,
            updateBlocksMark = updateBlocksMark,
            createTable = createTable,
            fillTableRow = fillTableRow,
            clearBlockContent = clearBlockContent,
            clearBlockStyle = clearBlockStyle,
            analyticsSpaceHelperDelegate = analyticSpaceHelperDelegate,
            spaceManager = spaceManager
        )

        dispatcher = Dispatcher.Default()

        tableDelegate = DefaultEditorTableDelegate(
            dispatcher, createTableColumn, createTableRow,
            deleteTableColumn, deleteTableRow, duplicateTableRow,
            duplicateTableColumn, fillTableRow, fillTableColumn,
            moveTableRow, moveTableColumn, setTableRowHeader
        )

        return EditorViewModel(
            openPage = openPage,
            closePage = closePage,
            createBlockLinkWithObject = createBlockLinkWithObject,
            createObjectAsMentionOrLink = createObjectAsMentionOrLink,
            interceptEvents = interceptEvents,
            updateLinkMarks = updateLinkMark,
            removeLinkMark = removeLinkMark,
            reducer = DocumentExternalEventReducer(),
            urlBuilder = urlBuilder,
            renderer = DefaultBlockViewRenderer(
                urlBuilder = urlBuilder,
                toggleStateHolder = ToggleStateHolder.Default(),
                coverImageHashProvider = coverImageHashProvider,
                storeOfRelations = storeOfRelations,
                storeOfObjectTypes = storeOfObjectTypes,
                fieldParser = fieldParser,
                resourceProvider = resourceProvider
            ),
            orchestrator = orchestrator,
            analytics = analytics,
            dispatcher = dispatcher,
            delegator = delegator,
            updateDetail = updateDetail,
            searchObjects = searchObjects,
            getDefaultObjectType = getDefaultObjectType,
            findObjectSetForType = findObjectSetForType,
            createObjectSet = createObjectSet,
            copyFileToCache = copyFileToCacheDirectory,
            downloadUnsplashImage = downloadUnsplashImage,
            setDocCoverImage = setDocCoverImage,
            setDocImageIcon = setDocImageIcon,
            createObject = createObject,
            objectToSet = objectToSet,
            objectToCollection = convertObjectToCollection,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            featureToggles = mock(),
            tableDelegate = tableDelegate,
            getObjectTypes = getObjectTypes,
            interceptFileLimitEvents = interceptFileLimitEvents,
            addRelationToObject = addRelationToObject,
            spaceManager = spaceManager,
            applyTemplate = applyTemplate,
            setObjectType = setObjectType,
            templatesContainer = templatesContainer,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            dispatchers = dispatchers,
            vmParams = EditorViewModel.Params(
                ctx = root,
                space = SpaceId(defaultSpace)
            ),
            permissions = permissions,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            clearLastOpenedObject = clearLastOpenedObject,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
            getNetworkMode = getNetworkMode,
            fieldParser = fieldParser,
            dateProvider = dateProvider,
            spaceViews = spacedViews,
            urlHelper = mock(),
            addDiscussion = mock(),
            getChatMessages = mock(),
            backHistoryDelegate = mock(),
            exitToVaultDelegate = mock(),
            typeSuggestionEngine = TypeSuggestionEngine.NoOp
        )
    }

    fun stubParsedProperties() {
        fieldParser.stub {
            onBlocking {
                getObjectParsedProperties(
                    objectType = any(),
                    objPropertiesKeys = any(),
                    storeOfRelations = any()
                )
            } doReturn ParsedProperties()
        }
    }

    fun stubGetNetworkMode() {
        getNetworkMode.stub {
            onBlocking { run(Unit) } doReturn NetworkModeConfig()
        }
    }

    fun stubOpenDocument(
        document: List<Block> = emptyList(),
        details: ObjectViewDetails = ObjectViewDetails(
            details = mapOf(
                root to mapOf(
                    Relations.ID to root,
                    Relations.TYPE to listOf(objType.id)
                )
            )
        ),
        objectRestrictions: List<ObjectRestriction> = emptyList(),
        spaceId: SpaceId = SpaceId(defaultSpace)
    ) {
        openPage.stub {
            onBlocking {
                async(
                    OpenPage.Params(
                        obj = root,
                        saveAsLastOpened = true,
                        space = spaceId
                    )
                )
            } doReturn Resultat.success(
                Result.Success(
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.ShowObject(
                                context = root,
                                root = root,
                                details = details.details,
                                blocks = document,
                                objectRestrictions = objectRestrictions
                            )
                        )
                    )
                )
            )
        }
    }

    fun stubMove() {
        move.stub {
            onBlocking { async(any()) } doReturn Resultat.Success(
                Payload(
                    context = root,
                    events = emptyList()
                )
            )
        }
    }

    fun stubInterceptEvents(
        params: InterceptEvents.Params = InterceptEvents.Params(context = root),
        flow: Flow<List<Event>> = flowOf(),
        stubInterceptThreadStatus: Boolean = true
    ) {
        interceptEvents.stub {
            onBlocking { build(params) } doReturn flow
        }
        if (stubInterceptThreadStatus) stubInterceptThreadStatus()
    }

    fun stubInterceptThreadStatus() {
        spaceSyncAndP2PStatusProvider.stub {
            onBlocking { observe() } doReturn emptyFlow()
        }
    }

    fun stubUpdateTextStyle(
        params: UpdateTextStyle.Params? = null,
        events: List<Event> = emptyList()
    ) {
        updateTextStyle.stub {
            onBlocking { invoke(params ?: any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = events
                )
            )
        }
    }

    fun stubTurnIntoStyle(
        params: TurnIntoStyle.Params? = null,
        events: List<Event> = emptyList()
    ) {
        turnIntoStyle.stub {
            onBlocking { invoke(params ?: any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = events
                )
            )
        }
    }

    fun stubUnlinkBlocks(
        params: UnlinkBlocks.Params? = null,
        events: List<Event> = emptyList()
    ) {
        unlinkBlocks.stub {
            onBlocking {
                if (params == null) invoke(any()) else invoke(params)
            } doReturn Either.Right(
                Payload(
                    context = root,
                    events = events
                )
            )
        }
    }

    fun stubCreateBlock(root: String) {
        createBlock.stub {
            onBlocking { async(any()) } doReturn Resultat.success(
                Pair(
                    MockDataFactory.randomString(), Payload(
                        context = root,
                        events = listOf()
                    )
                )
            )

        }
    }

    fun stubMergeBlocks(root: String) {
        mergeBlocks.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = emptyList()
                )
            )
        }
    }

    fun stubCreateBlockLinkWithObject(root: String, target: String) {
        createBlockLinkWithObject.stub {
            onBlocking {
                async(any())
            } doReturn Resultat.success(
                CreateBlockLinkWithObject.Result(
                    id = root,
                    objectId = target,
                    payload = Payload(
                        context = root,
                        events = emptyList()
                    ),
                    details = emptyMap()
                )
            )
        }
    }

    fun stubUpdateText() {
        updateText.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(Unit)
        }
    }

    fun stubCopy() {
        copy.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(Unit)
        }
    }

    fun stubPaste() {
        paste.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Response.Clipboard.Paste(
                    0,
                    true,
                    emptyList(),
                    Payload("", emptyList())
                )
            )
        }
    }


    fun stubReplaceBlock() {
        replaceBlock.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Pair(
                    MockDataFactory.randomUuid(),
                    Payload(context = root, events = emptyList())
                )
            )
        }
    }

    /**
     * Stubs [replaceBlock] with the full identity-fork swap payload: [forked]
     * is added, [empty] is deleted, and the root's children become [children].
     */
    fun stubReplaceBlockWithSwap(empty: Block, forked: Block, children: List<Id>) {
        replaceBlock.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Pair(
                    forked.id,
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.UpdateStructure(
                                context = root,
                                id = root,
                                children = children
                            ),
                            Event.Command.AddBlock(
                                context = root,
                                blocks = listOf(forked)
                            ),
                            Event.Command.DeleteBlock(
                                context = root,
                                targets = listOf(empty.id)
                            )
                        )
                    )
                )
            )
        }
    }

    /**
     * Stubs [createBlock] with the trailing-placeholder swap payload: [created]
     * is added and the root's children become [children].
     */
    fun stubCreateBlockWithSwap(created: Block, children: List<Id>) {
        createBlock.stub {
            onBlocking { async(any()) } doReturn Resultat.success(
                Pair(
                    created.id,
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.AddBlock(
                                context = root,
                                blocks = listOf(created)
                            ),
                            Event.Command.UpdateStructure(
                                context = root,
                                id = root,
                                children = children
                            )
                        )
                    )
                )
            )
        }
    }

    /**
     * Stubs [updateLinkMark] to append the new mark to the given marks — the
     * real use case's merge for a mark that overlaps nothing.
     */
    fun stubUpdateLinkMarksToAppend() {
        updateLinkMark.stub {
            on { invoke(any(), any(), any()) } doAnswer { invocation ->
                val params = invocation.getArgument<UpdateLinkMarks.Params>(1)
                val onResult = invocation
                    .getArgument<(Either<Throwable, List<Block.Content.Text.Mark>>) -> Unit>(2)
                onResult(Either.Right(params.marks + params.newMark))
            }
        }
    }

    fun stubSplitBlock() {
        splitBlock.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Pair(
                    MockDataFactory.randomUuid(),
                    Payload(context = root, events = emptyList())
                )
            )
        }
    }

    fun stubClosePage() {
        closePage.stub {
            onBlocking { async(any()) } doReturn Resultat.success(Unit)
        }
    }

    fun stubUpdateBackground() {
        updateBackgroundColor.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Payload(context = root, events = emptyList())
            )
        }
    }

    fun stubUpdateTextColor() {
        updateTextColor.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Payload(context = root, events = emptyList())
            )
        }
    }

    fun stubUpdateBlocksMark() {
        updateBlocksMark.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Payload(context = root, events = emptyList())
            )
        }
    }

    fun stubDuplicateBlock(
        new: Id,
        events: List<Event>
    ) {
        duplicateBlock.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Pair(
                    listOf(new),
                    Payload(
                        context = root,
                        events = events
                    )
                )
            )
        }
    }

    fun stubSearchObjects(objects: List<ObjectWrapper.Basic> = emptyList()) {
        searchObjects.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(objects)
        }
    }

    fun stubGetDefaultObjectType(
        id: String = MockDataFactory.randomString(),
        type: String = MockDataFactory.randomString(),
        name: String? = null,
        template: String? = null
    ) {
        getDefaultObjectType.stub {
            onBlocking { async(
                params = SpaceId(defaultSpace)
            ) } doReturn Resultat.success(
                GetDefaultObjectType.Response(
                    id = TypeId(id),
                    type = TypeKey(type),
                    name = name,
                    defaultTemplate = template
                )
            )
        }
    }

    fun stubCreateObjectAsMentionOrLink(name: String, id: String) {
        val result = CreateObjectAsMentionOrLink.Result(id, name)
        createObjectAsMentionOrLink.stub {
            onBlocking { async(any()) } doReturn Resultat.success(result)
        }
    }

    protected fun stubGetTemplates(
        templates: List<ObjectWrapper.Basic> = emptyList()
    ) {
        getTemplates.stub {
            onBlocking { run(any()) } doReturn templates
        }
    }

    fun stubFillRow() {
        fillTableRow.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = listOf()
                )
            )
        }
    }

    fun stubGetObjectTypes(types: List<ObjectWrapper.Type>) {
        getObjectTypes.stub {
            onBlocking {
                async(any())
            } doReturn Resultat.success(types)
        }
    }

    fun setStoreOfObjectTypes(types: List<ObjectWrapper.Type>) {
        runBlocking {
            storeOfObjectTypes.clear()
            storeOfObjectTypes.merge(types = types)
        }
    }

    fun stubFileLimitEvents() {
        interceptFileLimitEvents.stub {
            onBlocking { run(Unit) } doReturn emptyFlow()
        }
    }

    fun stubSetInternalFlags() {
        setObjectInternalFlags.stub {
            onBlocking { async(any()) } doReturn Resultat.success(Payload(root, emptyList()))
        }
    }

    fun stubSpaceManager(space: String = defaultSpace) {
        spaceManager.stub {
            onBlocking { get() } doReturn space
        }
    }

    fun stubUserPermission(
        space: SpaceId = SpaceId(defaultSpace),
        permission: SpaceMemberPermissions = SpaceMemberPermissions.OWNER
    ) {
        (permissions as UserPermissionProviderStub).stubObserve(
            space, permission
        )
    }

    fun stubAnalyticSpaceHelperDelegate() {
        Mockito.`when`(analyticSpaceHelperDelegate.provideParams(defaultSpace))
            .thenReturn(AnalyticSpaceHelperDelegate.Params.EMPTY)

        analyticSpaceHelperDelegate.stub {
            on { provideParams(defaultSpace) } doReturn AnalyticSpaceHelperDelegate.Params.EMPTY
        }
    }

    @Mock
    lateinit var stringResourceProvider: StringResourceProvider

    fun proceedWithDefaultBeforeTestStubbing() {
        fieldParser = FieldParserImpl(dateProvider, logger, getDateObjectByTimestamp, stringResourceProvider)
        stubAnalyticSpaceHelperDelegate()
        stubSpaceManager()
        stubUserPermission()
        stubGetNetworkMode()
        stubFileLimitEvents()
        stubInterceptEvents()
        stubClosePage()
        stubInterceptThreadStatus()
        val spaceView = StubSpaceView(
            id = defaultSpace,
            spaceUxType = SpaceUxType.DATA
        )
        spacedViews.stub {
            onBlocking { get(space = SpaceId(defaultSpace)) } doReturn spaceView
        }
    }
}