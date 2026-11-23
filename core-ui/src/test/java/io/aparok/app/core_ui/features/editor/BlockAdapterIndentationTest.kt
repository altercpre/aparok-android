package io.aparok.app.core_ui.features.editor

import android.os.Build
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.BuildConfig
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.StubParagraphView
import io.aparok.app.core_ui.features.editor.holders.text.*
import io.aparok.app.core_utils.ext.dimen
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_CHECKBOX
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_HEADER_ONE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_HEADER_THREE
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_HEADER_TWO
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_NUMBERED
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_PARAGRAPH
import io.aparok.app.test_utils.MockDataFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class BlockAdapterIndentationTest : BlockAdapterTestSetup() {

    @Test
    fun `should update indentation for paragraph`() {

        // Setup

        val padding = context.dimen(R.dimen.default_document_content_padding_start).toInt()
        val indent = context.dimen(R.dimen.indent)

        val block = StubParagraphView(indent = 0)

        val views = listOf(block)

        val updated = listOf(block.copy(indent = 2))

        val adapter = buildAdapter(views)

        val recycler = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }

        val holder = adapter.onCreateViewHolder(recycler, HOLDER_PARAGRAPH)

        adapter.onBindViewHolder(holder, 0)

        check(holder is Paragraph)

        // Testing

        assertEquals(
            actual = holder.content.paddingLeft,
            expected = padding
        )

        val payload: MutableList<Any> = mutableListOf(
            BlockViewDiffUtil.Payload(
                changes = listOf(BlockViewDiffUtil.INDENT_CHANGED)
            )
        )

        adapter.updateWithDiffUtil(updated)

        adapter.onBindViewHolder(holder, 0, payloads = payload)

        // TODO check apply decorations
    }

    @Test
    fun `should update indentation for header1`() {

        // Setup

        val padding = context.dimen(R.dimen.default_document_content_padding_start).toInt()
        val indent = context.dimen(R.dimen.indent)

        val block = BlockView.Text.Header.One(
            id = MockDataFactory.randomUuid(),
            text = MockDataFactory.randomString(),
            indent = 0
        )

        val views = listOf(block)

        val updated = listOf(block.copy(indent = 2))

        val adapter = buildAdapter(views)

        val recycler = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }

        val holder = adapter.onCreateViewHolder(recycler, HOLDER_HEADER_ONE)

        adapter.onBindViewHolder(holder, 0)

        check(holder is HeaderOne)

        // Testing

        assertEquals(
            actual = holder.content.paddingLeft,
            expected = padding
        )

        val payload: MutableList<Any> = mutableListOf(
            BlockViewDiffUtil.Payload(
                changes = listOf(BlockViewDiffUtil.INDENT_CHANGED)
            )
        )

        adapter.updateWithDiffUtil(updated)

        adapter.onBindViewHolder(holder, 0, payloads = payload)

        // TODO check apply decorations
    }

    @Test
    fun `should update indentation for header2`() {

        // Setup

        val padding = context.dimen(R.dimen.default_document_content_padding_start).toInt()
        val indent = context.dimen(R.dimen.indent)

        val block = BlockView.Text.Header.Two(
            id = MockDataFactory.randomUuid(),
            text = MockDataFactory.randomString(),
            indent = 0
        )

        val views = listOf(block)

        val updated = listOf(block.copy(indent = 2))

        val adapter = buildAdapter(views)

        val recycler = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }

        val holder = adapter.onCreateViewHolder(recycler, HOLDER_HEADER_TWO)

        adapter.onBindViewHolder(holder, 0)

        check(holder is HeaderTwo)

        // Testing

        assertEquals(
            actual = holder.content.paddingLeft,
            expected = padding
        )

        val payload: MutableList<Any> = mutableListOf(
            BlockViewDiffUtil.Payload(
                changes = listOf(BlockViewDiffUtil.INDENT_CHANGED)
            )
        )

        adapter.updateWithDiffUtil(updated)

        adapter.onBindViewHolder(holder, 0, payloads = payload)

        // TODO check apply decorations
    }

    @Test
    fun `should update indentation for header3`() {

        // Setup

        val padding = context.dimen(R.dimen.default_document_content_padding_start).toInt()
        val indent = context.dimen(R.dimen.indent)

        val block = BlockView.Text.Header.Three(
            id = MockDataFactory.randomUuid(),
            text = MockDataFactory.randomString(),
            indent = 0
        )

        val views = listOf(block)

        val updated = listOf(block.copy(indent = 2))

        val adapter = buildAdapter(views)

        val recycler = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }

        val holder = adapter.onCreateViewHolder(recycler, HOLDER_HEADER_THREE)

        adapter.onBindViewHolder(holder, 0)

        check(holder is HeaderThree)

        // Testing

        assertEquals(
            actual = holder.content.paddingLeft,
            expected = padding
        )

        val payload: MutableList<Any> = mutableListOf(
            BlockViewDiffUtil.Payload(
                changes = listOf(BlockViewDiffUtil.INDENT_CHANGED)
            )
        )

        adapter.updateWithDiffUtil(updated)

        adapter.onBindViewHolder(holder, 0, payloads = payload)

        // TODO check apply decorations
    }

    @Test
    fun `should update indentation for checkbox`() {

        // Setup

        val indent = context.dimen(R.dimen.indent)

        val block = BlockView.Text.Checkbox(
            id = MockDataFactory.randomUuid(),
            text = MockDataFactory.randomString(),
            indent = 0
        )

        val views = listOf(block)

        val updated = listOf(block.copy(indent = 2))

        val adapter = buildAdapter(views)

        val recycler = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }

        val holder = adapter.onCreateViewHolder(recycler, HOLDER_CHECKBOX)

        adapter.onBindViewHolder(holder, 0)

        check(holder is Checkbox)

        // Testing

        assertEquals(
            actual = holder.checkbox.paddingLeft,
            expected = 0
        )

        val payload: MutableList<Any> = mutableListOf(
            BlockViewDiffUtil.Payload(
                changes = listOf(BlockViewDiffUtil.INDENT_CHANGED)
            )
        )

        adapter.updateWithDiffUtil(updated)

        adapter.onBindViewHolder(holder, 0, payloads = payload)

        // TODO check apply decorations
    }

    @Test
    fun `should update indentation for numbered block`() {

        // Setup

        val indent = context.dimen(R.dimen.indent)

        val block = BlockView.Text.Numbered(
            id = MockDataFactory.randomUuid(),
            text = MockDataFactory.randomString(),
            indent = 0,
            number = 1
        )

        val views = listOf(block)

        val updated = listOf(block.copy(indent = 2))

        val adapter = buildAdapter(views)

        val recycler = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }

        val holder = adapter.onCreateViewHolder(recycler, HOLDER_NUMBERED)

        adapter.onBindViewHolder(holder, 0)

        check(holder is Numbered)

        // Testing

        assertEquals(
            actual = holder.number.marginLeft,
            expected = 0
        )

        val payload: MutableList<Any> = mutableListOf(
            BlockViewDiffUtil.Payload(
                changes = listOf(BlockViewDiffUtil.INDENT_CHANGED)
            )
        )

        adapter.updateWithDiffUtil(updated)

        adapter.onBindViewHolder(holder, 0, payloads = payload)

        // TODO check apply decorations
    }
}