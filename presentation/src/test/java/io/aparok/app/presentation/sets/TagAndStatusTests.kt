package io.aparok.app.presentation.sets

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relation
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.StubRelationOptionObject
import io.aparok.app.domain.config.Gateway
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.DefaultObjectStore
import io.aparok.app.domain.objects.GetDateObjectByTimestamp
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.primitives.FieldParserImpl
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.presentation.mapper.toViewerColumns
import io.aparok.app.core_models.ui.ObjectIcon
import io.aparok.app.domain.misc.UrlBuilderImpl
import io.aparok.app.presentation.relations.ObjectSetConfig
import io.aparok.app.presentation.sets.model.CellView
import io.aparok.app.presentation.sets.model.ColumnView
import io.aparok.app.presentation.sets.model.StatusView
import io.aparok.app.presentation.sets.model.TagView
import io.aparok.app.presentation.sets.model.Viewer
import io.aparok.app.test_utils.MockDataFactory
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import org.mockito.kotlin.whenever

class TagAndStatusTests {

    private val defaultSpace = MockDataFactory.randomUuid()

    @Mock
    lateinit var gateway: Gateway

    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var logger: Logger

    @Mock
    lateinit var getDateObjectByTimestamp: GetDateObjectByTimestamp

    @Mock
    lateinit var stringResourceProvider: StringResourceProvider

