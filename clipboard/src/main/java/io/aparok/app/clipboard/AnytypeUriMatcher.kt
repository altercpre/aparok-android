package io.aparok.app.clipboard

import io.aparok.app.data.auth.other.ClipboardUriMatcher

class AnytypeUriMatcher : ClipboardUriMatcher {
    override fun isAnytypeUri(uri: String): Boolean {
        return uri == BuildConfig.ANYTYPE_CLIPBOARD_URI
    }
}