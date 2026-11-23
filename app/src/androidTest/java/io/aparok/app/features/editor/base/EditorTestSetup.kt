package io.aparok.app.features.editor.base

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import io.aparok.app.R
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.app.DefaultFeatureToggles
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectViewDetails
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Relation
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.domain.auth.interactor.ClearLastOpenedObject
import io.aparok.app.domain.base.AppCoroutineDispatchers
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
import io.aparok.app.domain.clipboard.Clipboard
import io.aparok.app.domain.clipboard.Copy
import io.aparok.app.domain.clipboard.Paste
import io.aparok.app.domain.config.Gateway
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.cover.RemoveDocCover
import io.aparok.app.domain.cover.SetDocCoverImage
import io.aparok.app.domain.download.DownloadFile
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.icon.SetDocumentImageIcon
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.misc.UrlBuilderImpl
import io.aparok.app.presentation.util.UrlHelper
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.networkmode.GetNetworkMode
import io.aparok.app.domain.`object`.ConvertObjectToCollection
import io.aparok.app.domain.`object`.ConvertObjectToSet
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.DefaultStoreOfObjectTypes
import io.aparok.app.domain.objects.DefaultStoreOfRelations
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
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.relations.SetRelationKey
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.sets.FindObjectSetForType
import io.aparok.app.domain.table.CreateTable
import io.aparok.app.domain.table.FillTableRow
import io.aparok.app.domain.templates.ApplyTemplate
import io.aparok.app.domain.templates.GetTemplates
import io.aparok.app.domain.unsplash.DownloadUnsplashImage
import io.aparok.app.domain.unsplash.UnsplashRepository
import io.aparok.app.domain.workspace.FileLimitsEventChannel
import io.aparok.app.domain.workspace.InterceptFileLimitEvents
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.editor.DocumentExternalEventReducer
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.editor.EditorViewModel
import io.aparok.app.presentation.editor.EditorViewModelFactory
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.editor.editor.Interactor
import io.aparok.app.presentation.editor.editor.Orchestrator
import io.aparok.app.presentation.editor.editor.Proxy
import io.aparok.app.presentation.editor.editor.pattern.DefaultPatternMatcher
import io.aparok.app.presentation.editor.editor.table.EditorTableDelegate
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import io.aparok.app.presentation.editor.selection.SelectionStateHolder
import io.aparok.app.presentation.editor.toggle.ToggleStateHolder
import io.aparok.app.presentation.search.ObjectSearchConstants
import io.aparok.app.presentation.templates.ObjectTypeTemplatesContainer
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.util.downloader.DocumentFileShareDownloader
import io.aparok.app.presentation.widgets.collection.ResourceProvider
import io.aparok.app.test_utils.MockDataFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

open class EditorTestSetup {

    lateinit var createBlockLinkWithObject: CreateBlockLinkWithObject
    lateinit var downloadFile: DownloadFile
    lateinit var undo: Undo
    lateinit var redo: Redo
    lateinit var copy: Copy
    lateinit var paste: Paste
    lateinit var updateAlignment: UpdateAlignment
    lateinit var replaceBlock: ReplaceBlock
    lateinit var setupBookmark: SetupBookmark
    lateinit var createBookmarkBlock: CreateBookmarkBlock
    lateinit var uploadBlock: UploadBlock
    lateinit var splitBlock: SplitBlock
    lateinit var updateBackgroundColor: UpdateBackgroundColor
    lateinit var move: Move
    lateinit var setRelationKey: SetRelationKey
    lateinit var updateDetail: UpdateDetail

    lateinit var copyFileToCacheDirectory: CopyFileToCacheDirectory

    @Mock
    lateinit var documentFileShareDownloader: DocumentFileShareDownloader

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var openPage: OpenPage

    @Mock
    lateinit var closePage: CloseObject

    @Mock
    lateinit var updateText: UpdateText

    @Mock
    lateinit var createBlock: CreateBlock

    @Mock
    lateinit var interceptEvents: InterceptEvents

    @Mock
    lateinit var updateCheckbox: UpdateCheckbox

    @Mock
    lateinit var unlinkBlocks: UnlinkBlocks

    lateinit var getSearchObjects: SearchObjects

    @Mock
    lateinit var duplicateBlock: DuplicateBlock

    @Mock
    lateinit var updateTextStyle: UpdateTextStyle

