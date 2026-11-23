package io.aparok.app.domain.table

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class FillTableRow(
    private val repo: BlockRepository
) : BaseUseCase<Payload, FillTableRow.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.fillTableRow(
            ctx = params.ctx,
            targetIds = params.targetIds
        )
    }

    /**
     * @property [targetIds] the list of rows that need to be filled in
     */
    data class Params(
        val ctx: Id,
        val targetIds: List<Id>
    )
}