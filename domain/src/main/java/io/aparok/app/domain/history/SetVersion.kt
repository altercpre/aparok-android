package io.aparok.app.domain.history

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetVersion @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<SetVersion.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.VersionHistory.SetVersion(
            objectId = params.objectId,
            versionId = params.versionId
        )
        repo.setVersion(command)
    }

    data class Params(
        val objectId: Id,
        val versionId: Id
    )
}