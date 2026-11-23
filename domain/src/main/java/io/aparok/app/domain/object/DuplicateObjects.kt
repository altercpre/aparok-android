package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class DuplicateObjects(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DuplicateObjects.Params, List<Id>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Id> {
        return repo.duplicateObjectsList(params.ids)
    }

    data class Params(
        val ids: List<Id>
    )
}