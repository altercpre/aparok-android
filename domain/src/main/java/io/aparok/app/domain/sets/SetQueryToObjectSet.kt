package io.aparok.app.domain.sets

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class SetQueryToObjectSet(private val repo: BlockRepository, dispatchers: AppCoroutineDispatchers) :
    ResultInteractor<SetQueryToObjectSet.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.SetQueryToSet(
            ctx = params.ctx,
            query = params.query
        )
        return repo.setQueryToSet(command)
    }

    class Params(
        val ctx: Id,
        val query: Id
    )
}