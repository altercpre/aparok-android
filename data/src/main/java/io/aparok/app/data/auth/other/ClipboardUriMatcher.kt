package io.aparok.app.data.auth.other

interface ClipboardUriMatcher {
    fun isAnytypeUri(uri: String) : Boolean
}