    @Mock
    lateinit var updateTextColor: UpdateTextColor

    @Mock
    lateinit var updateLinkMarks: UpdateLinkMarks

    @Mock
    lateinit var removeLinkMark: RemoveLinkMark

    @Mock
    lateinit var mergeBlocks: MergeBlocks

    @Mock
    lateinit var createObject: CreateObject

    @Mock
    lateinit var appCoroutineDispatchers: AppCoroutineDispatchers

    @Mock
    lateinit var storelessSubscriptionContainer: StorelessSubscriptionContainer

    lateinit var getTemplates: GetTemplates
    lateinit var applyTemplate: ApplyTemplate

    lateinit var createObjectAsMentionOrLink: CreateObjectAsMentionOrLink

    lateinit var setDocCoverImage: SetDocCoverImage
    lateinit var setDocImageIcon: SetDocumentImageIcon
    lateinit var removeDocCover: RemoveDocCover

    lateinit var updateFields: UpdateFields
    lateinit var turnIntoDocument: TurnIntoDocument
    lateinit var turnIntoStyle: TurnIntoStyle
    lateinit var setObjectType: SetObjectType
    lateinit var objectToSet: ConvertObjectToSet
    lateinit var clearBlockContent: ClearBlockContent
    lateinit var clearBlockStyle: ClearBlockStyle

    lateinit var getDefaultObjectType: GetDefaultObjectType

    private lateinit var findObjectSetForType: FindObjectSetForType
    private lateinit var createObjectSet: CreateObjectSet
    lateinit var getObjectTypes: GetObjectTypes

    lateinit var downloadUnsplashImage: DownloadUnsplashImage

    lateinit var featureToggles: FeatureToggles

    @Mock
    lateinit var updateDivider: UpdateDivider

    @Mock
    lateinit var clearLastOpenedObject: ClearLastOpenedObject

    @Mock
    lateinit var uriMatcher: Clipboard.UriMatcher

    @Mock
    lateinit var updateBlocksMark: UpdateBlocksMark

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var getNetworkMode: GetNetworkMode

    @Mock
    lateinit var unsplashRepository: UnsplashRepository

    @Mock
    lateinit var userSettingsRepository: UserSettingsRepository

    @Mock
    lateinit var coverImageHashProvider: CoverImageHashProvider

    @Mock
    lateinit var clipboard: Clipboard

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var createTable: CreateTable

    @Mock
    lateinit var fillTableRow: FillTableRow

    @Mock
    lateinit var tableDelegate: EditorTableDelegate

    @Mock
    lateinit var objectToCollection: ConvertObjectToCollection

    @Mock
    lateinit var fileLimitsEventChannel: FileLimitsEventChannel

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var templatesContainer: ObjectTypeTemplatesContainer

    @Mock
    lateinit var permissions: UserPermissionProvider

    @Mock
    lateinit var analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate

    @Mock
    lateinit var spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider

    @Mock
    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var resourceProvider: ResourceProvider

    lateinit var interceptFileLimitEvents: InterceptFileLimitEvents

    lateinit var addRelationToObject: AddRelationToObject

    val root: String = "rootId123"
    val defaultSpace = MockDataFactory.randomString()


    @Mock
    lateinit var urlHelper: UrlHelper

    private val urlBuilder by lazy {
        UrlBuilderImpl(gateway = gateway)
    }

    private val intents = Proxy.Intents()

    private val stores = Editor.Storage()

    private val proxies = Editor.Proxer(
        intents = intents
    )

    @Mock
    lateinit var spacedViews: SpaceViewSubscriptionContainer

    private val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    private val storeOfObjectTypes: StoreOfObjectTypes = DefaultStoreOfObjectTypes()

