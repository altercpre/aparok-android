package io.aparok.app.domain.workspace

import io.aparok.app.core_models.FileLimitsEvent
import kotlinx.coroutines.flow.Flow

interface FileLimitsEventChannel {
    fun observe() : Flow<List<FileLimitsEvent>>
}