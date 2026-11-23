package io.aparok.app.presentation.home

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.StubConfig
import io.aparok.app.core_models.StubLinkToObjectBlock
import io.aparok.app.core_models.StubObject
import io.aparok.app.core_models.StubSmartBlock
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.DefaultStoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.presentation.widgets.Widget
import io.aparok.app.presentation.widgets.parseWidgets
import io.aparok.app.test_utils.MockDataFactory
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock

class ParseWidgetLimitTest {

    val source = StubObject()
    val storeOfObjectTypes = DefaultStoreOfObjectTypes()
    var urlBuilder: UrlBuilder = mock()

    @Test
    fun `should parse widget limit for widget with tree layout`() = runTest {

        val widgetContent = Block.Content.Widget(
            limit = MockDataFactory.randomInt(),
            layout = Block.Content.Widget.Layout.TREE
        )

        val widgetLink = StubLinkToObjectBlock(
            target = source.id
        )

        val widgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(widgetLink.id),
            fields = Block.Fields.empty()
        )

        val smartBlock = StubSmartBlock(
            id = HomeScreenViewModelTest.WIDGET_OBJECT_ID,
            children = listOf(widgetBlock.id),
        )

        val blocks = listOf(smartBlock, widgetBlock, widgetLink)

        val result = blocks.parseWidgets(
            root = smartBlock.id,
            details = buildMap {
                put(source.id, source.map)
            },
            config = StubConfig(),
            urlBuilder = urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes
        )

        assertTrue {
            val actualWidget = result.first()
            actualWidget is Widget.Tree && actualWidget.limit == widgetContent.limit
        }
    }

    @Test
    fun `should parse widget limit for widget with list layout`() = runTest {

        val widgetContent = Block.Content.Widget(
            limit = MockDataFactory.randomInt(),
            layout = Block.Content.Widget.Layout.LIST
        )

        val widgetLink = StubLinkToObjectBlock(
            target = source.id
        )

        val widgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(widgetLink.id),
            fields = Block.Fields.empty()
        )

        val smartBlock = StubSmartBlock(
            id = HomeScreenViewModelTest.WIDGET_OBJECT_ID,
            children = listOf(widgetBlock.id),
        )

        val blocks = listOf(smartBlock, widgetBlock, widgetLink)

        val result = blocks.parseWidgets(
            root = smartBlock.id,
            details = buildMap {
                put(source.id, source.map)
            },
            config = StubConfig(),
            urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes
        )

        assertTrue {
            val actualWidget = result.first()
            actualWidget is Widget.List && actualWidget.limit == widgetContent.limit
        }
    }

    @Test
    fun `should parse widget limit for widget with compact list layout`() = runTest {

        val widgetContent = Block.Content.Widget(
            limit = MockDataFactory.randomInt(),
            layout = Block.Content.Widget.Layout.COMPACT_LIST
        )

        val widgetLink = StubLinkToObjectBlock(
            target = source.id
        )

        val widgetBlock = Block(
            id = MockDataFactory.randomUuid(),
            content = widgetContent,
            children = listOf(widgetLink.id),
            fields = Block.Fields.empty()
        )

        val smartBlock = StubSmartBlock(
            id = HomeScreenViewModelTest.WIDGET_OBJECT_ID,
            children = listOf(widgetBlock.id),
        )

        val blocks = listOf(smartBlock, widgetBlock, widgetLink)

        val result = blocks.parseWidgets(
            root = smartBlock.id,
            details = buildMap {
                put(source.id, source.map)
            },
            config = StubConfig(),
            urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes
        )

        assertTrue {
            val actualWidget = result.first()
            actualWidget is Widget.List && actualWidget.limit == widgetContent.limit
        }
    }
}