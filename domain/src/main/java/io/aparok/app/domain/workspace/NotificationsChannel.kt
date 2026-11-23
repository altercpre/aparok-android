package io.aparok.app.domain.workspace

import io.aparok.app.core_models.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationsChannel {
    fun observe(): Flow<List<Notification.Event>>
}