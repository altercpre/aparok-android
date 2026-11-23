package io.aparok.app.presentation.editor.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.ext.content
import io.aparok.app.presentation.MockBlockFactory
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.editor.render.parseThemeBackgroundColor
import io.aparok.app.presentation.util.CoroutinesTestRule
import io.aparok.app.presentation.util.TXT
import io.aparok.app.test_utils.MockDataFactory
import com.jraska.livedata.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import io.aparok.app.core_models.ui.ObjectIcon

class EditorLatexBlockTest : EditorPresentationTestSetup() {

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

    val title = Block(
        id = MockDataFactory.randomUuid(),
        content = Block.Content.Text(
            text = MockDataFactory.randomString(),
            style = Block.Content.Text.Style.TITLE,
            marks = emptyList()
        ),
        children = emptyList(),
        fields = Block.Fields.empty()
    )

    val header = Block(
        id = MockDataFactory.randomUuid(),
        content = Block.Content.Layout(
            type = Block.Content.Layout.Type.HEADER
        ),
        fields = Block.Fields.empty(),
        children = listOf(title.id)
    )

    @Test
    fun `should render latex block as latex view`() {

        // SETUP

        val latex = Block(
            id = MockDataFactory.randomUuid(),
            fields = Block.Fields.empty(),
            children = emptyList(),
            content = Block.Content.Latex(
                latex = MockDataFactory.randomString()
            )
        )

        val page = Block(
            id = root,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, latex.id)
        )

        val document = listOf(page, header, title, latex)

        stubOpenDocument(document)
        stubInterceptEvents()
        stubInterceptThreadStatus()

        val vm = buildViewModel()

        vm.onStart(id = root, space = defaultSpace)

        // TESTING

        vm.state.test().apply {
            assertValue(
                ViewState.Success(
                    blocks = listOf(
                        BlockView.Title.Basic(
                            id = title.id,
                            isFocused = false,
                            text = title.content<TXT>().text,
                            mode = BlockView.Mode.EDIT,
                            icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                        ),
                        BlockView.Latex(
                            id = latex.id,
                            isSelected = false,
                            latex = latex.content<Block.Content.Latex>().latex,
                            indent = 0
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `should render latex block as latex view with indent level equal to 1`() {

        // SETUP

        val latex = Block(
            id = MockDataFactory.randomUuid(),
            fields = Block.Fields.empty(),
            children = emptyList(),
            content = Block.Content.Latex(
                latex = MockDataFactory.randomString()
            )
        )

        val p = MockBlockFactory.paragraph(
            children = listOf(latex.id)
        )

        val page = Block(
            id = root,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, p.id)
        )

        val document = listOf(page, header, title, p, latex)

        stubOpenDocument(document)
        stubInterceptEvents()
        stubInterceptThreadStatus()

        val vm = buildViewModel()

        vm.onStart(id = root, space = defaultSpace)

        // TESTING

        vm.state.test().apply {
            assertValue(
                ViewState.Success(
                    blocks = listOf(
                        BlockView.Title.Basic(
                            id = title.id,
                            isFocused = false,
                            text = title.content<TXT>().text,
                            mode = BlockView.Mode.EDIT,
                            icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                        ),
                        BlockView.Text.Paragraph(
                            id = p.id,
                            isFocused = false,
                            text = p.content<TXT>().text,
                            mode = BlockView.Mode.EDIT,
                            decorations = listOf(
                                BlockView.Decoration(
                                    background = p.parseThemeBackgroundColor()
                                )
                            )
                        ),
                        BlockView.Latex(
                            id = latex.id,
                            isSelected = false,
                            latex = latex.content<Block.Content.Latex>().latex,
                            indent = 1
                        )
                    )
                )
            )
        }
    }
}