package io.aparok.app.domain.table

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class SetTableRowHeader(
    private val repo: BlockRepository
) : BaseUseCase<Payload, SetTableRowHeader.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.setTableRowHeader(
            ctx = params.ctx,
            targetId = params.row,
            isHeader = params.isHeader
        )
    }

    data class Params(
        val ctx: Id,
        val row: Id,
        val isHeader: Boolean
    )
}