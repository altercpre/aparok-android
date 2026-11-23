package io.aparok.app.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.domain.multiplayer.GetSpaceMemberByIdentity
import io.aparok.app.domain.notifications.ReplyNotifications
import io.aparok.app.domain.spaces.SaveCurrentSpace
import io.aparok.app.domain.workspace.SpaceManager
import javax.inject.Inject

class NotificationsViewModelFactory @Inject constructor(
    private val analytics: Analytics,
    private val notificationsProvider: NotificationsProvider,
    private val spaceManager: SpaceManager,
    private val saveCurrentSpace: SaveCurrentSpace,
    private val getSpaceMemberByIdentity: GetSpaceMemberByIdentity,
    private val replyNotifications: ReplyNotifications,
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationsViewModel(
            analytics = analytics,
            notificationsProvider = notificationsProvider,
            spaceManager = spaceManager,
            saveCurrentSpace = saveCurrentSpace,
            getSpaceMemberByIdentity = getSpaceMemberByIdentity,
            replyNotifications = replyNotifications,
        ) as T
    }
}