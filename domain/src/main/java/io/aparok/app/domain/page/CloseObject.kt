package io.aparok.app.domain.page

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for closing a smart block by id.
 */
open class CloseObject @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CloseObject.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.closeObject(
            id = params.target,
            space = params.space
        )
    }

    data class Params(
        val target: Id,
        val space: SpaceId
    )
}