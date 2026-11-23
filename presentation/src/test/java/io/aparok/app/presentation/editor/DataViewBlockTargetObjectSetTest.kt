package io.aparok.app.presentation.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectTypeIds
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.StubDataViewBlock
import io.aparok.app.core_models.StubDataViewView
import io.aparok.app.core_models.StubHeader
import io.aparok.app.core_models.StubParagraph
import io.aparok.app.core_models.StubTitle
import io.aparok.app.core_models.ThemeColor
import io.aparok.app.core_models.ext.content
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.core_models.ObjectViewDetails
import io.aparok.app.presentation.editor.editor.EditorPresentationTestSetup
import io.aparok.app.presentation.editor.editor.ViewState
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.core_models.ui.ObjectIcon
import io.aparok.app.presentation.util.CoroutinesTestRule
import io.aparok.app.test_utils.MockDataFactory
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import net.bytebuddy.utility.RandomString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class DataViewBlockTargetObjectSetTest : EditorPresentationTestSetup() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val title = StubTitle()
    private val header = StubHeader(children = listOf(title.id))
    private val block = StubParagraph()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        proceedWithDefaultBeforeTestStubbing()
        stubInterceptEvents()
    }

    @Test
    fun `should render EmptySource set dataView block when targetObjectId is empty`() = runBlocking {

        // SETUP
        stubInterceptThreadStatus()
        val params = InterceptEvents.Params(context = root)
        val dv = StubDataViewBlock(targetObjectId = "")
        val page = Block(
            id = root,
            fields = Block.Fields.empty(),
            children = listOf(header.id, block.id, dv.id),
            content = Block.Content.Smart
        )

        stubOpenDocument(document = listOf(page, header, title, block, dv))
        stubInterceptEvents(params = params)

        val vm = buildViewModel()

        //EXPECTING
        val state = ViewState.Success(
            blocks = listOf(
                BlockView.Title.Basic(
                    id = title.id,
                    text = title.content<Block.Content.Text>().text,
                    isFocused = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                ),
                BlockView.Text.Paragraph(
                    id = block.id,
                    text = block.content<Block.Content.Text>().text,
                    decorations = listOf(BlockView.Decoration())
                ),
                BlockView.DataView.EmptySource(
                    id = dv.id,
                    title = null,
                    background = ThemeColor.DEFAULT,
                    isSelected = false,
                    icon = ObjectIcon.None,
                    decorations = listOf(BlockView.Decoration(style = BlockView.Decoration.Style.Card)),
                    isCollection = false
                )
            )
        )

        // TESTING
        vm.onStart(id = root, space = defaultSpace)

        assertEquals(
            expected = state,
            actual = vm.state.value
        )
    }

    @Test
    fun `should render EmptyData set dataView block when SetOf with empty strings`() = runBlocking {

        // SETUP
        stubInterceptThreadStatus()
        val params = InterceptEvents.Params(context = root)
        val targetObjectId = MockDataFactory.randomUuid()
        val dv = StubDataViewBlock(targetObjectId = targetObjectId)
        val page = Block(
            id = root,
            fields = Block.Fields.empty(),
            children = listOf(header.id, block.id, dv.id),
            content = Block.Content.Smart
        )
        val objectDetails =
            mapOf(
                Relations.ID to targetObjectId,
                Relations.TYPE to ObjectTypeIds.SET,
                Relations.LAYOUT to ObjectType.Layout.SET.code.toDouble(),
                Relations.SET_OF to listOf("")
            )

        val detailsList = ObjectViewDetails(details = mapOf(targetObjectId to objectDetails,
            root to mapOf(
                Relations.ID to root,
                Relations.TYPE to listOf(objType.id)
            )))

        stubOpenDocument(document = listOf(page, header, title, block, dv), details = detailsList)
        stubInterceptEvents(params = params)

        val vm = buildViewModel()

        //EXPECTING
        val state = ViewState.Success(
            blocks = listOf(
                BlockView.Title.Basic(
                    id = title.id,
                    text = title.content<Block.Content.Text>().text,
                    isFocused = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                ),
                BlockView.Text.Paragraph(
                    id = block.id,
                    text = block.content<Block.Content.Text>().text,
                    decorations = listOf(BlockView.Decoration())
                ),
                BlockView.DataView.EmptyData(
                    id = dv.id,
                    title = null,
                    background = ThemeColor.DEFAULT,
                    isSelected = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT,
                    decorations = listOf(BlockView.Decoration(style = BlockView.Decoration.Style.Card)),
                    isCollection = false
                )
            )
        )

        // TESTING
        vm.onStart(id = root, space = defaultSpace)

        assertEquals(
            expected = state,
            actual = vm.state.value
        )
    }

    @Test
    fun `should render EmptyData set dataView block when SetOf is empty`() = runBlocking {

        // SETUP
        stubInterceptThreadStatus()
        val params = InterceptEvents.Params(context = root)
        val targetObjectId = MockDataFactory.randomUuid()
        val dv = StubDataViewBlock(targetObjectId = targetObjectId)
        val page = Block(
            id = root,
            fields = Block.Fields.empty(),
            children = listOf(header.id, block.id, dv.id),
            content = Block.Content.Smart
        )
        val objectDetails =
            mapOf(
                Relations.ID to targetObjectId,
                Relations.TYPE to ObjectTypeIds.SET,
                Relations.LAYOUT to ObjectType.Layout.SET.code.toDouble(),
                Relations.SET_OF to emptyList<String>()
            )

        val detailsList = ObjectViewDetails(
            details = mapOf(
                targetObjectId to objectDetails,
                root to mapOf(
                    Relations.ID to root,
                    Relations.TYPE to listOf(objType.id)
                )
            )
        )

        stubOpenDocument(document = listOf(page, header, title, block, dv), details = detailsList)
        stubInterceptEvents(params = params)

        val vm = buildViewModel()

        //EXPECTING
        val state = ViewState.Success(
            blocks = listOf(
                BlockView.Title.Basic(
                    id = title.id,
                    text = title.content<Block.Content.Text>().text,
                    isFocused = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                ),
                BlockView.Text.Paragraph(
                    id = block.id,
                    text = block.content<Block.Content.Text>().text,
                    decorations = listOf(BlockView.Decoration())
                ),
                BlockView.DataView.EmptyData(
                    id = dv.id,
                    title = null,
                    background = ThemeColor.DEFAULT,
                    isSelected = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT,
                    decorations = listOf(BlockView.Decoration(style = BlockView.Decoration.Style.Card)),
                    isCollection = false
                )
            )
        )

        // TESTING
        vm.onStart(id = root, space = defaultSpace)

        assertEquals(
            expected = state,
            actual = vm.state.value
        )
    }

    @Test
    fun `should render Default set dataView block when SetOf with non empty id `() = runBlocking {

        // SETUP
        stubInterceptThreadStatus()
        val params = InterceptEvents.Params(context = root)
        val targetObjectId = MockDataFactory.randomUuid()
        val dv = StubDataViewBlock(targetObjectId = targetObjectId)
        val page = Block(
            id = root,
            fields = Block.Fields.empty(),
            children = listOf(header.id, block.id, dv.id),
            content = Block.Content.Smart
        )
        val objectDetails =
            mapOf(
                Relations.ID to targetObjectId,
                Relations.TYPE to ObjectTypeIds.SET,
                Relations.LAYOUT to ObjectType.Layout.SET.code.toDouble(),
                Relations.SET_OF to listOf<String>("", "   ", RandomString.make())
            )

        val detailsList = ObjectViewDetails(details = mapOf(targetObjectId to objectDetails))

        stubOpenDocument(document = listOf(page, header, title, block, dv), details = detailsList)
        stubInterceptEvents(params = params)

        val vm = buildViewModel()

        //EXPECTING
        val state = ViewState.Success(
            blocks = listOf(
                BlockView.Title.Basic(
                    id = title.id,
                    text = title.content<Block.Content.Text>().text,
                    isFocused = false
                ),
                BlockView.Text.Paragraph(
                    id = block.id,
                    text = block.content<Block.Content.Text>().text,
                    decorations = listOf(BlockView.Decoration())
                ),
                BlockView.DataView.Default(
                    id = dv.id,
                    title = null,
                    background = ThemeColor.DEFAULT,
                    isSelected = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT,
                    decorations = listOf(BlockView.Decoration(style = BlockView.Decoration.Style.Card)),
                    isCollection = false
                )
            )
        )

        // TESTING
        vm.onStart(id = root, space = defaultSpace)

        assertEquals(
            expected = state,
            actual = vm.state.value
        )
    }

    @Test
    fun `should render Default collection dataView block when targetObjectId is not empty `() = runBlocking {

        // SETUP
        stubInterceptThreadStatus()
        val params = InterceptEvents.Params(context = root)
        val targetObjectId = MockDataFactory.randomUuid()
        val dv = StubDataViewBlock(targetObjectId = targetObjectId, isCollection = true)
        val page = Block(
            id = root,
            fields = Block.Fields.empty(),
            children = listOf(header.id, block.id, dv.id),
            content = Block.Content.Smart
        )
        val objectDetails =
            mapOf(
                Relations.ID to targetObjectId,
                Relations.TYPE to ObjectTypeIds.COLLECTION,
                Relations.LAYOUT to ObjectType.Layout.COLLECTION.code.toDouble()
            )

        val detailsList = ObjectViewDetails(
            details = mapOf(
                targetObjectId to objectDetails,
                root to mapOf(
                    Relations.ID to root,
                    Relations.TYPE to listOf(objType.id)
                )
            )
        )

        stubOpenDocument(document = listOf(page, header, title, block, dv), details = detailsList)
        stubInterceptEvents(params = params)

        val vm = buildViewModel()

        //EXPECTING
        val state = ViewState.Success(
            blocks = listOf(
                BlockView.Title.Basic(
                    id = title.id,
                    text = title.content<Block.Content.Text>().text,
                    isFocused = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                ),
                BlockView.Text.Paragraph(
                    id = block.id,
                    text = block.content<Block.Content.Text>().text,
                    decorations = listOf(BlockView.Decoration())
                ),
                BlockView.DataView.Default(
                    id = dv.id,
                    title = null,
                    background = ThemeColor.DEFAULT,
                    isSelected = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT,
                    decorations = listOf(BlockView.Decoration(style = BlockView.Decoration.Style.Card)),
                    isCollection = true
                )
            )
        )

        // TESTING
        vm.onStart(id = root, space = defaultSpace)

        assertEquals(
            expected = state,
            actual = vm.state.value
        )
    }

    @Test
    fun `should render EmptySource collection dataView block when targetObjectId is empty `() = runBlocking {

        // SETUP
        stubInterceptThreadStatus()
        val params = InterceptEvents.Params(context = root)
        val targetObjectId = ""
        val dv = StubDataViewBlock(targetObjectId = targetObjectId, isCollection = true)
        val page = Block(
            id = root,
            fields = Block.Fields.empty(),
            children = listOf(header.id, block.id, dv.id),
            content = Block.Content.Smart
        )

        stubOpenDocument(document = listOf(page, header, title, block, dv))
        stubInterceptEvents(params = params)

        val vm = buildViewModel()

        //EXPECTING
        val state = ViewState.Success(
            blocks = listOf(
                BlockView.Title.Basic(
                    id = title.id,
                    text = title.content<Block.Content.Text>().text,
                    isFocused = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                ),
                BlockView.Text.Paragraph(
                    id = block.id,
                    text = block.content<Block.Content.Text>().text,
                    decorations = listOf(BlockView.Decoration())
                ),
                BlockView.DataView.EmptySource(
                    id = dv.id,
                    title = null,
                    background = ThemeColor.DEFAULT,
                    isSelected = false,
                    icon = ObjectIcon.None,
                    decorations = listOf(BlockView.Decoration(style = BlockView.Decoration.Style.Card)),
                    isCollection = true
                )
            )
        )

        // TESTING
        vm.onStart(id = root, space = defaultSpace)

        assertEquals(
            expected = state,
            actual = vm.state.value
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when getting event for set target object id for data view block, should render as empty data inline set block`() =
        runBlocking {

            // SETUP
            stubInterceptThreadStatus()
            val params = InterceptEvents.Params(context = root)
            val emptyViewer = StubDataViewView(
                name = "",
                viewerRelations = emptyList(),
                sorts = emptyList()
            )
            val dv = StubDataViewBlock(viewers = listOf(emptyViewer))
            val page = Block(
                id = root,
                fields = Block.Fields.empty(),
                children = listOf(header.id, block.id, dv.id),
                content = Block.Content.Smart
            )

            val events = flow<List<Event>> {
                delay(DELAY)
                emit(
                    listOf(
                        Event.Command.DataView.SetTargetObjectId(
                            context = root,
                            dv = dv.id,
                            targetObjectId = root
                        )
                    )
                )
            }

            stubOpenDocument(
                document = listOf(page, header, title, block, dv),
                details = ObjectViewDetails(
                    details = mapOf(
                        root to mapOf(
                            Relations.ID to root,
                            Relations.UNIQUE_KEY to root,
                            Relations.RECOMMENDED_LAYOUT to ObjectType.Layout.BASIC.code.toDouble(),
                            Relations.TYPE to listOf(objType.id)
                        )
                    )
                )
            )
            stubInterceptEvents(
                params = params,
                flow = events
            )

            val vm = buildViewModel()

            //EXPECTING
            val state = ViewState.Success(
                blocks = listOf(
                    BlockView.Title.Basic(
                        id = title.id,
                        text = title.content<Block.Content.Text>().text,
                        isFocused = false,
                        icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                    ),
                    BlockView.Text.Paragraph(
                        id = block.id,
                        text = block.content<Block.Content.Text>().text,
                        decorations = listOf(BlockView.Decoration())
                    ),
                    BlockView.DataView.EmptyData(
                        id = dv.id,
                        title = null,
                        background = ThemeColor.DEFAULT,
                        isSelected = false,
                        icon = ObjectIcon.TypeIcon.Fallback.DEFAULT,
                        decorations = listOf(BlockView.Decoration(style = BlockView.Decoration.Style.Card)),
                        isCollection = false
                    )
                )
            )

            // TESTING
            vm.onStart(id = root, space = defaultSpace)

            coroutineTestRule.advanceTime(DELAY)

            assertEquals(
                expected = state,
                actual = vm.state.value
            )
        }

    companion object {
        private const val DELAY = 100L
    }
}