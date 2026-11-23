package io.aparok.app.presentation.sets.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Relation
import io.aparok.app.core_models.StubRelationObject
import io.aparok.app.domain.base.Resultat
import io.aparok.app.domain.config.Gateway
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.misc.UrlBuilderImpl
import io.aparok.app.domain.objects.DefaultObjectStore
import io.aparok.app.domain.objects.DefaultStoreOfObjectTypes
import io.aparok.app.domain.objects.DefaultStoreOfRelations
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.objects.options.GetOptions
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.mapper.toDomain
import io.aparok.app.presentation.sets.MockObjectSetFactory
import io.aparok.app.presentation.sets.ObjectSetDatabase
import io.aparok.app.presentation.sets.ObjectSetSession
import io.aparok.app.presentation.sets.dataViewState
import io.aparok.app.presentation.sets.model.Viewer
import io.aparok.app.presentation.util.CoroutinesTestRule
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.test_utils.MockDataFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

class FilterViewModelInputFieldValueModifyTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var updateDataViewViewer: UpdateDataViewViewer

    @Mock
    lateinit var searchObjects: SearchObjects

    @Mock
    lateinit var getOptions: GetOptions

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var spaceViews: SpaceViewSubscriptionContainer

    private lateinit var viewModel: FilterViewModel
    private lateinit var urlBuilder: UrlBuilder
    private val root = MockDataFactory.randomUuid()
    private val dataViewId = MockDataFactory.randomString()
    private val session = ObjectSetSession()
    private val storeOfObjectTypes: StoreOfObjectTypes = DefaultStoreOfObjectTypes()

    //LONG TEXT
    private val relation1 = StubRelationObject(
        key = MockDataFactory.randomString(),
        name = MockDataFactory.randomString(),
        format = Relation.Format.LONG_TEXT
    )

    private val viewerRelation1 = Block.Content.DataView.Viewer.ViewerRelation(
        key = relation1.key,
        isVisible = true
    )

    //NUMBER
    private val relation2 = StubRelationObject(
        key = MockDataFactory.randomString(),
        name = MockDataFactory.randomString(),
        format = Relation.Format.NUMBER
    )

    private val viewerRelation2 = Block.Content.DataView.Viewer.ViewerRelation(
        key = relation2.key,
        isVisible = true
    )

    //SHORT TEXT
    private val relation3 = StubRelationObject(
        key = MockDataFactory.randomString(),
        name = MockDataFactory.randomString(),
        format = Relation.Format.SHORT_TEXT
    )

    private val viewerRelation3 = Block.Content.DataView.Viewer.ViewerRelation(
        key = relation3.key,
        isVisible = true
    )

    //URL
    private val relation4 = StubRelationObject(
        key = MockDataFactory.randomString(),
        name = MockDataFactory.randomString(),
        format = Relation.Format.URL
    )

    private val viewerRelation4 = Block.Content.DataView.Viewer.ViewerRelation(
        key = relation4.key,
        isVisible = true
    )

    //EMAIL
    private val relation5 = StubRelationObject(
        key = MockDataFactory.randomString(),
        name = MockDataFactory.randomString(),
        format = Relation.Format.EMAIL
    )

    private val viewerRelation5 = Block.Content.DataView.Viewer.ViewerRelation(
        key = relation5.key,
        isVisible = true
    )

    //PHONE
    private val relation6 = StubRelationObject(
        key = MockDataFactory.randomString(),
        name = MockDataFactory.randomString(),
        format = Relation.Format.PHONE
    )

    private val viewerRelation6 = Block.Content.DataView.Viewer.ViewerRelation(
        key = relation6.key,
        isVisible = true
    )

    private val relations = listOf(
        relation1,
        relation2,
        relation3,
        relation4,
        relation5,
        relation6
    )

    private val state = MutableStateFlow(
        MockObjectSetFactory.makeDefaultSetObjectState(
            dataViewId = dataViewId,
            relations = relations,
            viewerRelations = listOf(
                viewerRelation1,
                viewerRelation2,
                viewerRelation3,
                viewerRelation4,
                viewerRelation5,
                viewerRelation6
            ),
            filters = listOf(
                Block.Content.DataView.Filter(
                    relation = relation1.key,
                    condition = Block.Content.DataView.Filter.Condition.LIKE,
                    value = MockDataFactory.randomString()
                )
            )
        )
    )
    private val dispatcher = Dispatcher.Default<Payload>()

    private val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    private val objectStore: ObjectStore = DefaultObjectStore()
    private val db = ObjectSetDatabase(store = objectStore)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        urlBuilder = UrlBuilderImpl(gateway)
        val analyticSpaceHelperDelegate = mock<io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate>()
        analyticSpaceHelperDelegate.stub {
            on { provideParams(any()) } doReturn io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate.Params.EMPTY
        }
        viewModel = FilterViewModel(
            objectState = state,
            dispatcher = dispatcher,
            urlBuilder = urlBuilder,
            updateDataViewViewer = updateDataViewViewer,
            searchObjects = searchObjects,
            analytics = analytics,
            storeOfObjectTypes = storeOfObjectTypes,
            storeOfRelations = storeOfRelations,
            objectSetDatabase = db,
            getOptions = getOptions,
            spaceManager = spaceManager,
            fieldParser = fieldParser,
            spaceViews = spaceViews,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
        )
    }

    //region LONG TEXT
    @Test
    fun `should empty string value, long text 1`() = runTest {

        storeOfRelations.merge(relations)

        //INIT
        val condition = Viewer.Filter.Condition.Text.Empty()
        val textInput = EMPTY_STRING
        val filterIndex = 0

        shouldSendFilterValueAsNullString(relation1.key, condition, textInput, filterIndex)
    }

    @Test
    fun `should empty string value, long text 2`() = runTest {

        storeOfRelations.merge(relations)

        //INIT
        val condition = Viewer.Filter.Condition.Text.Empty()
        val textInput = NOT_EMPTY_STRING
        val filterIndex = 0

        shouldSendFilterValueAsNullString(relation1.key, condition, textInput, filterIndex)
    }

    @Test
    fun `should empty string value, long text 3`() = runTest {

        storeOfRelations.merge(relations)

        //INIT
        val condition = Viewer.Filter.Condition.Text.NotEmpty()
        val textInput = EMPTY_STRING
        val filterIndex = 0

        shouldSendFilterValueAsNullString(relation1.key, condition, textInput, filterIndex)
    }

    @Test
    fun `should empty string value, long text 4`() = runTest {

        storeOfRelations.merge(relations)

        //INIT
        val condition = Viewer.Filter.Condition.Text.NotEmpty()
        val textInput = NOT_EMPTY_STRING
        val filterIndex = 0

        shouldSendFilterValueAsNullString(relation1.key, condition, textInput, filterIndex)
    }

    @Test
    fun `should not empty string value, long text 1`() = runTest {

        storeOfRelations.merge(relations)

        //INIT
        val condition = Viewer.Filter.Condition.Text.Equal()
        val textInput = NOT_EMPTY_STRING
        val filterIndex = 0

        shouldSendFilterValueAsNotEmptyString(relation1.key, condition, textInput, filterIndex)
    }

    @Test
    fun `should not empty string value, long text 2`() = runTest {

        storeOfRelations.merge(relations)

        //INIT
        val condition = Viewer.Filter.Condition.Text.NotEqual()
        val textInput = NOT_EMPTY_STRING
        val filterIndex = 0

        shouldSendFilterValueAsNotEmptyString(relation1.key, condition, textInput, filterIndex)
    }

    @Test
    fun `should not empty string value, long text 3`() = runTest {

        storeOfRelations.merge(relations)

        //INIT
        val condition = Viewer.Filter.Condition.Text.NotLike()
        val textInput = NOT_EMPTY_STRING
        val filterIndex = 0

        shouldSendFilterValueAsNotEmptyString(relation1.key, condition, textInput, filterIndex)
    }

    @Test
    fun `should not empty string value, long text 4`() = runTest {

        storeOfRelations.merge(relations)

        //INIT
        val condition = Viewer.Filter.Condition.Text.Like()
        val textInput = NOT_EMPTY_STRING
        val filterIndex = 0

        shouldSendFilterValueAsNotEmptyString(relation1.key, condition, textInput, filterIndex)
    }

    //endregion

    private fun shouldSendFilterValueAsNullString(
        relationKey: String,
        condition: Viewer.Filter.Condition,
        textInput: String,
        filterIndex: Int?
    ) {
        stubUpdateDataView()

        val viewer = state.value.dataViewState()!!.viewers[0]

        viewModel.onStart(
            relationKey = relation1.key,
            filterIndex = filterIndex,
            viewerId = viewer.id
        )

        viewModel.onConditionUpdate(condition)

        viewModel.onModifyApplyClicked(
            ctx = root,
            input = textInput,
            viewerId = viewer.id
        )

        verifyBlocking(updateDataViewViewer, times(1)) {
            async(
                UpdateDataViewViewer.Params.Filter.Replace(
                    ctx = root,
                    dv = dataViewId,
                    view = viewer.id,
                    filter = DVFilter(
                        relation = relationKey,
                        operator = DEFAULT_OPERATOR,
                        condition = condition.toDomain(),
                        value = null
                    )
                )
            )
        }
    }

    private fun shouldSendFilterValueAsNotEmptyString(
        relationKey: String,
        condition: Viewer.Filter.Condition,
        textInput: String,
        filterIndex: Int?
    ) {

        stubUpdateDataView()

        val viewer = state.value.dataViewState()!!.viewers[0]

        viewModel.onStart(
            relationKey = relation1.key,
            filterIndex = filterIndex,
            viewerId = viewer.id
        )

        viewModel.onConditionUpdate(condition)

        viewModel.onModifyApplyClicked(
            ctx = root,
            input = textInput,
            viewerId = viewer.id
        )

        verifyBlocking(updateDataViewViewer, times(1)) {
            async(
                UpdateDataViewViewer.Params.Filter.Replace(
                    ctx = root,
                    dv = dataViewId,
                    view = viewer.id,
                    filter = DVFilter(
                        relation = relationKey,
                        operator = DEFAULT_OPERATOR,
                        condition = condition.toDomain(),
                        value = textInput
                    )
                )
            )
        }
    }

    private fun stubUpdateDataView() {
        updateDataViewViewer.stub {
            onBlocking { async(any()) } doReturn Resultat.success(
                Payload(
                    context = "",
                    events = emptyList()
                )
            )
        }
        spaceManager.stub {
            onBlocking { get() } doReturn MockDataFactory.randomString()
        }
    }

    companion object {
        const val EMPTY_STRING = ""
        const val NOT_EMPTY_STRING = "not empty"
        val DEFAULT_OPERATOR = Block.Content.DataView.Filter.Operator.NO
    }
}