package io.aparok.app.data.auth.event

import io.aparok.app.core_models.FileLimitsEvent
import kotlinx.coroutines.flow.Flow

interface FileLimitsRemoteChannel {
    fun observe(): Flow<List<FileLimitsEvent>>
}