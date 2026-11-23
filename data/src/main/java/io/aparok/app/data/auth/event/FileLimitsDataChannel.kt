package io.aparok.app.data.auth.event

import io.aparok.app.core_models.FileLimitsEvent
import io.aparok.app.domain.workspace.FileLimitsEventChannel
import kotlinx.coroutines.flow.Flow

class FileLimitsDataChannel(
    private val channel: FileLimitsRemoteChannel
) : FileLimitsEventChannel {

    override fun observe(): Flow<List<FileLimitsEvent>> {
        return channel.observe()
    }
}