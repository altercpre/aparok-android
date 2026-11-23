package io.aparok.app.domain.notifications

import io.aparok.app.presentation.notifications.PushKey

interface PushKeyProvider {
    fun start()
    fun stop()
    fun getPushKey(): Map<String, PushKey>
}