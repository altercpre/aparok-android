package io.aparok.app.features.relations

import android.os.Bundle
import android.text.InputType
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.aparok.app.R
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Relation
import io.aparok.app.domain.`object`.ReloadObject
import io.aparok.app.domain.objects.DefaultObjectStore
import io.aparok.app.domain.objects.DefaultStoreOfRelations
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.presentation.relations.providers.DataViewObjectValueProvider
import io.aparok.app.presentation.sets.ObjectSetDatabase
import io.aparok.app.presentation.sets.RelationTextValueViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.test_utils.MockDataFactory
import io.aparok.app.test_utils.utils.espresso.TextLineCountMatcher
import io.aparok.app.ui.relations.RelationTextValueFragment
import io.aparok.app.utils.CoroutinesTestRule
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@LargeTest
class DisplayObjectRelationTextValueTest {

    @get:Rule
    val animationsRule = DisableAnimationsRule()

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Mock
    lateinit var reloadObject: ReloadObject

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var storeOfObjectTypes: StoreOfObjectTypes

    val root = MockDataFactory.randomUuid()

    private val state: MutableStateFlow<ObjectState> = MutableStateFlow(ObjectState.Init)
    private val store: ObjectStore = DefaultObjectStore()
    private val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    private val db = ObjectSetDatabase(store = store)

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        TestRelationTextValueFragment.testVmFactory = RelationTextValueViewModel.Factory(
            values = DataViewObjectValueProvider(db = db, objectState = state),
            reloadObject = reloadObject,
            analytics = analytics,
            storeOfObjectTypes = storeOfObjectTypes,
            storeOfRelations = storeOfRelations
        )
    }

    @Test
    fun shouldSetDescriptionTextAndNotDisplayActionButton() {

        // SETUP

        val relationText = "Architect"
        val valueText = "Filippo Brunelleschi"

        val target = MockDataFactory.randomUuid()

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = emptyList(),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val relation = Relation(
            key = MockDataFactory.randomUuid(),
            name = relationText,
            format = Relation.Format.SHORT_TEXT,
            source = Relation.Source.values().random()
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(
                Block(
                    id = MockDataFactory.randomUuid(),
                    children = emptyList(),
                    fields = Block.Fields.empty(),
                    content = Block.Content.DataView(
                        viewers = listOf(viewer)
                    )
                )
            )
        )

        launchFragment(
            bundleOf(
                RelationTextValueFragment.CONTEXT_ID to root,
                RelationTextValueFragment.RELATION_KEY to relation.key,
                RelationTextValueFragment.OBJECT_ID to target,
                RelationTextValueFragment.FLOW_KEY to RelationTextValueFragment.FLOW_DATAVIEW
            )
        )

        // TESTING

        onView(withId(R.id.textInputField)).apply {
            check(matches(withText(valueText)))
        }

        onView(withId(R.id.tvRelationHeader)).apply {
            check(matches(withText(relationText)))
        }

        onView(withId(R.id.btnAction)).apply {
            check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun shouldSetNumberTextAndNotDisplayActionButton() {

        // SETUP

        val relationText = "Year"
        val valueText = "1446"

        val target = MockDataFactory.randomUuid()

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = emptyList(),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val relation = Relation(
            key = MockDataFactory.randomUuid(),
            name = relationText,
            format = Relation.Format.NUMBER,
            source = Relation.Source.values().random()
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(
                Block(
                    id = MockDataFactory.randomUuid(),
                    children = emptyList(),
                    fields = Block.Fields.empty(),
                    content = Block.Content.DataView(
                        viewers = listOf(viewer)
                    )
                )
            )
        )

        // TESTING

        launchFragment(
            bundleOf(
                RelationTextValueFragment.CONTEXT_ID to root,
                RelationTextValueFragment.RELATION_KEY to relation.key,
                RelationTextValueFragment.OBJECT_ID to target,
                RelationTextValueFragment.FLOW_KEY to RelationTextValueFragment.FLOW_DATAVIEW
            )
        )

        // TESTING

        onView(withId(R.id.textInputField)).apply {
            check(matches(withText(valueText)))
        }

        onView(withId(R.id.tvRelationHeader)).apply {
            check(matches(withText(relationText)))
        }

        onView(withId(R.id.btnAction)).apply {
            check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun shouldSetPhoneTextAndDisplayActionButton() {

        // SETUP

        val relationText = "Phone number"
        val valueText = "+ 124242423423"

        val target = MockDataFactory.randomUuid()

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = emptyList(),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val relation = Relation(
            key = MockDataFactory.randomUuid(),
            name = relationText,
            format = Relation.Format.PHONE,
            source = Relation.Source.values().random()
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(
                Block(
                    id = MockDataFactory.randomUuid(),
                    children = emptyList(),
                    fields = Block.Fields.empty(),
                    content = Block.Content.DataView(
                        viewers = listOf(viewer)
                    )
                )
            )
        )

        // TESTING

        launchFragment(
            bundleOf(
                RelationTextValueFragment.CONTEXT_ID to root,
                RelationTextValueFragment.RELATION_KEY to relation.key,
                RelationTextValueFragment.OBJECT_ID to target,
                RelationTextValueFragment.FLOW_KEY to RelationTextValueFragment.FLOW_DATAVIEW
            )
        )

        // TESTING

        onView(withId(R.id.textInputField)).apply {
            check(matches(withText(valueText)))
        }

        onView(withId(R.id.tvRelationHeader)).apply {
            check(matches(withText(relationText)))
        }

        onView(withId(R.id.btnAction)).apply {
            check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldSetEmailTextAndDisplayActionButton() {

        // SETUP

        val relationText = "Email"
        val valueText = "foo.bar@foobar.com"

        val target = MockDataFactory.randomUuid()

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = emptyList(),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val relation = Relation(
            key = MockDataFactory.randomUuid(),
            name = relationText,
            format = Relation.Format.EMAIL,
            source = Relation.Source.values().random()
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(
                Block(
                    id = MockDataFactory.randomUuid(),
                    children = emptyList(),
                    fields = Block.Fields.empty(),
                    content = Block.Content.DataView(
                        viewers = listOf(viewer)
                    )
                )
            )
        )

        launchFragment(
            bundleOf(
                RelationTextValueFragment.CONTEXT_ID to root,
                RelationTextValueFragment.RELATION_KEY to relation.key,
                RelationTextValueFragment.OBJECT_ID to target,
                RelationTextValueFragment.FLOW_KEY to RelationTextValueFragment.FLOW_DATAVIEW
            )
        )

        // TESTING

        onView(withId(R.id.textInputField)).apply {
            check(matches(withText(valueText)))
        }

        onView(withId(R.id.tvRelationHeader)).apply {
            check(matches(withText(relationText)))
        }

        onView(withId(R.id.btnAction)).apply {
            check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldSetUrlTextAndDisplayActionButton() {

        // SETUP

        val relationText = "Url"
        val valueText = "https://github.com/altercpre/aparok-android/releases/latest"

        val target = MockDataFactory.randomUuid()

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = emptyList(),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val relation = Relation(
            key = MockDataFactory.randomUuid(),
            name = relationText,
            format = Relation.Format.URL,
            source = Relation.Source.values().random()
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(
                Block(
                    id = MockDataFactory.randomUuid(),
                    children = emptyList(),
                    fields = Block.Fields.empty(),
                    content = Block.Content.DataView(
                        viewers = listOf(viewer)
                    )
                )
            )
        )

        launchFragment(
            bundleOf(
                RelationTextValueFragment.CONTEXT_ID to root,
                RelationTextValueFragment.RELATION_KEY to relation.key,
                RelationTextValueFragment.OBJECT_ID to target,
                RelationTextValueFragment.FLOW_KEY to RelationTextValueFragment.FLOW_DATAVIEW
            )
        )

        // TESTING

        onView(withId(R.id.textInputField)).apply {
            check(matches(withText(valueText)))
        }

        onView(withId(R.id.tvRelationHeader)).apply {
            check(matches(withText(relationText)))
        }

        onView(withId(R.id.btnAction)).apply {
            check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldDisplayShortText() {

        // SETUP

        val relationText = "Short text"
        val valueText =
            "Anytype is a next generation software that breaks down barriers between applications, gives back privacy and data ownership to users."

        val target = MockDataFactory.randomUuid()

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = emptyList(),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val relation = Relation(
            key = MockDataFactory.randomUuid(),
            name = relationText,
            format = Relation.Format.SHORT_TEXT,
            source = Relation.Source.values().random()
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(
                Block(
                    id = MockDataFactory.randomUuid(),
                    children = emptyList(),
                    fields = Block.Fields.empty(),
                    content = Block.Content.DataView(
                        viewers = listOf(viewer),

                    )
                )
            )
        )

        launchFragment(
            bundleOf(
                RelationTextValueFragment.CONTEXT_ID to root,
                RelationTextValueFragment.RELATION_KEY to relation.key,
                RelationTextValueFragment.OBJECT_ID to target,
                RelationTextValueFragment.FLOW_KEY to RelationTextValueFragment.FLOW_DATAVIEW
            )
        )

        // TESTING

        onView(withId(R.id.textInputField)).apply {
            check(matches(withText(valueText)))
            check(matches(withInputType(InputType.TYPE_CLASS_TEXT)))
            check(matches(TextLineCountMatcher(1)))
        }

        onView(withId(R.id.tvRelationHeader)).apply {
            check(matches(withText(relationText)))
        }

        onView(withId(R.id.btnAction)).apply {
            check(matches(not(isDisplayed())))
        }
    }

    private fun launchFragment(args: Bundle): FragmentScenario<TestRelationTextValueFragment> {
        return launchFragmentInContainer(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }
}