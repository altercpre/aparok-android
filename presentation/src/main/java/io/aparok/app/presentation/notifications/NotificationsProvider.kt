package io.aparok.app.presentation.notifications

import io.aparok.app.core_models.Notification
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.workspace.NotificationsChannel
import io.aparok.app.presentation.BuildConfig
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber

interface NotificationsProvider {
    val events: StateFlow<List<Notification.Event>>

    class Default @Inject constructor(
        dispatchers: AppCoroutineDispatchers,
        scope: CoroutineScope,
        private val notificationsChannel: NotificationsChannel,
        private val awaitAccountStartManager: AwaitAccountStartManager
    ) : NotificationsProvider {

        private val _events = MutableStateFlow<List<Notification.Event>>(emptyList())
        override val events: StateFlow<List<Notification.Event>> = _events

        init {
            scope.launch(dispatchers.io) {
                observe().collect { events ->
                    if (BuildConfig.DEBUG) {
                        Timber.d("New notifications: $events")
                    }
                    _events.value = events
                }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun observe(): Flow<List<Notification.Event>> {
            return awaitAccountStartManager.state().flatMapLatest { state ->
                if (state is AwaitAccountStartManager.State.Started)
                    notificationsChannel.observe()
                else
                    emptyFlow()
            }
        }
    }
}
