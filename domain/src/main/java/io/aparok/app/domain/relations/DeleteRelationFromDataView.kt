package io.aparok.app.domain.relations

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for deleting a relation from a data view.
 */
class DeleteRelationFromDataView(
    private val repo: BlockRepository
) : BaseUseCase<Payload, DeleteRelationFromDataView.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.deleteRelationFromDataView(
            ctx = params.ctx,
            relation = params.relation,
            dv = params.dv
        )
    }

    /**
     * @param ctx id of the object
     * @param relation relation id or key
     */
    class Params(
        val ctx: Id,
        val dv: Id,
        val relation: Id
    )
}