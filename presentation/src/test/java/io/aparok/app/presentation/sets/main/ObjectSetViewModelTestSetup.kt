package io.aparok.app.presentation.sets.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Block.Content.DataView.Sort
import io.aparok.app.core_models.Config
import io.aparok.app.core_models.DVSort
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.NetworkModeConfig
import io.aparok.app.core_models.ObjectViewDetails
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_models.RelationLink
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.SearchResult
import io.aparok.app.core_models.StubConfig
import io.aparok.app.core_models.StubSpaceView
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.multiplayer.SpaceUxType
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeId
import io.aparok.app.core_models.primitives.TypeKey
import io.aparok.app.core_models.restrictions.DataViewRestrictions
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.base.Result
import io.aparok.app.domain.base.Resultat
import io.aparok.app.domain.block.interactor.CreateBlock
import io.aparok.app.domain.block.interactor.UpdateText
import io.aparok.app.domain.block.interactor.sets.GetObjectTypes
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.collections.AddObjectToCollection
import io.aparok.app.domain.collections.RemoveObjectFromCollection
import io.aparok.app.domain.config.Gateway
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.cover.SetDocCoverImage
import io.aparok.app.domain.dataview.SetDataViewProperties
import io.aparok.app.domain.dataview.interactor.SetDataViewObjectOrder
import io.aparok.app.domain.library.StoreSearchParams
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.search.BoardGroupSubscriptionContainer
import io.aparok.app.domain.search.BoardRecordsSubscriptionContainer
import io.aparok.app.domain.dataview.interactor.CreateDataViewObject
import io.aparok.app.domain.discussions.AddDiscussion
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.misc.UrlBuilderImpl
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.networkmode.GetNetworkMode
import io.aparok.app.domain.`object`.ConvertObjectToCollection
import io.aparok.app.domain.`object`.DuplicateObjects
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.DefaultObjectStore
import io.aparok.app.domain.objects.DefaultStoreOfObjectTypes
import io.aparok.app.domain.objects.DefaultStoreOfRelations
import io.aparok.app.domain.objects.GetDateObjectByTimestamp
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.page.CloseObject
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.primitives.FieldParserImpl
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.DataViewSubscriptionContainer
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.sets.OpenObjectSet
import io.aparok.app.domain.sets.SetQueryToObjectSet
import io.aparok.app.domain.templates.CreateTemplate
import io.aparok.app.domain.unsplash.DownloadUnsplashImage
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.emojifier.data.EmojiProvider
import io.aparok.app.emojifier.suggest.EmojiSuggester
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.collections.MockCollection
import io.aparok.app.presentation.collections.MockSet
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.home.UserPermissionProviderStub
import io.aparok.app.presentation.relations.ObjectSetConfig
import io.aparok.app.presentation.search.ObjectSearchConstants
import io.aparok.app.presentation.sets.ObjectSetDatabase
import io.aparok.app.presentation.sets.ObjectSetPaginator
import io.aparok.app.presentation.sets.ObjectSetSession
import io.aparok.app.presentation.sets.ObjectSetViewModel
import io.aparok.app.presentation.sets.state.DefaultObjectStateReducer
import io.aparok.app.presentation.sets.subscription.DataViewSubscription
import io.aparok.app.presentation.sets.subscription.DefaultDataViewSubscription
import io.aparok.app.presentation.sets.subscription.updateWithRelationFormat
import io.aparok.app.presentation.sets.updateFormatForSubscription
import io.aparok.app.presentation.sets.viewer.ViewerDelegate
import io.aparok.app.presentation.templates.ObjectTypeTemplatesContainer
import io.aparok.app.presentation.util.DefaultCoroutineTestRule
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.test_utils.MockDataFactory
import java.util.Locale
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.bytebuddy.utility.RandomString
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

@OptIn(ExperimentalCoroutinesApi::class)
open class ObjectSetViewModelTestSetup {

