package io.aparok.app.data.auth.status

import io.aparok.app.core_models.multiplayer.P2PStatusUpdate
import io.aparok.app.core_models.multiplayer.SpaceSyncUpdate
import kotlinx.coroutines.flow.Flow

interface SyncAndP2PStatusEventsStore {
    val p2pStatus: Flow<Map<String, P2PStatusUpdate>>
    val syncStatus: Flow<Map<String, SpaceSyncUpdate>>

    fun start()
    fun stop()
}