    open fun setup() {
        MockitoAnnotations.openMocks(this)

        val dispatchers = AppCoroutineDispatchers(
            io = StandardTestDispatcher(),
            main = StandardTestDispatcher(),
            computation = StandardTestDispatcher()
        )

        splitBlock = SplitBlock(repo)
        undo = Undo(repo, dispatchers)
        redo = Redo(repo, dispatchers)
        objectToSet = ConvertObjectToSet(repo, dispatchers)
        replaceBlock = ReplaceBlock(repo)
        setupBookmark = SetupBookmark(repo)
        updateAlignment = UpdateAlignment(repo)
        uploadBlock = UploadBlock(repo)
        createBlockLinkWithObject = CreateBlockLinkWithObject(repo, dispatchers)
        setRelationKey = SetRelationKey(repo)
        turnIntoDocument = TurnIntoDocument(repo)
        updateFields = UpdateFields(repo)
        setObjectType = SetObjectType(repo, dispatchers)
        createObjectAsMentionOrLink = CreateObjectAsMentionOrLink(
            repo = repo,
            dispatchers = dispatchers,
            spaceManager = spaceManager
        )
        getSearchObjects = SearchObjects(repo)
        downloadUnsplashImage = DownloadUnsplashImage(unsplashRepository)
        clearBlockContent = ClearBlockContent(repo)
        clearBlockStyle = ClearBlockStyle(repo)
        downloadFile = DownloadFile(
            downloader = mock(),
            context = Dispatchers.Main
        )
        copy = Copy(
            repo = repo,
            clipboard = clipboard
        )

        paste = Paste(
            repo = repo,
            clipboard = clipboard,
            matcher = uriMatcher
        )
        move = Move(
            repo, dispatchers
        )
        getObjectTypes = GetObjectTypes(repo, dispatchers)

        updateBackgroundColor = UpdateBackgroundColor(repo)

        setDocCoverImage = SetDocCoverImage(repo)
        setDocImageIcon = SetDocumentImageIcon(repo)
        removeDocCover = RemoveDocCover(repo)
        turnIntoStyle = TurnIntoStyle(repo)
        updateDetail = UpdateDetail(repo)
        getDefaultObjectType = GetDefaultObjectType(
            userSettingsRepository = userSettingsRepository,
            blockRepository = repo,
            dispatchers = dispatchers
        )
        createObjectSet = CreateObjectSet(repo)
        findObjectSetForType = FindObjectSetForType(repo)
        createBookmarkBlock = CreateBookmarkBlock(repo)
        applyTemplate = ApplyTemplate(
            repo = repo,
            dispatchers = dispatchers
        )
        getTemplates = GetTemplates(
            repo = repo,
            dispatchers = dispatchers,
            spaceManager = spaceManager
        )

        featureToggles = mock<DefaultFeatureToggles>()
        addRelationToObject = AddRelationToObject(repo, dispatchers)

        interceptFileLimitEvents = InterceptFileLimitEvents(fileLimitsEventChannel, dispatchers)

        TestEditorFragment.testViewModelFactory = EditorViewModelFactory(
            openPage = openPage,
            closeObject = closePage,
            interceptEvents = interceptEvents,
            updateLinkMarks = updateLinkMarks,
            removeLinkMark = removeLinkMark,
            createBlockLinkWithObject = createBlockLinkWithObject,
            documentEventReducer = DocumentExternalEventReducer(),
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
            orchestrator = Orchestrator(
                createBlock = createBlock,
                splitBlock = splitBlock,
                unlinkBlocks = unlinkBlocks,
                updateCheckbox = updateCheckbox,
                updateTextStyle = updateTextStyle,
                updateText = updateText,
                updateBackgroundColor = updateBackgroundColor,
                undo = undo,
                redo = redo,
                copy = copy,
                paste = paste,
                duplicateBlock = duplicateBlock,
                updateAlignment = updateAlignment,
                downloadFile = downloadFile,
                documentFileShareDownloader = documentFileShareDownloader,
                mergeBlocks = mergeBlocks,
                updateTextColor = updateTextColor,
                replaceBlock = replaceBlock,
                setupBookmark = setupBookmark,
                setRelationKey = setRelationKey,
                memory = Editor.Memory(
                    selections = SelectionStateHolder.Default()
                ),
                stores = stores,
                proxies = proxies,
                textInteractor = Interactor.TextInteractor(
                    proxies = proxies,
                    stores = stores,
                    matcher = DefaultPatternMatcher()
                ),
                uploadBlock = uploadBlock,
                move = move,
                analytics = analytics,
                updateDivider = updateDivider,
                updateFields = updateFields,
                turnIntoDocument = turnIntoDocument,
                turnIntoStyle = turnIntoStyle,
                updateBlocksMark = updateBlocksMark,
                createBookmarkBlock = createBookmarkBlock,
                createTable = createTable,
                fillTableRow = fillTableRow,
                clearBlockContent = clearBlockContent,
                clearBlockStyle = clearBlockStyle,
                analyticsSpaceHelperDelegate = analyticSpaceHelperDelegate,
                spaceManager = spaceManager
            ),
            createObjectAsMentionOrLink = createObjectAsMentionOrLink,
            analytics = analytics,
            dispatcher = Dispatcher.Default(),
            updateDetail = updateDetail,
            searchObjects = getSearchObjects,
            getDefaultObjectType = getDefaultObjectType,
            createObjectSet = createObjectSet,
            findObjectSetForType = findObjectSetForType,
            copyFileToCacheDirectory = copyFileToCacheDirectory,
            downloadUnsplashImage = downloadUnsplashImage,
            delegator = Delegator.Default(),
            setDocCoverImage = setDocCoverImage,
            setDocImageIcon = setDocImageIcon,
            createObject = createObject,
            objectToSet = objectToSet,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            featureToggles = featureToggles,
            tableDelegate = tableDelegate,
            getObjectTypes = getObjectTypes,
            objectToCollection = objectToCollection,
            interceptFileLimitEvents = interceptFileLimitEvents,
            addRelationToObject = addRelationToObject,
            spaceManager = spaceManager,
            applyTemplate = applyTemplate,
            setObjectType = setObjectType,
            templatesContainer = templatesContainer,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            dispatchers = appCoroutineDispatchers,
            params = EditorViewModel.Params(
                ctx = root,
                space = SpaceId(defaultSpace)
            ),
            permissions = permissions,
            syncStatusProvider = spaceSyncAndP2PStatusProvider,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            clearLastOpenedObject = clearLastOpenedObject,
            getNetworkMode = getNetworkMode,
            fieldParser = fieldParser,
            dateProvider = dateProvider,
            spaceViews = spacedViews,
            urlHelper = urlHelper,
            addDiscussion = mock(),
            getChatMessages = mock(),
            backHistoryDelegate = mock(),
            exitToVaultDelegate = mock()
        )
    }

