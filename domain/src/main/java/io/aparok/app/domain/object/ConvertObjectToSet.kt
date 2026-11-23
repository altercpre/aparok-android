package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class ConvertObjectToSet(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ConvertObjectToSet.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) =
        repo.objectToSet(
            ctx = params.ctx,
            source = params.sources
        )

    data class Params(
        val ctx: Id,
        val sources: List<String>
    )
}