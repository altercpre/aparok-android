package io.aparok.app.domain.spaces

import io.aparok.app.core_models.Struct
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetSpaceDetails @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<SetSpaceDetails.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setSpaceDetails(
            space = params.space,
            details = params.details
        )
    }

    class Params(val space: SpaceId, val details: Struct)
}