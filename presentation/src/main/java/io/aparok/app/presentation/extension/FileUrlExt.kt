package io.aparok.app.presentation.extension

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Block.Content
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.Url
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.presentation.editor.editor.Orchestrator
import timber.log.Timber

fun UrlBuilder.getUrlForFileBlock(
    fileBlock: Block?,
    isOriginalImage: Boolean = false,
    isThumbnail: Boolean = false
): Url? {
    if (fileBlock == null) return null
    val fileContent = fileBlock.content as? Block.Content.File ?: return null
    return getUrlForFileContent(fileContent, isOriginalImage, isThumbnail)
}

fun UrlBuilder.getUrlForFileContent(
    fileContent: Block.Content.File,
    isOriginalImage: Boolean = false,
    isThumbnail: Boolean = false
): Url? {
    val targetObjectId = fileContent.targetObjectId
    if (fileContent.state != Block.Content.File.State.DONE || targetObjectId.isNullOrBlank()) {
        return null
    }
    return getUrlBasedOnType(fileContent.type, targetObjectId, isOriginalImage, isThumbnail)
}

private fun UrlBuilder.getUrlBasedOnType(
    fileType: Block.Content.File.Type?,
    targetObjectId: String,
    isOriginalImage: Boolean,
    isThumbnail: Boolean
): Url? {
    return when (fileType) {
        Block.Content.File.Type.IMAGE -> {
            when {
                isOriginalImage -> original(targetObjectId)
                isThumbnail -> thumbnail(targetObjectId)
                else -> large(targetObjectId)
            }
        }

        Block.Content.File.Type.VIDEO -> video(targetObjectId)
        Block.Content.File.Type.FILE,
        Block.Content.File.Type.PDF,
        Block.Content.File.Type.AUDIO -> file(targetObjectId)

        Block.Content.File.Type.NONE, null -> null
    }
}

fun UrlBuilder.getUrlBasedOnFileLayout(
    obj: Id,
    layout: ObjectType.Layout
): Url? {
    return when (layout) {
        ObjectType.Layout.IMAGE -> original(obj)
        ObjectType.Layout.VIDEO -> video(obj)
        ObjectType.Layout.FILE,
        ObjectType.Layout.PDF,
        ObjectType.Layout.AUDIO -> file(obj)

        else -> null
    }
}


data class FileDetails(
    val content: Block.Content.File,
    val targetObjectId: String,
    val fileName: String,
)

/**
 * Attempts to retrieve and validate the file details for a given block [blockId].
 * Returns [FileDetails] if successful, or `null` if something went wrong.
 */
fun List<Block>.getFileDetailsForBlock(
    blockId: String,
    orchestrator: Orchestrator,
    fieldParser: FieldParser
): FileDetails? {

    val block = firstOrNull { it.id == blockId } ?: run {
        Timber.e("No block found with id $blockId")
        return null
    }

    val content = block.content
    if (content !is Content.File || content.state != Content.File.State.DONE) {
        Timber.e("Block content is not a file or is not in the DONE state; cannot proceed.")
        return null
    }

    val targetObjectId = content.targetObjectId
    if (targetObjectId.isEmpty()) {
        Timber.e("Target object ID is empty; cannot proceed with file sharing.")
        return null
    }

    val fileObject = orchestrator.stores.details.current().getObject(targetObjectId)
    if (fileObject == null) {
        Timber.e("Object with id $targetObjectId not found.")
        return null
    }

    val fileName = fieldParser.getObjectName(fileObject)

    return FileDetails(
        content = content,
        targetObjectId = targetObjectId,
        fileName = fileName,
    )
}
