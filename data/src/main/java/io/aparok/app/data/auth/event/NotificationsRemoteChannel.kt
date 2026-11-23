package io.aparok.app.data.auth.event

import io.aparok.app.core_models.Notification
import io.aparok.app.domain.workspace.NotificationsChannel
import kotlinx.coroutines.flow.Flow

interface NotificationsRemoteChannel {
    fun observe(): Flow<List<Notification.Event>>
}

class NotificationsDateChannel(
    private val channel: NotificationsRemoteChannel
) : NotificationsChannel {

    override fun observe(): Flow<List<Notification.Event>> {
        return channel.observe()
    }
}