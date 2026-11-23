package io.aparok.app.domain.icon

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.icon.RemoveObjectIcon.Params

class RemoveObjectIcon(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repository.removeDocumentIcon(ctx = params.objectId)
    }

    data class Params(val objectId: Id)
}