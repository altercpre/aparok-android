package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.InternalFlags
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class SetObjectInternalFlags(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetObjectInternalFlags.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.SetInternalFlags(
            ctx = params.ctx,
            flags = params.flags
        )
        return repo.setInternalFlags(command)
    }

    data class Params(
        val ctx: Id,
        val flags: List<InternalFlags>
    )
}