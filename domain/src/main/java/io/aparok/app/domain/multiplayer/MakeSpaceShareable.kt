package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for making private space shareable.
 * If space is not shareable, you cannot generate an invite link for it.
 */
class MakeSpaceShareable @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, Unit>(dispatchers.io) {

    override suspend fun doWork(params: SpaceId) {
        repo.makeSpaceShareable(space = params)
    }
}