package io.aparok.app.domain.collections

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class AddObjectToCollection @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddObjectToCollection.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.AddObjectToCollection(
            ctx = params.ctx,
            afterId = params.after,
            ids = params.targets
        )
        return repo.addObjectToCollection(command)
    }

    class Params(
        val ctx: Id,
        val targets: List<Id>,
        val after: Id = ""
    )
}