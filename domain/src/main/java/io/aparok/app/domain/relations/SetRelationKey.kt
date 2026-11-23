package io.aparok.app.domain.relations

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class SetRelationKey(
    private val repo: BlockRepository
) : BaseUseCase<Payload, SetRelationKey.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.setRelationKey(
            Command.SetRelationKey(
                contextId = params.contextId,
                blockId = params.blockId,
                key = params.key
            )
        )
    }

    class Params(
        val contextId: Id,
        val blockId: Id,
        val key: Id
    )
}