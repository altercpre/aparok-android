package io.aparok.app.analytics.props

data class Props(val map: Map<String?, Any?>) {

    companion object {

        const val OBJ_TYPE_CUSTOM = "custom"
        const val OBJ_RELATION_CUSTOM = "custom"

        fun empty() = Props(emptyMap())
    }
}