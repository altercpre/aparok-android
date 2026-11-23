package io.aparok.app.domain.history

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.history.Version
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetVersions @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetVersions.Params, List<Version>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Version> {
        val command = Command.VersionHistory.GetVersions(
            objectId = params.objectId,
            lastVersion = params.lastVersion,
            limit = params.limit
        )
        return repo.getVersions(command)
    }

    data class Params(
        val objectId: Id,
        val lastVersion: Id? = null,
        val limit: Int = 300
    )
}