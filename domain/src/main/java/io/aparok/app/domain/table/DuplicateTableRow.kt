package io.aparok.app.domain.table

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Position
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class DuplicateTableRow(
    private val repo: BlockRepository
) : BaseUseCase<Payload, DuplicateTableRow.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.duplicateTableRow(
            ctx = params.ctx,
            targetId = params.targetDrop,
            blockId = params.row,
            position = params.position
        )
    }

    /**
     * @param ctx - id of the context object
     * @param row - row to duplicate
     * @param targetDrop - id of the row in relation to which the duplicate is positioned
     * @param position - position of the new row, relative to [targetDrop]
     */
    data class Params(
        val ctx: Id,
        val targetDrop: Id,
        val row: Id,
        val position: Position
    )
}