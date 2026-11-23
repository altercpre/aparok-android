package io.aparok.app.presentation.editor.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.ext.content
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.editor.render.parseThemeBackgroundColor
import io.aparok.app.presentation.util.CoroutinesTestRule
import io.aparok.app.test_utils.MockDataFactory
import com.jraska.livedata.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import io.aparok.app.core_models.ui.ObjectIcon

class EditorGranularChangeTest : EditorPresentationTestSetup() {

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

    @Test
    fun `should update checkbox via granular change event`() {

        // SETUP

        val delay = 1000L

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

        val checkbox = Block(
            id = MockDataFactory.randomString(),
            fields = Block.Fields.empty(),
            children = emptyList(),
            content = Block.Content.Text(
                text = MockDataFactory.randomString(),
                isChecked = false,
                marks = emptyList(),
                style = Block.Content.Text.Style.CHECKBOX
            )
        )

        val doc = listOf(
            Block(
                id = root,
                fields = Block.Fields(emptyMap()),
                content = Block.Content.Smart,
                children = listOf(header.id, checkbox.id)
            ),
            header,
            title,
            checkbox
        )

        val events = flow<List<Event>> {
            delay(delay)
            emit(
                listOf(
                    Event.Command.GranularChange(
                        context = root,
                        id = checkbox.id,
                        checked = true
                    )
                )
            )
        }

        stubOpenDocument(doc)
        stubInterceptEvents(flow = events)

        val vm = buildViewModel()

        // Expected value before granular change vent

        val before = ViewState.Success(
            blocks = listOf(
                BlockView.Title.Basic(
                    id = title.id,
                    text = title.content<Block.Content.Text>().text,
                    isFocused = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                ),
                BlockView.Text.Checkbox(
                    id = checkbox.id,
                    text = checkbox.content<Block.Content.Text>().text,
                    decorations = listOf(
                        BlockView.Decoration(
                            background = checkbox.parseThemeBackgroundColor()
                        )
                    )
                )
            )
        )

        // Expected value after granular change vent

        val after = ViewState.Success(
            blocks = listOf(
                BlockView.Title.Basic(
                    id = title.id,
                    text = title.content<Block.Content.Text>().text,
                    isFocused = false,
                    icon = ObjectIcon.TypeIcon.Fallback.DEFAULT
                ),
                BlockView.Text.Checkbox(
                    id = checkbox.id,
                    text = checkbox.content<Block.Content.Text>().text,
                    isChecked = true,
                    decorations = listOf(
                        BlockView.Decoration(
                            background = checkbox.parseThemeBackgroundColor()
                        )
                    )
                )
            )
        )

        // TESTING

        vm.onStart(id = root, space = defaultSpace)

        // Checking that checkbox is not checked

        vm.state.test().assertValue(before)

        // Moving time forward to receive granular change event

        coroutineTestRule.advanceTime(delay)

        // Checking that checkbox is checked

        vm.state.test().assertValue(after)
    }
}