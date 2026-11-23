package io.aparok.app.core_models

data class GlobalSearchHistory(
    val query: String,
    val relatedObject: Id? = null
)
