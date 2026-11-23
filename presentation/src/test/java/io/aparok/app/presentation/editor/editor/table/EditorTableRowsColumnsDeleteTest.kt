package io.aparok.app.presentation.editor.editor.table

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.aparok.app.core_models.StubHeader
import io.aparok.app.core_models.StubLayoutColumns
import io.aparok.app.core_models.StubLayoutRows
import io.aparok.app.core_models.StubSmartBlock
import io.aparok.app.core_models.StubTable
import io.aparok.app.core_models.StubTableCells
import io.aparok.app.core_models.StubTableColumns
import io.aparok.app.core_models.StubTableRows
import io.aparok.app.core_models.StubTitle
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.editor.editor.EditorPresentationTestSetup
import io.aparok.app.presentation.editor.editor.control.ControlPanelState
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.util.DefaultCoroutineTestRule
import io.aparok.app.test_utils.MockDataFactory
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class EditorTableRowsColumnsDeleteTest : EditorPresentationTestSetup() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = DefaultCoroutineTestRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        proceedWithDefaultBeforeTestStubbing()
    }

    private var tableId = MockDataFactory.randomUuid()

    @Test
    fun `when table contains only one row table widget shouldn't has delete button`() = runTest {
        //SETUP
        val columns = StubTableColumns(size = 4)
        val rows = StubTableRows(size = 1)
        val cells = StubTableCells(columns = columns, rows = rows)
        val columnLayout = StubLayoutColumns(children = columns.map { it.id })
        val rowLayout = StubLayoutRows(children = rows.map { it.id })
        val table = StubTable(id = tableId, children = listOf(columnLayout.id, rowLayout.id))
        val title = StubTitle()
        val header = StubHeader(children = listOf(title.id))
        val page = StubSmartBlock(id = root, children = listOf(header.id, table.id))
        val document =
            listOf(page, header, title, table, columnLayout, rowLayout) + columns + rows + cells
        stubInterceptEvents()
        stubOpenDocument(document)

        //TESTING Focus Cell[0] - Enter Table Mode - Click Tab ROW
        val vm = buildViewModel()
        vm.apply {
            onStart(id = root, space = defaultSpace)
            advanceUntilIdle()
            onBlockFocusChanged(
                id = cells[0].id,
                hasFocus = true
            )
            advanceUntilIdle()
            onBlockToolbarBlockActionsClicked()
            advanceUntilIdle()
            onBlockFocusChanged(
                id = cells[0].id,
                hasFocus = false
            )
            advanceUntilIdle()
            onSimpleTableWidgetItemClicked(
                item = SimpleTableWidgetItem.Tab.Row
            )
            advanceUntilIdle()
        }

        //EXPECTED
        val expectedMode = Editor.Mode.Table(
            tableId = table.id,
            initialTargets = setOf(cells[0].id),
            targets = setOf(
                cells[0].id,
                cells[1].id,
                cells[2].id,
                cells[3].id
            ),
            tab = BlockView.Table.Tab.ROW
        )
        val expectedSelectedState = listOf(
            cells[0].id,
            cells[1].id,
            cells[2].id,
            cells[3].id
        )

        val row1 = BlockView.Table.RowId(rows[0].id)
        val ids = listOf(row1)

        val expectedSimpleTableWidget = ControlPanelState.Toolbar.SimpleTableWidget(
            isVisible = true,
            tableId = tableId,
            items = listOf(
                SimpleTableWidgetItem.Row.InsertAbove(row1),
                SimpleTableWidgetItem.Row.InsertBelow(row1),
                SimpleTableWidgetItem.Row.Duplicate(row1),
                SimpleTableWidgetItem.Row.ClearContents(ids),
                SimpleTableWidgetItem.Row.Color(ids),
                SimpleTableWidgetItem.Row.Style(ids),
                SimpleTableWidgetItem.Row.ResetStyle(ids)
            ),
            selectedCount = 1,
            tab = BlockView.Table.Tab.ROW
        )

        //ASSERT
        assertEquals(
            expected = expectedSelectedState,
            actual = vm.currentSelection().toList()
        )
        assertEquals(
            expected = expectedMode,
            actual = vm.mode as Editor.Mode.Table
        )
        assertEquals(
            expected = expectedSimpleTableWidget,
            actual = vm.controlPanelViewState.value?.simpleTableWidget
        )
    }

    @Test
    fun `when table contains only one column table widget shouldn't has delete button`() = runTest {
        //SETUP
        val columns = StubTableColumns(size = 1)
        val rows = StubTableRows(size = 3)
        val cells = StubTableCells(columns = columns, rows = rows)
        val columnLayout = StubLayoutColumns(children = columns.map { it.id })
        val rowLayout = StubLayoutRows(children = rows.map { it.id })
        val table = StubTable(id = tableId, children = listOf(columnLayout.id, rowLayout.id))
        val title = StubTitle()
        val header = StubHeader(children = listOf(title.id))
        val page = StubSmartBlock(id = root, children = listOf(header.id, table.id))
        val document =
            listOf(page, header, title, table, columnLayout, rowLayout) + columns + rows + cells
        stubInterceptEvents()
        stubOpenDocument(document)

        //TESTING Focus Cell[0] - Enter Table Mode - Click Tab COLUMN
        val vm = buildViewModel()
        vm.apply {
            onStart(id = root, space = defaultSpace)
            advanceUntilIdle()
            onBlockFocusChanged(
                id = cells[0].id,
                hasFocus = true
            )
            advanceUntilIdle()
            onBlockToolbarBlockActionsClicked()
            advanceUntilIdle()
            onBlockFocusChanged(
                id = cells[0].id,
                hasFocus = false
            )
            advanceUntilIdle()
            onSimpleTableWidgetItemClicked(
                item = SimpleTableWidgetItem.Tab.Column
            )
            advanceUntilIdle()
        }

        //EXPECTED
        val expectedMode = Editor.Mode.Table(
            tableId = table.id,
            initialTargets = setOf(cells[0].id),
            targets = setOf(
                cells[0].id,
                cells[1].id,
                cells[2].id
            ),
            tab = BlockView.Table.Tab.COLUMN
        )
        val expectedSelectedState = listOf(
            cells[0].id,
            cells[1].id,
            cells[2].id
        )

        val column1 = BlockView.Table.ColumnId(columns[0].id)
        val ids = listOf(column1)

        val expectedSimpleTableWidget = ControlPanelState.Toolbar.SimpleTableWidget(
            isVisible = true,
            tableId = tableId,
            items = listOf(
                SimpleTableWidgetItem.Column.InsertLeft(column1),
                SimpleTableWidgetItem.Column.InsertRight(column1),
                SimpleTableWidgetItem.Column.Duplicate(column1),
                SimpleTableWidgetItem.Column.ClearContents(ids),
                SimpleTableWidgetItem.Column.Color(ids),
                SimpleTableWidgetItem.Column.Style(ids),
                SimpleTableWidgetItem.Column.ResetStyle(ids)
            ),
            selectedCount = 1,
            tab = BlockView.Table.Tab.COLUMN
        )

        //ASSERT
        assertEquals(
            expected = expectedSelectedState,
            actual = vm.currentSelection().toList()
        )
        assertEquals(
            expected = expectedMode,
            actual = vm.mode as Editor.Mode.Table
        )
        assertEquals(
            expected = expectedSimpleTableWidget,
            actual = vm.controlPanelViewState.value?.simpleTableWidget
        )
    }
}