package io.aparok.app.data.auth.repo.clipboard

import io.aparok.app.core_models.Block
import io.aparok.app.data.auth.model.ClipEntity

interface ClipboardDataStore {
    /**
     * Stores last copied Anytype blocks.
     * @see ClipEntity
     */
    interface Storage {
        fun persist(blocks: List<Block>)
        fun fetch() : List<Block>
    }

    /**
     * Provides access to system clipboard.
     */
    interface System {
        /**
         * Stores copied [text] and optionally a [html] representation on the systen clipboard.
         */
        suspend fun put(text: String, html: String?, ignoreHtml: Boolean)

        /**
         * @return current clip on the clipboard.
         */
        suspend fun clip() : ClipEntity?
    }

    class Factory(
        val storage: Storage,
        val system: System
    )
}