    val root: Id = "context-${RandomString.make()}"
    val defaultSpace = "DEFAULT_SPACE_ID"

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val rule = DefaultCoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var openObjectSet: OpenObjectSet

    @Mock
    lateinit var closeObject: CloseObject

    @Mock
    lateinit var updateText: UpdateText

    @Mock
    lateinit var createDataViewObject: CreateDataViewObject

    @Mock
    lateinit var interceptEvents: InterceptEvents

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var downloadUnsplashImage: DownloadUnsplashImage

    @Mock
    lateinit var setDocCoverImage: SetDocCoverImage

    @Mock
    lateinit var createObject: CreateObject

    @Mock
    lateinit var objectToCollection: ConvertObjectToCollection

    @Mock
    lateinit var setQueryToObjectSet: SetQueryToObjectSet

    @Mock
    lateinit var setObjectDetails: UpdateDetail

    //@Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var coverImageHashProvider: CoverImageHashProvider

    @Mock
    lateinit var subscriptionEventChannel: SubscriptionEventChannel

    @Mock
    lateinit var removeObjectFromCollection: RemoveObjectFromCollection

    @Mock
    lateinit var addObjectToCollection: AddObjectToCollection

    lateinit var storeOfObjectTypes: StoreOfObjectTypes

    @Mock
    lateinit var getObjectTypes: GetObjectTypes

    @Mock
    lateinit var getDefaultObjectType: GetDefaultObjectType

    @Mock
    lateinit var duplicateObjects: DuplicateObjects

    @Mock
    lateinit var setObjectListIsArchived: SetObjectListIsArchived

    @Mock
    lateinit var templatesContainer: ObjectTypeTemplatesContainer

    @Mock
    lateinit var addDiscussion: AddDiscussion

    @Mock
    lateinit var viewerDelegate: ViewerDelegate

    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var createTemplate: CreateTemplate

    @Mock
    lateinit var getNetworkMode: GetNetworkMode

    @Mock
    lateinit var analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate

    @Mock
    lateinit var spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider

    @Mock
    lateinit var localeProvider : LocaleProvider

    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var logger: Logger

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var spacedViews: SpaceViewSubscriptionContainer

    @Mock
    lateinit var deepLinkResolver: DeepLinkResolver

    @Mock
    lateinit var setDataViewProperties: SetDataViewProperties

    @Mock
    lateinit var setDataViewObjectOrder: SetDataViewObjectOrder

    @Mock
    lateinit var boardGroupSubscriptionContainer: BoardGroupSubscriptionContainer

    @Mock
    lateinit var boardRecordsSubscriptionContainer: BoardRecordsSubscriptionContainer

    @Mock
    lateinit var storelessSubscriptionContainer: StorelessSubscriptionContainer

    @Mock
    lateinit var userSettingsRepository: UserSettingsRepository

    @Mock
    lateinit var emojiProvider: EmojiProvider

    @Mock
    lateinit var emojiSuggester: EmojiSuggester

    @Mock
    lateinit var createBlock: CreateBlock

    var permissions: UserPermissionProvider = UserPermissionProviderStub()

    lateinit var spaceConfig: Config

    var stateReducer = DefaultObjectStateReducer()

    lateinit var dataViewSubscriptionContainer: DataViewSubscriptionContainer
    lateinit var dataViewSubscription: DataViewSubscription

    val dispatcher = Dispatcher.Default<Payload>()
    val delegator = Delegator.Default<Action>()
    val session = ObjectSetSession()
    val paginator = ObjectSetPaginator()

    val objectStore: ObjectStore = DefaultObjectStore()
    protected val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    val database = ObjectSetDatabase(objectStore)

    val urlBuilder: UrlBuilder get() = UrlBuilderImpl(gateway)

    val defaultObjectPageType = TypeKey(MockDataFactory.randomString())
    val defaultObjectPageTypeName = MockDataFactory.randomString()

    lateinit var dispatchers: AppCoroutineDispatchers

