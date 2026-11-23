package io.aparok.app.domain.debugging

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.workspace.SpaceManager
import javax.inject.Inject

class DebugSpace @Inject constructor(
    private val repo: BlockRepository,
    private val spaceManager: SpaceManager,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, String>(dispatchers.io) {

    override suspend fun doWork(params: Unit): String = repo.debugSpace(
        space = spaceManager.get().let { id ->
            if (id.isEmpty())
                throw IllegalStateException("Space not found")
            else
                SpaceId(id)
        }
    )
}