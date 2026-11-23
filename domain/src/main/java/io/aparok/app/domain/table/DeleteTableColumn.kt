package io.aparok.app.domain.table

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class DeleteTableColumn(
    private val repo: BlockRepository
) : BaseUseCase<Payload, DeleteTableColumn.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.deleteTableColumn(
            ctx = params.ctx,
            targetId = params.target
        )
    }

    /**
     * @param ctx - id of the context object
     * @param target - id of the column to delete
     */
    data class Params(
        val ctx: Id,
        val target: Id
    )
}