package io.aparok.app.domain.notifications

import io.aparok.app.core_models.DecryptedPushContent
import io.aparok.app.core_models.Id

interface NotificationBuilder {
    suspend fun buildAndNotify(message: DecryptedPushContent.Message, spaceId: Id, groupId: String)
    fun clearNotificationChannel(spaceId: String, chatId: String)
    fun clearNotificationsByGroupId(groupId: String)
}