    @Mock
    lateinit var storeOfObjectTypes: StoreOfObjectTypes

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        fieldParser = FieldParserImpl(dateProvider, logger, getDateObjectByTimestamp, stringResourceProvider)
    }

    @Test
    fun `should create row with text and tag cells`() = runTest {

        val viewerRelations = listOf(
            Block.Content.DataView.Viewer.ViewerRelation(
                key = MockDataFactory.randomUuid(),
                isVisible = true
            ),
            Block.Content.DataView.Viewer.ViewerRelation(
                key = MockDataFactory.randomUuid(),
                isVisible = true
            )
        )

        val selOptions = listOf(
            StubRelationOptionObject(
                id = MockDataFactory.randomUuid(),
                space = defaultSpace,
                text = "Tag1",
                color = "000"
            ),
            StubRelationOptionObject(
                id = MockDataFactory.randomUuid(),
                space = defaultSpace,
                text = "Tag2",
                color = "111"
            ),
            StubRelationOptionObject(
                id = MockDataFactory.randomUuid(),
                space = defaultSpace,
                text = "Tag3",
                color = "222"
            ),
            StubRelationOptionObject(
                id = MockDataFactory.randomUuid(),
                space = defaultSpace,
                text = "Tag4",
                color = "333"
            )
        )

        whenever(stringResourceProvider.getUntitledObjectTitle()).thenReturn("Untitled")

        val recordId = MockDataFactory.randomUuid()
        val records = mapOf<String, Any?>(
            ObjectSetConfig.ID_KEY to recordId,
            Relations.SPACE_ID to defaultSpace,
            ObjectSetConfig.TYPE_KEY to "Type111",
            viewerRelations[0].key to "Title4",
            viewerRelations[1].key to listOf(selOptions[1].id, selOptions[2].id)
        )

        val dataViewRelations = listOf(
            ObjectWrapper.Relation(
                mapOf(
                    Relations.RELATION_KEY to viewerRelations[0].key,
                    Relations.SPACE_ID to defaultSpace,
                    Relations.NAME to "name",
                    Relations.RELATION_FORMAT to Relation.Format.LONG_TEXT.code.toDouble(),
                    Relations.IS_READ_ONLY to true,
                    Relations.IS_HIDDEN to false,
                    Relations.SOURCE to Relation.Source.DETAILS.name
                )
            ),
            ObjectWrapper.Relation(
                mapOf(
                    Relations.RELATION_KEY to viewerRelations[1].key,
                    Relations.SPACE_ID to defaultSpace,
                    Relations.NAME to "Tags",
                    Relations.RELATION_FORMAT to RelationFormat.TAG.code.toDouble(),
                    Relations.IS_READ_ONLY to true,
                    Relations.IS_HIDDEN to false,
                    Relations.SOURCE to Relation.Source.DETAILS.name
                )
            )
        )

        val store = DefaultObjectStore()

        store.merge(
            objects = listOf(ObjectWrapper.Basic(records)),
            dependencies = selOptions.map { ObjectWrapper.Basic(it.map) },
            subscriptions = emptyList()
        )

        //TESTING

        val columns: List<ColumnView> = viewerRelations.toViewerColumns(
            relations = dataViewRelations,
            filterBy = listOf()
        )

        val result = columns.buildGridRow(
            obj = ObjectWrapper.Basic(records),
            builder = UrlBuilderImpl(gateway),
            showIcon = false,
            store = store,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes
        )

        val expected = Viewer.GridView.Row(
            id = recordId,
            name = "Untitled",
            type = "Type111",
            showIcon = false,
            objectIcon = ObjectIcon.TypeIcon.Fallback.DEFAULT,
            cells = listOf(
                CellView.Description(
                    id = recordId,
                    relationKey = viewerRelations[0].key,
                    text = "Title4"
                ),
                CellView.Tag(
                    id = recordId,
                    relationKey = viewerRelations[1].key,
                    tags = listOf(
                        TagView(
                            id = selOptions[1].id,
                            tag = selOptions[1].name.orEmpty(),
                            color = selOptions[1].color
                        ),
                        TagView(
                            id = selOptions[2].id,
                            tag = selOptions[2].name.orEmpty(),
                            color = selOptions[2].color
                        )
                    ),
                )
            )
        )

        assertEquals(
            expected = expected,
            actual = result
        )
    }

    @Test
    fun `should create row with text and status cells`() = runTest {

        val viewerRelations = listOf(
            Block.Content.DataView.Viewer.ViewerRelation(
                key = MockDataFactory.randomUuid(),
                isVisible = true
            ),
            Block.Content.DataView.Viewer.ViewerRelation(
                key = MockDataFactory.randomUuid(),
                isVisible = true
            )
        )

        whenever(stringResourceProvider.getUntitledObjectTitle()).thenReturn("Untitled")

        val selOptions = listOf(
            StubRelationOptionObject(
                id = MockDataFactory.randomUuid(),
                space = defaultSpace,
                text = "Status1",
                color = "000"
            ),
            StubRelationOptionObject(
                id = MockDataFactory.randomUuid(),
                space = defaultSpace,
                text = "Status2",
                color = "111"
            ),
            StubRelationOptionObject(
                id = MockDataFactory.randomUuid(),
                space = defaultSpace,
                text = "Status3",
                color = "222"
            ),
            StubRelationOptionObject(
                id = MockDataFactory.randomUuid(),
                space = defaultSpace,
                text = "Status4",
                color = "333"
            )
        )

        val recordId = MockDataFactory.randomUuid()
        val records = mapOf<String, Any?>(
            ObjectSetConfig.ID_KEY to recordId,
            Relations.SPACE_ID to defaultSpace,
            ObjectSetConfig.TYPE_KEY to "Type111",
            viewerRelations[0].key to "Title4",
            viewerRelations[1].key to listOf(selOptions[2].id)
        )

        val dataViewRelations = listOf(
            ObjectWrapper.Relation(
                mapOf(
                    Relations.RELATION_KEY to viewerRelations[0].key,
                    Relations.SPACE_ID to defaultSpace,
                    Relations.NAME to "name",
                    Relations.RELATION_FORMAT to Relation.Format.LONG_TEXT.code.toDouble(),
                    Relations.IS_READ_ONLY to true,
                    Relations.IS_HIDDEN to false,
                    Relations.SOURCE to Relation.Source.DETAILS.name
                )
            ),
            ObjectWrapper.Relation(
                mapOf(
                    Relations.RELATION_KEY to viewerRelations[1].key,
                    Relations.SPACE_ID to defaultSpace,
                    Relations.NAME to "Status",
                    Relations.RELATION_FORMAT to Relation.Format.STATUS.code.toDouble(),
                    Relations.IS_READ_ONLY to true,
                    Relations.IS_HIDDEN to false,
                    Relations.SOURCE to Relation.Source.DETAILS.name
                )
            )
        )

        val store = DefaultObjectStore()

        store.merge(
            objects = listOf(ObjectWrapper.Basic(records)),
            dependencies = selOptions.map { ObjectWrapper.Basic(it.map) },
            subscriptions = emptyList()
        )

        //TESTING

        val columns: List<ColumnView> = viewerRelations.toViewerColumns(
            relations = dataViewRelations,
            filterBy = listOf()
        )

        val result = columns.buildGridRow(
            obj = ObjectWrapper.Basic(records),
            builder = UrlBuilderImpl(gateway),
            showIcon = false,
            store = store,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes
        )

        val expected = Viewer.GridView.Row(
            id = recordId,
            name = "Untitled",
            type = "Type111",
            showIcon = false,
            objectIcon = ObjectIcon.TypeIcon.Fallback.DEFAULT,
            cells = listOf(
                CellView.Description(
                    id = recordId,
                    relationKey = viewerRelations[0].key,
                    text = "Title4",
                ),
                CellView.Status(
                    id = recordId,
                    relationKey = viewerRelations[1].key,
                    status = listOf(
                        StatusView(
                            id = selOptions[2].id,
                            status = selOptions[2].name.orEmpty(),
                            color = selOptions[2].color
                        )
                    ),
                )
            )
        )

        assertEquals(
            expected = expected,
            actual = result
        )
    }
}