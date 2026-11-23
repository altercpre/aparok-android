package io.aparok.app.presentation.home

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.StubConfig
import io.aparok.app.core_models.StubLinkToObjectBlock
import io.aparok.app.core_models.StubObject
import io.aparok.app.core_models.StubSmartBlock
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.DefaultStoreOfObjectTypes
import io.aparok.app.presentation.widgets.parseWidgets
import io.aparok.app.test_utils.MockDataFactory
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock

class ParseWidgetTest {


    var urlBuilder: UrlBuilder = mock()
    val storeOfObjectTypes = DefaultStoreOfObjectTypes()

    @Test
    fun `should hide widgets with archived source`() = runTest {

        val invalidSource = StubObject(
            isArchived = true
        )

        val validSource = StubObject(
            isArchived = false,
            isDeleted = false
        )

        val widgetContent = Block.Content.Widget(
            layout = listOf(
                Block.Content.Widget.Layout.TREE,
                Block.Content.Widget.Layout.LIST,
                Block.Content.Widget.Layout.LINK,
                Block.Content.Widget.Layout.COMPACT_LIST
            ).random()
        )

        val firstWidgetLink = StubLinkToObjectBlock(
            target = invalidSource.id
        )

        val secondWidgetLink = StubLinkToObjectBlock(
            target = validSource.id
        )

        val firstWidgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(firstWidgetLink.id),
            fields = Block.Fields.empty()
        )

        val secondWidgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(secondWidgetLink.id),
            fields = Block.Fields.empty()
        )

        val smartBlock = StubSmartBlock(
            id = HomeScreenViewModelTest.WIDGET_OBJECT_ID,
            children = listOf(firstWidgetBlock.id, secondWidgetBlock.id)
        )

        val blocks = listOf(
            smartBlock,
            firstWidgetBlock,
            firstWidgetLink,
            secondWidgetBlock,
            secondWidgetLink
        )

        val result = blocks.parseWidgets(
            root = smartBlock.id,
            details = buildMap {
                put(invalidSource.id, invalidSource.map)
                put(validSource.id, validSource.map)
            },
            config = StubConfig(),
            urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes
        )

        assertTrue {
            result.size == 1
        }

        assertTrue {
            result.first().id == secondWidgetBlock.id
        }
    }

    @Test
    fun `should hide widgets with deleted source`()= runTest {

        val invalidSource = StubObject(
            isDeleted = true
        )

        val validSource = StubObject(
            isArchived = false,
            isDeleted = false
        )

        val widgetContent = Block.Content.Widget(
            layout = listOf(
                Block.Content.Widget.Layout.TREE,
                Block.Content.Widget.Layout.LIST,
                Block.Content.Widget.Layout.LINK,
                Block.Content.Widget.Layout.COMPACT_LIST
            ).random()
        )

        val firstWidgetLink = StubLinkToObjectBlock(
            target = invalidSource.id
        )

        val secondWidgetLink = StubLinkToObjectBlock(
            target = validSource.id
        )

        val firstWidgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(firstWidgetLink.id),
            fields = Block.Fields.empty()
        )

        val secondWidgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(secondWidgetLink.id),
            fields = Block.Fields.empty()
        )

        val smartBlock = StubSmartBlock(
            id = HomeScreenViewModelTest.WIDGET_OBJECT_ID,
            children = listOf(firstWidgetBlock.id, secondWidgetBlock.id)
        )

        val blocks = listOf(
            smartBlock,
            firstWidgetBlock,
            firstWidgetLink,
            secondWidgetBlock,
            secondWidgetLink
        )

        val result = blocks.parseWidgets(
            root = smartBlock.id,
            details = buildMap {
                put(invalidSource.id, invalidSource.map)
                put(validSource.id, validSource.map)
            },
            config = StubConfig(),
            urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes
        )

        assertTrue {
            result.size == 1
        }

        assertTrue {
            result.first().id == secondWidgetBlock.id
        }
    }

    @Test
    fun `should hide widgets with deleted or archived source because of empty details`() = runTest {

        val invalidSource = StubObject(
            isArchived = true,
            isDeleted = true
        )

        val validSource = StubObject(
            isArchived = false,
            isDeleted = false
        )

        val widgetContent = Block.Content.Widget(
            layout = listOf(
                Block.Content.Widget.Layout.TREE,
                Block.Content.Widget.Layout.LIST,
                Block.Content.Widget.Layout.LINK,
                Block.Content.Widget.Layout.COMPACT_LIST
            ).random()
        )

        val firstWidgetLink = StubLinkToObjectBlock(
            target = invalidSource.id
        )

        val secondWidgetLink = StubLinkToObjectBlock(
            target = validSource.id
        )

        val firstWidgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(firstWidgetLink.id),
            fields = Block.Fields.empty()
        )

        val secondWidgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(secondWidgetLink.id),
            fields = Block.Fields.empty()
        )

        val smartBlock = StubSmartBlock(
            id = HomeScreenViewModelTest.WIDGET_OBJECT_ID,
            children = listOf(firstWidgetBlock.id, secondWidgetBlock.id)
        )

        val blocks = listOf(
            smartBlock,
            firstWidgetBlock,
            firstWidgetLink,
            secondWidgetBlock,
            secondWidgetLink
        )

        val result = blocks.parseWidgets(
            root = smartBlock.id,
            details = buildMap {
                put(invalidSource.id, emptyMap())
                put(validSource.id, validSource.map)
            },
            config = StubConfig(),
            urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes
        )

        assertTrue {
            result.size == 1
        }

        assertTrue {
            result.first().id == secondWidgetBlock.id
        }
    }

    @Test
    fun `should hide widgets with source having empty details`() = runTest {

        val invalidSource = ObjectWrapper.Basic(
            mapOf(Relations.ID to MockDataFactory.randomUuid())
        )

        val validSource = StubObject(
            isArchived = false,
            isDeleted = false
        )

        val widgetContent = Block.Content.Widget(
            layout = listOf(
                Block.Content.Widget.Layout.TREE,
                Block.Content.Widget.Layout.LIST,
                Block.Content.Widget.Layout.LINK,
                Block.Content.Widget.Layout.COMPACT_LIST
            ).random()
        )

        val firstWidgetLink = StubLinkToObjectBlock(
            target = invalidSource.id
        )

        val secondWidgetLink = StubLinkToObjectBlock(
            target = validSource.id
        )

        val firstWidgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(firstWidgetLink.id),
            fields = Block.Fields.empty()
        )

        val secondWidgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(secondWidgetLink.id),
            fields = Block.Fields.empty()
        )

        val smartBlock = StubSmartBlock(
            id = HomeScreenViewModelTest.WIDGET_OBJECT_ID,
            children = listOf(firstWidgetBlock.id, secondWidgetBlock.id)
        )

        val blocks = listOf(
            smartBlock,
            firstWidgetBlock,
            firstWidgetLink,
            secondWidgetBlock,
            secondWidgetLink
        )

        val result = blocks.parseWidgets(
            root = smartBlock.id,
            details = buildMap {
                put(invalidSource.id, emptyMap())
                put(validSource.id, validSource.map)
            },
            config = StubConfig(),
            urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes
        )

        assertTrue {
            result.size == 1
        }

        assertTrue {
            result.first().id == secondWidgetBlock.id
        }
    }
}