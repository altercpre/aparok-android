package io.aparok.app.domain.media

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Discards preloaded file by ID.
 */
class DiscardPreloadedFile @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Id, Unit>(dispatchers.io) {
    override suspend fun doWork(params: Id) {
        repo.discardPreloadedFile(
            command = Command.DiscardPreloadedFile(params)
        )
    }
}