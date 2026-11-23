package io.aparok.app.domain.debugging

import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class DebugRunProfiler @Inject constructor(
    private val repo: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DebugRunProfiler.Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        return repo.debugRunProfiler(params.durationInSeconds)
    }

    data class Params(val durationInSeconds: Int)
}
