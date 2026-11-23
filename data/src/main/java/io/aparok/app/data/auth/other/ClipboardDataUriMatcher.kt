package io.aparok.app.data.auth.other

import io.aparok.app.domain.clipboard.Clipboard

class ClipboardDataUriMatcher(
    private val matcher: ClipboardUriMatcher
) : Clipboard.UriMatcher {

    override fun isAnytypeUri(
        uri: String
    ): Boolean = matcher.isAnytypeUri(uri)
}