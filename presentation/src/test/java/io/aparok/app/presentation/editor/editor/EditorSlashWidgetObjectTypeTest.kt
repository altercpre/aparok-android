package io.aparok.app.presentation.editor.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.Position
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.StubObject
import io.aparok.app.core_models.StubParagraph
import io.aparok.app.core_models.StubSmartBlock
import io.aparok.app.core_models.primitives.TypeId
import io.aparok.app.core_models.primitives.TypeKey
import io.aparok.app.domain.page.CreateBlockLinkWithObject
import io.aparok.app.presentation.MockTypicalDocumentFactory
import io.aparok.app.presentation.editor.EditorViewModel.Companion.TEXT_CHANGES_DEBOUNCE_DURATION
import io.aparok.app.presentation.editor.editor.model.types.Types
import io.aparok.app.presentation.editor.editor.slash.SlashEvent
import io.aparok.app.presentation.editor.editor.slash.SlashItem
import io.aparok.app.core_models.ui.ObjectIcon
import io.aparok.app.presentation.objects.ObjectTypeView
import io.aparok.app.presentation.util.DefaultCoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

class EditorSlashWidgetObjectTypeTest : EditorPresentationTestSetup() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = DefaultCoroutineTestRule()

    val space = defaultSpace

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        proceedWithDefaultBeforeTestStubbing()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun after() {
        coroutineTestRule.advanceTime(TEXT_CHANGES_DEBOUNCE_DURATION)
    }

    @Test
    fun `should invoke CreateBlockLinkWithObject UseCase on clicked on object type item with bottom position when text block is not empty`() = runTest {
        // SETUP
        val doc = MockTypicalDocumentFactory.page(root)
        val a = MockTypicalDocumentFactory.a
        val type1 = StubObject(name = "Hd")
        val type2 = StubObject(name = "Df")
        val type3 = StubObject(name = "LK")

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubUpdateText()
        stubSearchObjects(listOf(type1, type2, type3))
        stubCreateBlockLinkWithObject(root, a.id)
        stubOpenDocument(doc)
        stubSpaceManager(defaultSpace)

        val vm = buildViewModel()

        vm.onStart(id = root, space = defaultSpace)

        advanceUntilIdle()

        vm.apply {
            onBlockFocusChanged(a.id, true)
            advanceUntilIdle()
            onSlashTextWatcherEvent(SlashEvent.Start(100, 0))
            advanceUntilIdle()
            onSlashTextWatcherEvent(
                SlashEvent.Filter(
                    filter = "/",
                    viewType = Types.HOLDER_NUMBERED
                )
            )
            advanceUntilIdle()
        }

        // TESTING

        vm.onSlashItemClicked(
            SlashItem.ObjectType(
                objectTypeView = ObjectTypeView(
                    id = type2.id,
                    name = type2.name.orEmpty(),
                    description = type2.description,
                    icon = ObjectIcon.None,
                    key = type2.getValue<Key>(Relations.UNIQUE_KEY)!!
                )
            )
        )

        advanceUntilIdle()

        val params = CreateBlockLinkWithObject.Params(
            context = root,
            target = a.id,
            position = Position.BOTTOM,
            typeId = TypeId(type2.id),
            typeKey = TypeKey(type2.getValue<Key>(Relations.UNIQUE_KEY)!!),
            space = space,
            template = null
        )

        verifyBlocking(createBlockLinkWithObject, times(1)) { async(params) }
    }

    @Test
    fun `should invoke CreateBlockLinkWithObject UseCase on clicked on object type item with replace position when text block is empty`() = runTest {
        // SETUP

        val paragraph = StubParagraph(text = "")
        val smart = StubSmartBlock(children = listOf(paragraph.id))
        val doc = listOf(smart, paragraph)

        val type1 = StubObject(name = "Hd")
        val type2 = StubObject(name = "Df")
        val type3 = StubObject(name = "LK")

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubUpdateText()
        stubSearchObjects(listOf(type1, type2, type3))
        stubCreateBlockLinkWithObject(root, paragraph.id)
        stubOpenDocument(doc)
        stubSpaceManager(space = space)

        val vm = buildViewModel()
        vm.onStart(id = root, space = defaultSpace)

        advanceUntilIdle()

        vm.apply {
            onBlockFocusChanged(paragraph.id, true)
            advanceUntilIdle()
            onSlashTextWatcherEvent(SlashEvent.Start(1, 0))
            advanceUntilIdle()
            onSlashTextWatcherEvent(
                SlashEvent.Filter(
                    filter = "/",
                    viewType = Types.HOLDER_NUMBERED
                )
            )
            advanceUntilIdle()
        }

        // TESTING

        vm.onSlashItemClicked(
            SlashItem.ObjectType(
                objectTypeView = ObjectTypeView(
                    id = type2.id,
                    name = type2.name.orEmpty(),
                    description = type2.description,
                    key = type2.getValue(Relations.UNIQUE_KEY)!!,
                    icon = ObjectIcon.None
                )
            )
        )

        advanceUntilIdle()

        val params = CreateBlockLinkWithObject.Params(
            context = root,
            target = paragraph.id,
            position = Position.REPLACE,
            typeId = TypeId(type2.id),
            typeKey = TypeKey(type2.getValue(Relations.UNIQUE_KEY)!!),
            space = space,
            template = null
        )

        advanceUntilIdle()

        verifyBlocking(createBlockLinkWithObject, times(1)) { async(params) }
    }
}