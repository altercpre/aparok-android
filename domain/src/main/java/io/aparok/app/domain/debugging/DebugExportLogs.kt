package io.aparok.app.domain.debugging

import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class DebugExportLogs @Inject constructor(
    private val repo: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DebugExportLogs.Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        return repo.debugExportLogs(params.dir)
    }

    data class Params(val dir: String)
}