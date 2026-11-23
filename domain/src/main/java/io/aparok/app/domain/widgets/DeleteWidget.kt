package io.aparok.app.domain.widgets

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class DeleteWidget @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DeleteWidget.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload = repo.unlink(
        command = Command.Unlink(
            context = params.ctx,
            targets = params.targets
        )
    )

    /**
     * Params for deleting widget blocks from widget object
     * @property [ctx] id of the widget object
     * @property [targets] ids of widget blocks to delete from widget object
     */
    data class Params(
        val ctx: Id,
        val targets: List<Id>
    )
}