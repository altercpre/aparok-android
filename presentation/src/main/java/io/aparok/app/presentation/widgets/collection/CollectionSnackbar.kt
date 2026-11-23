package io.aparok.app.presentation.widgets.collection

data class CollectionSnackbar(
    val message: String,
    val action: () -> Unit = { }
)