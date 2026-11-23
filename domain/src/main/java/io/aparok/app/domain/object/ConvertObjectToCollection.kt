package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class ConvertObjectToCollection(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ConvertObjectToCollection.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.objectToCollection(ctx = params.ctx)

    data class Params(val ctx: Id)
}