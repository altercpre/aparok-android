package io.aparok.app.domain.history

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.history.DiffVersionResponse
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class DiffVersions @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<DiffVersions.Params, DiffVersionResponse>(dispatchers.io) {

    override suspend fun doWork(params: Params): DiffVersionResponse {
        val command = Command.VersionHistory.DiffVersions(
            objectId = params.objectId,
            spaceId = params.spaceId,
            currentVersion = params.currentVersion,
            previousVersion = params.previousVersion
        )
        return repo.diffVersions(command)
    }

    data class Params(
        val objectId: Id,
        val spaceId: Id,
        val currentVersion: Id,
        val previousVersion: Id
    )
}