package io.aparok.app.core_ui.tools

import io.aparok.app.core_models.Url

interface ClipboardInterceptor {

    fun onClipboardAction(action: Action)
    fun onBookmarkPasted(url: Url)
    fun onLinkPasted(url: Url) {}

    sealed class Action {
        data class Copy(val selection: IntRange) : Action()
        data class Paste(val selection: IntRange) : Action()
    }
}