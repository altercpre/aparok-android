package io.aparok.app.domain.chats

import io.aparok.app.core_models.chats.PushKeyUpdate
import kotlinx.coroutines.flow.Flow

interface PushKeyChannel {
    fun observe(): Flow<PushKeyUpdate>
}