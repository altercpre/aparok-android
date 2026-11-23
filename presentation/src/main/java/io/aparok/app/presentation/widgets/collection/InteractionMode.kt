package io.aparok.app.presentation.widgets.collection

sealed class InteractionMode {
    object Edit : InteractionMode()
    object View : InteractionMode()
}