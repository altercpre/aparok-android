package io.aparok.app.features.sets.dv

import androidx.core.os.bundleOf
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.aparok.app.R
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.DVViewerRelation
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.Relation
import io.aparok.app.emojifier.data.DefaultDocumentEmojiIconProvider
import io.aparok.app.presentation.MockBlockContentFactory.StubTextContent
import io.aparok.app.presentation.relations.ObjectSetConfig
import io.aparok.app.test_utils.MockDataFactory
import io.aparok.app.test_utils.utils.checkHasChildViewCount
import io.aparok.app.test_utils.utils.checkHasChildViewWithText
import io.aparok.app.test_utils.utils.checkHasText
import io.aparok.app.test_utils.utils.onItemView
import io.aparok.app.test_utils.utils.rVMatcher
import io.aparok.app.ui.sets.ObjectSetFragment
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ObjectSetGridNumberCellRenderingTest : TestObjectSetSetup() {

    @get:Rule
    val animationsRule = DisableAnimationsRule()

    override val title: Block = Block(
        id = MockDataFactory.randomUuid(),
        content = StubTextContent(
            style = Block.Content.Text.Style.TITLE,
            text = "Data View UI Testing",
            marks = emptyList()
        ),
        children = emptyList(),
        fields = Block.Fields.empty()
    )

    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun shouldRenderEmptyNumberCellWhenValueIsNull() {

        // SETUP

        val relationName = "Number"
        val valueText = null

        val relationId = MockDataFactory.randomUuid()
        val record1Id = MockDataFactory.randomUuid()

        val objectType = ObjectType(
            url = MockDataFactory.randomUuid(),
            name = "Movie",
            relationLinks = emptyList(),
            emoji = MockDataFactory.randomString(),
            layout = ObjectType.Layout.PROFILE,
            description = "",
            isHidden = false,
            isArchived = false,
            isReadOnly = false
        )

        val record1: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to record1Id,
            ObjectSetConfig.TYPE_KEY to objectType.url,
            ObjectSetConfig.NAME_KEY to "The Great Dictator",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            relationId to valueText
        )

        val relation = Relation(
            key = relationId,
            name = relationName,
            format = Relation.Format.NUMBER,
            source = Relation.Source.values().random()
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = listOf(
                DVViewerRelation(
                    key = relation.key,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val dataview = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),
                
            )
        )

        val root = Block(
            id = ctx,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, dataview.id)
        )

        val set = listOf(root, header, title, dataview)

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubOpenObjectSetWithRecord(
            set = set
        )

        // TESTING

        launchFragment(bundleOf(ObjectSetFragment.CONTEXT_ID_KEY to ctx))

        with(R.id.rvRows.rVMatcher()) {
            onItemView(0, R.id.tvTitle).checkHasText("The Great Dictator")
            onItemView(0, R.id.rowCellRecycler)
                .checkHasChildViewCount(1)
                .checkHasChildViewWithText(
                    pos = 0,
                    target = R.id.tvText,
                    text = ""
                )
        }
    }

    @Test
    fun shouldRenderEmptyNumberCellWhenValueIsString() {

        // SETUP

        val relationName = "Number"
        val valueText = "x2021"

        val relationId = MockDataFactory.randomUuid()
        val record1Id = MockDataFactory.randomUuid()

        val objectType = ObjectType(
            url = MockDataFactory.randomUuid(),
            name = "Movie",
            relationLinks = emptyList(),
            emoji = MockDataFactory.randomString(),
            layout = ObjectType.Layout.PROFILE,
            description = "",
            isHidden = false,
            isArchived = false,
            isReadOnly = false
        )

        val record1: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to record1Id,
            ObjectSetConfig.TYPE_KEY to objectType.url,
            ObjectSetConfig.NAME_KEY to "The Great Dictator",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            relationId to valueText
        )

        val relation = Relation(
            key = relationId,
            name = relationName,
            format = Relation.Format.NUMBER,
            source = Relation.Source.values().random()
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = listOf(
                DVViewerRelation(
                    key = relation.key,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val dataview = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),
                
            )
        )

        val root = Block(
            id = ctx,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, dataview.id)
        )

        val set = listOf(root, header, title, dataview)

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubOpenObjectSetWithRecord(
            set = set,

        )

        // TESTING

        launchFragment(bundleOf(ObjectSetFragment.CONTEXT_ID_KEY to ctx))

        with(R.id.rvRows.rVMatcher()) {
            onItemView(0, R.id.tvTitle).checkHasText("The Great Dictator")
            onItemView(0, R.id.rowCellRecycler)
                .checkHasChildViewCount(1)
                .checkHasChildViewWithText(
                    pos = 0,
                    target = R.id.tvText,
                    text = ""
                )
        }
    }

    @Test
    fun shouldRenderNumberCellWhenValueIsDoubleString() {

        // SETUP

        val relationName = "Number"
        val valueText = "1234.012"

        val relationId = MockDataFactory.randomUuid()
        val record1Id = MockDataFactory.randomUuid()

        val objectType = ObjectType(
            url = MockDataFactory.randomUuid(),
            name = "Movie",
            relationLinks = emptyList(),
            emoji = MockDataFactory.randomString(),
            layout = ObjectType.Layout.PROFILE,
            description = "",
            isHidden = false,
            isArchived = false,
            isReadOnly = false
        )

        val record1: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to record1Id,
            ObjectSetConfig.TYPE_KEY to objectType.url,
            ObjectSetConfig.NAME_KEY to "The Great Dictator",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            relationId to valueText
        )

        val relation = Relation(
            key = relationId,
            name = relationName,
            format = Relation.Format.NUMBER,
            source = Relation.Source.values().random()
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = listOf(
                DVViewerRelation(
                    key = relation.key,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val dataview = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),
                
            )
        )

        val root = Block(
            id = ctx,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, dataview.id)
        )

        val set = listOf(root, header, title, dataview)

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubOpenObjectSetWithRecord(
            set = set,

        )

        // TESTING

        launchFragment(bundleOf(ObjectSetFragment.CONTEXT_ID_KEY to ctx))

        with(R.id.rvRows.rVMatcher()) {
            onItemView(0, R.id.tvTitle).checkHasText("The Great Dictator")
            onItemView(0, R.id.rowCellRecycler)
                .checkHasChildViewCount(1)
                .checkHasChildViewWithText(
                    pos = 0,
                    target = R.id.tvText,
                    text = "1234.012"
                )
        }
    }

    @Test
    fun shouldRenderNumberCellWhenValueIsWholeDoubleString() {

        // SETUP

        val relationName = "Number"
        val valueText = "1234.0"

        val relationId = MockDataFactory.randomUuid()
        val record1Id = MockDataFactory.randomUuid()

        val objectType = ObjectType(
            url = MockDataFactory.randomUuid(),
            name = "Movie",
            relationLinks = emptyList(),
            emoji = MockDataFactory.randomString(),
            layout = ObjectType.Layout.PROFILE,
            description = "",
            isHidden = false,
            isArchived = false,
            isReadOnly = false
        )

        val record1: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to record1Id,
            ObjectSetConfig.TYPE_KEY to objectType.url,
            ObjectSetConfig.NAME_KEY to "The Great Dictator",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            relationId to valueText
        )

        val relation = Relation(
            key = relationId,
            name = relationName,
            format = Relation.Format.NUMBER,
            source = Relation.Source.values().random()
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = listOf(
                DVViewerRelation(
                    key = relation.key,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val dataview = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),
                
            )
        )

        val root = Block(
            id = ctx,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, dataview.id)
        )

        val set = listOf(root, header, title, dataview)

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubOpenObjectSetWithRecord(
            set = set,

        )

        // TESTING

        launchFragment(bundleOf(ObjectSetFragment.CONTEXT_ID_KEY to ctx))

        with(R.id.rvRows.rVMatcher()) {
            onItemView(0, R.id.tvTitle).checkHasText("The Great Dictator")
            onItemView(0, R.id.rowCellRecycler)
                .checkHasChildViewCount(1)
                .checkHasChildViewWithText(
                    pos = 0,
                    target = R.id.tvText,
                    text = "1234"
                )
        }
    }

    @Test
    fun shouldRenderNumberCellWhenValueIsDouble() {

        // SETUP

        val relationName = "Number"
        val valueText = 1234.0564321

        val relationId = MockDataFactory.randomUuid()
        val record1Id = MockDataFactory.randomUuid()

        val objectType = ObjectType(
            url = MockDataFactory.randomUuid(),
            name = "Movie",
            relationLinks = emptyList(),
            emoji = MockDataFactory.randomString(),
            layout = ObjectType.Layout.PROFILE,
            description = "",
            isHidden = false,
            isArchived = false,
            isReadOnly = false
        )

        val record1: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to record1Id,
            ObjectSetConfig.TYPE_KEY to objectType.url,
            ObjectSetConfig.NAME_KEY to "The Great Dictator",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            relationId to valueText
        )

        val relation = Relation(
            key = relationId,
            name = relationName,
            format = Relation.Format.NUMBER,
            source = Relation.Source.values().random()
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = listOf(
                DVViewerRelation(
                    key = relation.key,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val dataview = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),
                
            )
        )

        val root = Block(
            id = ctx,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, dataview.id)
        )

        val set = listOf(root, header, title, dataview)

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubOpenObjectSetWithRecord(
            set = set,

        )

        // TESTING

        launchFragment(bundleOf(ObjectSetFragment.CONTEXT_ID_KEY to ctx))

        with(R.id.rvRows.rVMatcher()) {
            onItemView(0, R.id.tvTitle).checkHasText("The Great Dictator")
            onItemView(0, R.id.rowCellRecycler)
                .checkHasChildViewCount(1)
                .checkHasChildViewWithText(
                    pos = 0,
                    target = R.id.tvText,
                    text = "1234.0564321"
                )
        }
    }

    @Test
    fun shouldRenderNumberCellWhenValueIsWholeDouble() {

        // SETUP

        val relationName = "Number"
        val valueText = -1234.0

        val relationId = MockDataFactory.randomUuid()
        val record1Id = MockDataFactory.randomUuid()

        val objectType = ObjectType(
            url = MockDataFactory.randomUuid(),
            name = "Movie",
            relationLinks = emptyList(),
            emoji = MockDataFactory.randomString(),
            layout = ObjectType.Layout.PROFILE,
            description = "",
            isHidden = false,
            isArchived = false,
            isReadOnly = false
        )

        val record1: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to record1Id,
            ObjectSetConfig.TYPE_KEY to objectType.url,
            ObjectSetConfig.NAME_KEY to "The Great Dictator",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            relationId to valueText
        )

        val relation = Relation(
            key = relationId,
            name = relationName,
            format = Relation.Format.NUMBER,
            source = Relation.Source.values().random()
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = listOf(
                DVViewerRelation(
                    key = relation.key,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val dataview = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),
                
            )
        )

        val root = Block(
            id = ctx,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, dataview.id)
        )

        val set = listOf(root, header, title, dataview)

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubOpenObjectSetWithRecord(
            set = set,

        )

        // TESTING

        launchFragment(bundleOf(ObjectSetFragment.CONTEXT_ID_KEY to ctx))

        with(R.id.rvRows.rVMatcher()) {
            onItemView(0, R.id.tvTitle).checkHasText("The Great Dictator")
            onItemView(0, R.id.rowCellRecycler)
                .checkHasChildViewCount(1)
                .checkHasChildViewWithText(
                    pos = 0,
                    target = R.id.tvText,
                    text = "-1234"
                )
        }
    }
}