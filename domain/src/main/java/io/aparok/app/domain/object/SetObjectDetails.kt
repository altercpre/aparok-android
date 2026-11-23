package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Struct
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetObjectDetails @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetObjectDetails.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload = repo.setObjectDetails(
        ctx = params.ctx,
        details = params.details
    )

    data class Params(
        val ctx: Id,
        val details: Struct
    )
}