package io.aparok.app.presentation.relations

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.DVViewer
import io.aparok.app.core_models.Relation
import io.aparok.app.core_models.StubRelationObject
import io.aparok.app.presentation.sets.model.SimpleRelationView
import io.aparok.app.test_utils.MockDataFactory
import org.junit.Test
import kotlin.test.assertEquals

class ObjectSetRenderMapperTest {


    @Test
    fun `should return relation view with visibility true`() {

        val viewerRel1 = Block.Content.DataView.Viewer.ViewerRelation(
            key = MockDataFactory.randomUuid(),
            isVisible = true
        )

        val viewerRel2 = Block.Content.DataView.Viewer.ViewerRelation(
            key = MockDataFactory.randomUuid(),
            isVisible = true
        )

        val viewerRel3 = Block.Content.DataView.Viewer.ViewerRelation(
            key = MockDataFactory.randomUuid(),
            isVisible = true
        )

        val viewerRelations = listOf(viewerRel1, viewerRel2, viewerRel3)

        val relation = StubRelationObject(
            key = viewerRel2.key,
            name = MockDataFactory.randomString(),
            format = Relation.Format.NUMBER
        )

        val viewer1 = DVViewer(
            id = MockDataFactory.randomUuid(),
            filters = emptyList(),
            sorts = emptyList(),
            type = Block.Content.DataView.Viewer.Type.GRID,
            name = MockDataFactory.randomString(),
            viewerRelations = viewerRelations
        )

        val result = viewer1.toViewRelation(relation)

        val expected = SimpleRelationView(
            key = relation.key,
            title = relation.name.orEmpty(),
            format = relation.format,
            isVisible = true,
            isHidden = false
        )

        assertEquals(expected, result)
    }

    @Test
    fun `should return relation view with visibility false`() {

        val viewerRel1 = Block.Content.DataView.Viewer.ViewerRelation(
            key = MockDataFactory.randomUuid(),
            isVisible = true
        )

        val viewerRel2 = Block.Content.DataView.Viewer.ViewerRelation(
            key = MockDataFactory.randomUuid(),
            isVisible = true
        )

        val viewerRel3 = Block.Content.DataView.Viewer.ViewerRelation(
            key = MockDataFactory.randomUuid(),
            isVisible = true
        )

        val viewerRelations = listOf(viewerRel1, viewerRel2, viewerRel3)

        val relation = StubRelationObject(
            key = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            format = Relation.Format.NUMBER
        )

        val viewer1 = DVViewer(
            id = MockDataFactory.randomUuid(),
            filters = emptyList(),
            sorts = emptyList(),
            type = Block.Content.DataView.Viewer.Type.GRID,
            name = MockDataFactory.randomString(),
            viewerRelations = viewerRelations
        )

        val result = viewer1.toViewRelation(relation)

        val expected = SimpleRelationView(
            key = relation.key,
            title = relation.name.orEmpty(),
            format = relation.format,
            isVisible = false,
            isHidden = false
        )

        assertEquals(expected, result)
    }
}