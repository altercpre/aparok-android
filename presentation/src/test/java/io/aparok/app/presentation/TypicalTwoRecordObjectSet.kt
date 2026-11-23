package io.aparok.app.presentation

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.DV
import io.aparok.app.core_models.DVViewer
import io.aparok.app.core_models.DVViewerRelation
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relation
import io.aparok.app.core_models.RelationLink
import io.aparok.app.presentation.relations.ObjectSetConfig
import io.aparok.app.test_utils.MockDataFactory

class TypicalTwoRecordObjectSet {

    val title = Block(
        id = MockDataFactory.randomUuid(),
        content = Block.Content.Text(
            style = Block.Content.Text.Style.TITLE,
            text = MockDataFactory.randomString(),
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

    val relationLinks = listOf(
        RelationLink(
            key = MockDataFactory.randomString(),
            format = Relation.Format.LONG_TEXT
        ),
        RelationLink(
            key = MockDataFactory.randomString(),
            format = Relation.Format.LONG_TEXT
        )
    )

    val vrelations = relationLinks.map { relation ->
        DVViewerRelation(
            key = relation.key,
            isVisible = true
        )
    }

    val firstRecordId = "firstRecordId"
    val secondRecordId = "secondRecordId"
    val firstRecordName = MockDataFactory.randomString()
    val secondRecordName = MockDataFactory.randomString()
    val firstRecordType = MockDataFactory.randomString()
    val secondRecordType = MockDataFactory.randomString()

    val firstRecord = mapOf(
        ObjectSetConfig.ID_KEY to firstRecordId,
        ObjectSetConfig.NAME_KEY to firstRecordName,
        ObjectSetConfig.TYPE_KEY to firstRecordType,
        relationLinks[0].key to MockDataFactory.randomString(),
        relationLinks[1].key to MockDataFactory.randomString()
    )

    val firstObject = ObjectWrapper.Basic(firstRecord)

    val secondRecord = mapOf(
        ObjectSetConfig.ID_KEY to secondRecordId,
        ObjectSetConfig.NAME_KEY to secondRecordName,
        ObjectSetConfig.TYPE_KEY to secondRecordType,
        relationLinks[0].key to MockDataFactory.randomString(),
        relationLinks[1].key to MockDataFactory.randomString()
    )

    val secondObject = ObjectWrapper.Basic(secondRecord)

    val initialObjects = listOf(firstObject, secondObject)

    val viewer1 = DVViewer(
        id = MockDataFactory.randomUuid(),
        filters = emptyList(),
        sorts = emptyList(),
        type = Block.Content.DataView.Viewer.Type.GRID,
        name = MockDataFactory.randomString(),
        viewerRelations = vrelations
    )

    val viewer2 = DVViewer(
        id = MockDataFactory.randomUuid(),
        filters = emptyList(),
        sorts = emptyList(),
        type = Block.Content.DataView.Viewer.Type.GRID,
        name = MockDataFactory.randomString(),
        viewerRelations = vrelations.mapIndexed { index, viewerRelation ->
            if (index == 0)
                viewerRelation.copy(isVisible = false)
            else
                viewerRelation
        }
    )

    val dv = Block(
        id = MockDataFactory.randomUuid(),
        content = DV(
            relationLinks = relationLinks,
            viewers = listOf(viewer1, viewer2)
        ),
        children = emptyList(),
        fields = Block.Fields.empty()
    )
}