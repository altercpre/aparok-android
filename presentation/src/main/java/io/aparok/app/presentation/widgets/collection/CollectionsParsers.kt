package io.aparok.app.presentation.widgets.collection

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Struct
import io.aparok.app.core_models.ext.asMap

fun List<Block>.parseFavorites(
    root: Id,
    details: Map<Id, Struct>
): Map<Id, FavoritesOrder> = buildMap {
    var count = 0
    val map = asMap()
    val favorites = map[root] ?: emptyList()
    favorites.forEach { f ->
        val content = f.content
        if (content is Block.Content.Link) {
            val raw = details[content.target] ?: emptyMap()
            val obj = ObjectWrapper.Basic(raw)
            if (obj.isArchived != true) {
                put(content.target, FavoritesOrder(f.id, count))
                count++
            }
        }
    }
}

data class FavoritesOrder(
    val blockId: Id,
    val order: Int
)