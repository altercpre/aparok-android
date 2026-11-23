package io.aparok.app.middleware.converters

import anytype.clipboard.Clipboard
import io.aparok.app.core_models.Block
import io.aparok.app.data.auth.mapper.Serializer
import io.aparok.app.middleware.mappers.toCoreModels
import io.aparok.app.middleware.mappers.toMiddlewareModel

class ClipboardSerializer : Serializer {

    override fun serialize(blocks: List<Block>): ByteArray {
        val models = blocks.map { it.toMiddlewareModel() }
        val clipboard = Clipboard(blocks = models)
        return Clipboard.ADAPTER.encode(clipboard)
    }

    override fun deserialize(blob: ByteArray): List<Block> {
        return Clipboard.ADAPTER.decode(blob).blocks.toCoreModels()
    }
}