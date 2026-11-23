package io.aparok.app.presentation.common

import io.aparok.app.core_models.Url

sealed class SystemAction {
    data class OpenUrl(val url: Url) : SystemAction()
    data class CopyToClipboard(val plain: String) : SystemAction()
    data class MailTo(val mail: String) : SystemAction()
}