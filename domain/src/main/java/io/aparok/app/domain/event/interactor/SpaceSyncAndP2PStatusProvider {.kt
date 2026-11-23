package io.aparok.app.domain.event.interactor

import io.aparok.app.core_models.multiplayer.SpaceSyncAndP2PStatusState
import kotlinx.coroutines.flow.Flow

interface SpaceSyncAndP2PStatusProvider {
    fun observe(): Flow<SpaceSyncAndP2PStatusState>
}