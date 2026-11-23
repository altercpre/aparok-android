package io.aparok.app.domain.block.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class ClearBlockStyle(
    private val repository: BlockRepository,
) : BaseUseCase<Payload, ClearBlockStyle.Params>() {

    data class Params(
        val ctx: Id,
        val blockIds: List<Id>
    )

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repository.clearBlockStyle(
            ctx = params.ctx,
            blockIds = params.blockIds
        )
    }
}