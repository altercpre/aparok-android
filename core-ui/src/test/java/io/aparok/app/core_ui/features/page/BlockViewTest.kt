package io.aparok.app.core_ui.features.page

import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO_ERROR
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO_PLACEHOLDER
import io.aparok.app.presentation.editor.editor.model.types.Types.HOLDER_VIDEO_UPLOAD
import io.aparok.app.test_utils.MockDataFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class BlockViewTest {

    @Test
    fun `should return video block with view type Empty`() {
        val block = BlockView.MediaPlaceholder.Video(
            id = MockDataFactory.randomUuid(),
            indent = MockDataFactory.randomInt(),
            isPreviousBlockMedia = false
        )
        assertEquals(HOLDER_VIDEO_PLACEHOLDER, block.getViewType())
    }

    @Test
    fun `should return video block with view type Error`() {
        val block = BlockView.Error.Video(
            id = MockDataFactory.randomUuid(),
            indent = MockDataFactory.randomInt()
        )
        assertEquals(HOLDER_VIDEO_ERROR, block.getViewType())
    }

    @Test
    fun `should return video block with view type Done`() {
        val block = BlockView.Media.Video(
            id = MockDataFactory.randomUuid(),
            targetObjectId = MockDataFactory.randomString(),
            url = MockDataFactory.randomString(),
            size = MockDataFactory.randomLong(),
            mime = MockDataFactory.randomString(),
            name = MockDataFactory.randomString(),
            indent = MockDataFactory.randomInt(),
            decorations = emptyList()
        )
        assertEquals(HOLDER_VIDEO, block.getViewType())
    }

    @Test
    fun `should return video block with view type Uploading`() {
        val block = BlockView.Upload.Video(
            id = MockDataFactory.randomString(),
            indent = MockDataFactory.randomInt()
        )
        assertEquals(HOLDER_VIDEO_UPLOAD, block.getViewType())
    }
}