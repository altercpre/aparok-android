package io.aparok.app.presentation.editor.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Position
import io.aparok.app.core_models.StubHeader
import io.aparok.app.core_models.StubParagraph
import io.aparok.app.core_models.StubTitle
import io.aparok.app.domain.block.interactor.CreateBlock
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.editor.editor.slash.SlashItem
import io.aparok.app.presentation.util.CoroutinesTestRule
import io.aparok.app.test_utils.MockDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class EditorCreateBlockTest : EditorPresentationTestSetup() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        proceedWithDefaultBeforeTestStubbing()
    }

    val title = StubTitle()
    val header = StubHeader(children = listOf(title.id))
    val a = StubParagraph(text = "")

    @Test
    fun `should create default link_to_object block with text style, small icon and no description`() =
        runTest {

            // SETUP
            val page = Block(
                id = root,
                fields = Block.Fields(emptyMap()),
                content = Block.Content.Smart,
                children = listOf(header.id, a.id)
            )

            val document = listOf(page, header, title, a)

            stubOpenDocument(document = document)
            stubInterceptEvents()
            stubInterceptThreadStatus()
            stubUpdateText()
            stubCreateBlock(root)

            val vm = buildViewModel()

            // TESTING

            vm.onStart(id  = root, space = defaultSpace)
            vm.onBlockFocusChanged(id = a.id, hasFocus = true)
            vm.onSelectionChanged(
                id = a.id,
                selection = IntRange(1, 1)
            )
            val blockView = BlockView.Text.Paragraph(id = a.id, text = "/")
            vm.onTextBlockTextChanged(view = blockView)
            vm.onSlashItemClicked(item = SlashItem.Actions.LinkTo)

            val linkToObject = MockDataFactory.randomUuid()

            vm.proceedWithLinkToAction(
                link = linkToObject,
                target = a.id,
                isBookmark = false
            )

            coroutineTestRule.advanceUntilIdle()

            //VERIFY
            verify(createBlock, times(1)).async(
                params = eq(
                    CreateBlock.Params(
                        context = root,
                        target = a.id,
                        position = Position.BOTTOM,
                        prototype = Block.Prototype.Link(
                            target = linkToObject,
                            cardStyle = Block.Content.Link.CardStyle.CARD,
                            iconSize = Block.Content.Link.IconSize.SMALL,
                            description = Block.Content.Link.Description.NONE
                        )
                    )
                )
            )
        }
}