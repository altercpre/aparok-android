package io.aparok.app.domain.table

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class DeleteTableRow(
    private val repo: BlockRepository
) : BaseUseCase<Payload, DeleteTableRow.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.deleteTableRow(
            ctx = params.ctx,
            targetId = params.target
        )
    }

    /**
     * @param ctx - id of the context object
     * @param target - id of the row to delete
     */
    data class Params(
        val ctx: Id,
        val target: Id
    )
}