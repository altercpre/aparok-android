package io.aparok.app.domain.workspace

import io.aparok.app.core_models.FileLimitsEvent
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import kotlinx.coroutines.flow.Flow

class InterceptFileLimitEvents(
    private val fileLimitsEventChannel: FileLimitsEventChannel,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, Flow<List<FileLimitsEvent>>>(dispatchers.io) {

    override suspend fun doWork(params: Unit): Flow<List<FileLimitsEvent>> {
        return fileLimitsEventChannel.observe()
    }
}