package io.aparok.app.core_ui.features.editor

interface ItemProviderAdapter<T> {
    fun provide(pos: Int): T
}
