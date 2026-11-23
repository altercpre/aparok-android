package io.aparok.app.features.sets.filter

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.aparok.app.R
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Relation
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
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
import io.aparok.app.presentation.sets.ObjectSetDatabase
import io.aparok.app.presentation.sets.filter.FilterViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.test_utils.MockDataFactory
import io.aparok.app.test_utils.utils.TestUtils
import io.aparok.app.ui.sets.modals.filter.ModifyFilterFromSelectedValueFragment
import io.aparok.app.utils.CoroutinesTestRule
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@RunWith(AndroidJUnit4::class)
@LargeTest
class ModifyStatusFilterTest {

    @get:Rule
    val animationsRule = DisableAnimationsRule()

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Mock
    lateinit var dispatchers: AppCoroutineDispatchers

    @Mock
    lateinit var repo: BlockRepository
    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var spaceViews: SpaceViewSubscriptionContainer

    private lateinit var updateDataViewViewer: UpdateDataViewViewer
    private lateinit var searchObjects: SearchObjects
    private lateinit var getOptions: GetOptions
    lateinit var urlBuilder: UrlBuilder

    private val root = MockDataFactory.randomUuid()
    private val state: MutableStateFlow<ObjectState> = MutableStateFlow(ObjectState.Init)
    private val dispatcher = Dispatcher.Default<Payload>()
    private val storeOfObjectTypes: StoreOfObjectTypes = DefaultStoreOfObjectTypes()
    private val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    private val objectStore: ObjectStore = DefaultObjectStore()
    private val db = ObjectSetDatabase(store = objectStore)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        updateDataViewViewer = UpdateDataViewViewer(repo, dispatchers)
        searchObjects = SearchObjects(repo)
        getOptions = GetOptions(repo)
        urlBuilder = UrlBuilderImpl(gateway)
        TestModifyFilterFromSelectedValueFragment.testVmFactory = FilterViewModel.Factory(
            objectState = state,
            updateDataViewViewer = updateDataViewViewer,
            dispatcher = dispatcher,
            searchObjects = searchObjects,
            getOptions = getOptions,
            urlBuilder = urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes,
            analytics = analytics,
            storeOfRelations = storeOfRelations,
            objectSetDatabase = db,
            spaceManager = spaceManager,
            fieldParser = fieldParser,
            spaceViews = spaceViews,
            analyticSpaceHelperDelegate = mock()
        )
    }

    @Test
    fun shouldSelectSecondStatusAndApplyChangesOnClick() {

        val relationKey = MockDataFactory.randomUuid()

        // Defining three different statuses:

        val option1 = Relation.Option(
            id = MockDataFactory.randomUuid(),
            text = "In progress",
            color = MockDataFactory.randomString()
        )

        val option2 = Relation.Option(
            id = MockDataFactory.randomUuid(),
            text = "In Testing",
            color = MockDataFactory.randomString()
        )

        val option3 = Relation.Option(
            id = MockDataFactory.randomUuid(),
            text = "Done",
            color = MockDataFactory.randomString()
        )

        // Defining viewer containing one filter

        val filter = DVFilter(
            relation = relationKey,
            value = listOf(option1.id),
            condition = DVFilterCondition.EQUAL
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = listOf(filter),
            sorts = emptyList(),
            viewerRelations = listOf(
                Block.Content.DataView.Viewer.ViewerRelation(
                    key = relationKey,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.values().random()
        )

        val dv = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer)
            )
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(dv)
        )

        // Launching fragment

        launchFragment(
            bundleOf(
                ModifyFilterFromSelectedValueFragment.CTX_KEY to root,
                ModifyFilterFromSelectedValueFragment.IDX_KEY to 0,
                ModifyFilterFromSelectedValueFragment.RELATION_KEY to relationKey,
            )
        )

        // TESTING

        val rvMatcher = TestUtils.withRecyclerView(R.id.rvViewerFilterRecycler)

        // Checking names

        onView(rvMatcher.atPositionOnView(0, R.id.tvStatusName)).apply {
            check(matches(withText(option1.text)))
        }

        onView(rvMatcher.atPositionOnView(1, R.id.tvStatusName)).apply {
            check(matches(withText(option2.text)))
        }

        onView(rvMatcher.atPositionOnView(2, R.id.tvStatusName)).apply {
            check(matches(withText(option3.text)))
        }

        // Veryfing that only the first status is selected

        onView(rvMatcher.atPositionOnView(0, R.id.ivSelectStatusIcon)).apply {
            check(matches(isSelected()))
        }

        onView(rvMatcher.atPositionOnView(1, R.id.ivSelectStatusIcon)).apply {
            check(matches(not(isSelected())))
        }

        onView(rvMatcher.atPositionOnView(2, R.id.ivSelectStatusIcon)).apply {
            check(matches(not(isSelected())))
        }

        // Verifying that the selection counter is equal to 1

        onView(withId(R.id.btnAdd)).apply {
            check(matches(withText("1")))
        }

        // Performing click, in order to sellect second status

        onView(rvMatcher.atPositionOnView(1, R.id.ivSelectStatusIcon)).apply {
            perform(click())
        }

        // Veryfing that only the second status is selected

        onView(rvMatcher.atPositionOnView(0, R.id.ivSelectStatusIcon)).apply {
            check(matches(not(isSelected())))
        }

        onView(rvMatcher.atPositionOnView(1, R.id.ivSelectStatusIcon)).apply {
            check(matches((isSelected())))
        }

        onView(rvMatcher.atPositionOnView(2, R.id.ivSelectStatusIcon)).apply {
            check(matches(not(isSelected())))
        }

        // Verifying that the selection counter is still equal to 1

        onView(withId(R.id.btnAdd)).apply {
            check(matches(withText("1")))
        }

        // Performing a click to apply filter changes.

        onView(withId(R.id.btnBottomAction)).apply {
            perform(click())
        }

        // Verifying that viewer's filters are updated.

        verifyBlocking(repo, times(1)) {
            updateDataViewViewer(
                context = root,
                target = dv.id,
                viewer = viewer.copy(
                    filters = listOf(filter.copy(value = listOf(option2.id)))
                )
            )
        }
    }

    private fun launchFragment(args: Bundle): FragmentScenario<TestModifyFilterFromSelectedValueFragment> {
        return launchFragmentInContainer(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }
}