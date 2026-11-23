package io.aparok.app.core_utils.ext

fun String?.orNull(): String? = this?.ifEmpty { null }