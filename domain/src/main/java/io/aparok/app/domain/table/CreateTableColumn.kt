package io.aparok.app.domain.table

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Position
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class CreateTableColumn(
    private val repo: BlockRepository
) : BaseUseCase<Payload, CreateTableColumn.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.createTableColumn(
            ctx = params.ctx,
            targetId = params.target,
            position = params.position
        )
    }

    /**
     * @param ctx - id of the context object
     * @param target - id of the closest column
     * @param position - position of the new column, relative to [target]
     */
    data class Params(
        val ctx: Id,
        val target: Id,
        val position: Position
    )
}