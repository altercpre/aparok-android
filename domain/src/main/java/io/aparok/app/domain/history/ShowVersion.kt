package io.aparok.app.domain.history

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.history.ShowVersionResponse
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class ShowVersion @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<ShowVersion.Params, ShowVersionResponse>(dispatchers.io) {

    override suspend fun doWork(params: Params): ShowVersionResponse {
        val command = Command.VersionHistory.ShowVersion(
            objectId = params.objectId,
            versionId = params.versionId,
            traceId = params.traceId
        )
        return repo.showVersion(command)
    }

    data class Params(
        val objectId: Id,
        val versionId: Id,
        val traceId: Id = ""
    )
}