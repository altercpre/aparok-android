package io.aparok.app.domain.dataview

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class SetDataViewQuery(private val repo: BlockRepository) :
    BaseUseCase<Payload, SetDataViewQuery.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.blockDataViewSetSource(
            ctx = params.ctx,
            block = DEFAULT_DATA_VIEW_BLOCK_ID,
            sources = params.sources
        )
    }

    companion object {
        const val DEFAULT_DATA_VIEW_BLOCK_ID = "dataview"
    }

    data class Params(
        val ctx: Id,
        val sources: List<Id>
    )
}