    /**
     * STUBBING
     */

    fun stubInterceptEvents() {
        interceptEvents.stub {
            onBlocking { build(any()) } doReturn emptyFlow()
        }
    }

    fun stubInterceptThreadStatus() {
        spaceSyncAndP2PStatusProvider.stub {
            onBlocking { observe() } doReturn emptyFlow()
        }
    }

    fun stubOpenDocument(
        document: List<Block>,
        details: ObjectViewDetails = ObjectViewDetails.EMPTY,
        relations: List<Relation> = emptyList()
    ) {
        openPage.stub {
            onBlocking { execute(any()) } doReturn Resultat.success(
                Result.Success(
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.ShowObject(
                                context = root,
                                root = root,
                                details = details.details,
                                blocks = document,
                            )
                        )
                    )
                )
            )
        }
    }

    fun stubCreateBlock(
        params: CreateBlock.Params,
        events: List<Event.Command>
    ) {
        createBlock.stub {
            onBlocking { execute(params) } doReturn Resultat.success(
                Pair(
                    MockDataFactory.randomUuid(),
                    Payload(context = root, events = events)
                )
            )
        }
    }

    fun stubSplitBlocks(
        command: Command.Split,
        new: Id,
        events: List<Event.Command>
    ) {
        repo.stub {
            onBlocking {
                split(command = command)
            } doReturn Pair(new, Payload(context = root, events = events))
        }
    }

    fun stubUpdateText() {
        updateText.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(Unit)
        }
    }

    fun stubGetObjectTypes(objectTypes: List<ObjectWrapper.Type>) {
        repo.stub {
            onBlocking {
                searchObjects(
                    space = SpaceId(defaultSpace),
                    filters = ObjectSearchConstants.filterTypes(),
                    keys = ObjectSearchConstants.defaultKeysObjectType,
                    sorts = emptyList(),
                    limit = 0,
                    offset = 0,
                    fulltext = ""
                )
            } doReturn objectTypes.map { it.map }
        }
    }

    fun stubUpdateTextStyle(
        events: List<Event> = emptyList()
    ) {
        updateTextStyle.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = events
                )
            )
        }
    }

    fun stubAnalytics() {
        analytics.stub {
            onBlocking { registerEvent(any()) } doReturn Unit
        }
    }

    fun launch(args: Bundle): FragmentScenario<TestEditorFragment> {
        return launchFragmentInContainer(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }
}