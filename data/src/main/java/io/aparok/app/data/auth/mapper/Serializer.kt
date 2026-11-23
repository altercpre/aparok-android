package io.aparok.app.data.auth.mapper

import io.aparok.app.core_models.Block

interface Serializer {
    fun serialize(blocks: List<Block>): ByteArray
    fun deserialize(blob: ByteArray): List<Block>
}