    @Mock
    lateinit var getDateObjectByTimestamp: GetDateObjectByTimestamp

    @Mock
    lateinit var stringResourceProvider: StringResourceProvider

    fun proceedWithDefaultBeforeTestStubbing() = runTest {
        repo = mock(verboseLogging = true)
        dispatchers = AppCoroutineDispatchers(
            io = rule.dispatcher,
            computation = rule.dispatcher,
            main = rule.dispatcher
        )
        spaceConfig = StubConfig()
        spaceManager = SpaceManager.Impl(
            repo = repo,
            dispatchers = dispatchers,
            logger = logger
        )
        stubSpaceManager(defaultSpace)
        dataViewSubscriptionContainer = DataViewSubscriptionContainer(
            repo = repo,
            channel = subscriptionEventChannel,
            store = objectStore,
            dispatchers = dispatchers
        )
        dataViewSubscription = DefaultDataViewSubscription(dataViewSubscriptionContainer)
        storeOfObjectTypes = DefaultStoreOfObjectTypes()
        stubLocalProvider()
        fieldParser = FieldParserImpl(dateProvider, logger, getDateObjectByTimestamp, stringResourceProvider)
        stubGetDefaultPageType()
        stubObservePermissions()
        stubAnalyticSpaceHelperDelegate()
        stubInterceptEvents()
        stubNetworkMode()
        givenNetworkNodeMocked()
        stubInterceptThreadStatus()
        val spaceView = StubSpaceView(
            id = defaultSpace,
            spaceUxType = SpaceUxType.DATA
        )
        spacedViews.stub {
            onBlocking { get(space = SpaceId(defaultSpace)) } doReturn spaceView
        }
        // Kanban is gated behind an experimental flag; default the tests to enabled so the
        // board-specific tests exercise the live board. Tests that need it off override this.
        userSettingsRepository.stub {
            on { observeKanbanEnabled() } doReturn flowOf(true)
        }
        // Board option headers subscribe through the storeless container; default to no
        // options so board tests render (tests exercising labels override this).
        storelessSubscriptionContainer.stub {
            on { subscribe(any<StoreSearchParams>()) } doReturn flowOf(emptyList())
        }
    }

    fun givenViewModel(): ObjectSetViewModel {
        return ObjectSetViewModel(
            openObjectSet = openObjectSet,
            closeObject = closeObject,
            updateText = updateText,
            interceptEvents = interceptEvents,
            createDataViewObject = createDataViewObject,
            dispatcher = dispatcher,
            delegator = delegator,
            coverImageHashProvider = coverImageHashProvider,
            urlBuilder = urlBuilder,
            session = session,
            analytics = analytics,
            downloadUnsplashImage = downloadUnsplashImage,
            setDocCoverImage = setDocCoverImage,
            createObject = createObject,
            setObjectDetails = setObjectDetails,
            paginator = paginator,
            database = database,
            dataViewSubscriptionContainer = dataViewSubscriptionContainer,
            storeOfRelations = storeOfRelations,
            stateReducer = stateReducer,
            dataViewSubscription = dataViewSubscription,
            objectStore = objectStore,
            addObjectToCollection = addObjectToCollection,
            objectToCollection = objectToCollection,
            setQueryToObjectSet = setQueryToObjectSet,
            storeOfObjectTypes = storeOfObjectTypes,
            templatesContainer = templatesContainer,
            setObjectListIsArchived = setObjectListIsArchived,
            duplicateObjects = duplicateObjects,
            viewerDelegate = viewerDelegate,
            spaceManager = spaceManager,
            createTemplate = createTemplate,
            dateProvider = dateProvider,
            vmParams = ObjectSetViewModel.Params(
                ctx = root,
                space = SpaceId(defaultSpace)
            ),
            permissions = permissions,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
            fieldParser = fieldParser,
            spaceViews = spacedViews,
            deepLinkResolver = deepLinkResolver,
            removeObjectFromCollection = removeObjectFromCollection,
            setDataViewProperties = setDataViewProperties,
            setDataViewObjectOrder = setDataViewObjectOrder,
            boardGroupSubscriptionContainer = boardGroupSubscriptionContainer,
            boardRecordsSubscriptionContainer = boardRecordsSubscriptionContainer,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            emojiProvider = emojiProvider,
            emojiSuggester = emojiSuggester,
            createBlock = createBlock,
            stringResourceProvider = stringResourceProvider,
            getDefaultObjectType = getDefaultObjectType,
            addDiscussion = addDiscussion,
            userSettingsRepository = userSettingsRepository,
            backHistoryDelegate = mock(),
            exitToVaultDelegate = mock(),
            viewStateDispatcher = rule.dispatcher
        )
    }

