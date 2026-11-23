package io.aparok.app.domain.vault

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class UnpinSpace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UnpinSpace.Params, Unit>(dispatchers.io) {

    data class Params(
        val spaceId: Id
    )

    override suspend fun doWork(params: Params): Unit {
        repo.spaceUnsetOrder(
            spaceViewId = params.spaceId
        )
    }
} 