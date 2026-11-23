package io.aparok.app.domain.table

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Position
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class DuplicateTableColumn(
    private val repo: BlockRepository
) : BaseUseCase<Payload, DuplicateTableColumn.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.duplicateTableColumn(
            ctx = params.ctx,
            targetId = params.targetDrop,
            blockId = params.column,
            position = params.position
        )
    }

    /**
     * @param ctx - id of the context object
     * @param column - column to duplicate
     * @param targetDrop - id of the column in relation to which the duplicate is positioned
     * @param position - position of the new column, relative to [targetDrop]
     */
    data class Params(
        val ctx: Id,
        val column: Id,
        val targetDrop: Id,
        val position: Position
    )
}