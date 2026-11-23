package io.aparok.app.domain.notifications

import io.aparok.app.core_models.Notification

interface SystemNotificationService {
    val areNotificationsEnabled: Boolean
    fun notify(notification: Notification)
    fun cancel(id: String)

    suspend fun setPendingNotification(notification: Notification)
    suspend fun clearPendingNotification()
    suspend fun notifyIfPending()
}