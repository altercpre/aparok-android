package io.aparok.app.util

import android.webkit.URLUtil
import io.aparok.app.presentation.util.UrlHelper

class UrlHelperImpl : UrlHelper {
    override fun isValidUrl(url: String): Boolean {
        return URLUtil.isValidUrl(url)
    }
}