    fun stubInterceptEvents(
        params: InterceptEvents.Params = InterceptEvents.Params(context = root),
        flow: Flow<List<Event>> = flowOf()
    ) {
        interceptEvents.stub {
            onBlocking { build(params) } doReturn flow
        }
    }

    fun stubInterceptThreadStatus() {
        spaceSyncAndP2PStatusProvider.stub {
            onBlocking { observe() } doReturn emptyFlow()
        }
    }

    fun stubOpenObject(
        doc: List<Block> = emptyList(),
        details: ObjectViewDetails = ObjectViewDetails.EMPTY,
        additionalEvents: List<Event> = emptyList(),
        dataViewRestrictions: List<DataViewRestrictions> = emptyList()
    ) {
        openObjectSet.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Result.Success(
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.ShowObject(
                                context = root,
                                root = root,
                                blocks = doc,
                                details = details.details,
                                dataViewRestrictions = dataViewRestrictions
                            )
                        ) + additionalEvents
                    )
                )
            )
        }
    }

    fun stubAddObjectToCollection() {
        addObjectToCollection.stub {
            onBlocking {
                async(any())
            } doReturn Resultat.success(Payload("", emptyList()))
        }
    }

    fun stubCloseBlock() {
        closeObject.stub {
            onBlocking {
                async(any())
            } doReturn Resultat.success(Unit)
        }
    }

    suspend fun stubSpaceManager(space: Id) {
        repo.stub {
            onBlocking { spaceOpen(space, false) } doReturn spaceConfig.copy(
                space = space
            )
            spaceManager.set(space)
        }
    }

    suspend fun stubSubscriptionResults(
        subscription: Id = MockDataFactory.randomString(),
        spaceId: Id,
        collection: Id? = null,
        objects: List<ObjectWrapper.Basic> = emptyList(),
        dependencies: List<ObjectWrapper.Basic> = listOf(),
        dvFilters: List<Block.Content.DataView.Filter> = emptyList(),
        dvSorts: List<Sort> = listOf(
            DVSort(
                relationKey = Relations.CREATED_DATE,
                type = Sort.Type.DESC,
                relationFormat = RelationFormat.DATE,
                includeTime = true
            )
        ),
        keys: List<Key> = emptyList(),
        sources: List<Id> = emptyList(),
        dvRelationLinks: List<RelationLink> = emptyList()
    ) {
        val dvKeys = (ObjectSearchConstants.defaultDataViewKeys + keys).toSet().toList()
        doReturn(
            SearchResult(
                results = objects,
                dependencies = dependencies
            )
        ).`when`(repo).searchObjectsWithSubscription(
            space = SpaceId(spaceId),
            subscription = subscription,
            collection = collection,
            filters = dvFilters.updateFormatForSubscription(storeOfRelations) + ObjectSearchConstants.defaultDataViewFilters(),
            sorts = dvSorts.updateWithRelationFormat(storeOfRelations),
            keys = dvKeys,
            source = sources,
            limit = ObjectSetConfig.DEFAULT_LIMIT,
            offset = 0L,
            afterId = null,
            beforeId = null,
            noDepSubscription = null,
            ignoreWorkspace = null
        )
        subscriptionEventChannel.stub {
            on {
                subscribe(listOf(subscription))
            } doReturn flow {
                emit(listOf())
            }
        }
    }

    suspend fun stubStoreOfRelations(mockObjectCollection: MockCollection) {
        storeOfRelations.merge(
            listOf(
                mockObjectCollection.relationObject1,
                mockObjectCollection.relationObject2,
                mockObjectCollection.relationObject3,
                mockObjectCollection.relationObject4,
                mockObjectCollection.relationObject5,
                mockObjectCollection.relationObject6,
                mockObjectCollection.createdDateRelation
            )
        )
    }

    suspend fun stubStoreOfRelations(mockObjectSet: MockSet) {
        storeOfRelations.merge(
            listOf(
                mockObjectSet.relationObject1,
                mockObjectSet.relationObject2,
                mockObjectSet.relationObject3,
                mockObjectSet.relationObject4,
                mockObjectSet.relationObject5,
                mockObjectSet.createdDateRelation
            )
        )
    }

    suspend fun stubStoreOfObjectTypes(id: String = "", map: Map<String, Any?> = emptyMap()) {
        storeOfObjectTypes.set(id, map)
    }

    fun stubGetDefaultPageType(
        type: TypeKey = defaultObjectPageType,
        name: String = defaultObjectPageTypeName,
        id: TypeId = TypeId(MockDataFactory.randomString()),
        template: Id? = null,
        spaceId: SpaceId? = null
    ) {
        val properSpaceId = spaceId ?: SpaceId(spaceConfig.space)
        getDefaultObjectType.stub {
            onBlocking { async(properSpaceId) } doReturn Resultat.Success(
                GetDefaultObjectType.Response(
                type = type,
                name = name,
                id = id,
                defaultTemplate = template
                )
            )
        }
    }

    fun stubTemplatesForTemplatesContainer(
        type: String = MockDataFactory.randomString(),
        templates: List<ObjectWrapper.Basic> = emptyList(),
        subId: Id = MockDataFactory.randomString()
    ) {
        templatesContainer.stub {
            onBlocking {
                subscribeToTemplates(
                    type = type,
                    space = SpaceId(defaultSpace),
                    subscription = subId
                )
            }.thenReturn(flowOf(templates))
        }
    }

    fun stubCreateDataViewObject(
        objectId: Id = MockDataFactory.randomString(),
        // TODO pass key here, not id
        objectType: Key = MockDataFactory.randomString()
    ) {
        createDataViewObject.stub {
            onBlocking { async(any()) }.thenReturn(
                Resultat.success(
                    CreateDataViewObject.Result(
                        objectId = objectId,
                        objectType = TypeKey(objectType),
                        struct = mapOf<Id, Any?>("spaceId" to "spaceId")
                    )
                )
            )
        }
    }

    fun stubNetworkMode() {
        getNetworkMode.stub {
            onBlocking { run(Unit) } doReturn NetworkModeConfig()
        }
    }

    fun stubObservePermissions(
        permission: SpaceMemberPermissions = SpaceMemberPermissions.OWNER
    ) {
        (permissions as UserPermissionProviderStub).stubObserve(
            SpaceId(""), permission
        )
    }

    fun stubObjectToCollection() {
        objectToCollection.stub {
            onBlocking { async(any()) } doReturn Resultat.success(Unit)
        }
    }

    fun givenNetworkNodeMocked() {
        getNetworkMode.stub {
            onBlocking {
                run(any())
            } doReturn NetworkModeConfig()
        }
    }

    fun stubAnalyticSpaceHelperDelegate() {
        Mockito.`when`(analyticSpaceHelperDelegate.provideParams(defaultSpace))
            .thenReturn(AnalyticSpaceHelperDelegate.Params.EMPTY)
    }

    fun stubLocalProvider() {
        localeProvider.stub {
            on { locale() } doReturn Locale.getDefault()
            on { language() } doReturn "en"
        }
    }
}
