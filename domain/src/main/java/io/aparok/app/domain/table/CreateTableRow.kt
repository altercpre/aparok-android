package io.aparok.app.domain.table

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Position
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class CreateTableRow(
    private val repo: BlockRepository
) : BaseUseCase<Payload, CreateTableRow.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.createTableRow(
            ctx = params.ctx,
            targetId = params.target,
            position = params.position
        )
    }

    /**
     * @param ctx - id of the context object
     * @param target - id of the closest row
     * @param position - position of the new row, relative to [target]
     */
    data class Params(
        val ctx: Id,
        val target: Id,
        val position: Position
    )
}