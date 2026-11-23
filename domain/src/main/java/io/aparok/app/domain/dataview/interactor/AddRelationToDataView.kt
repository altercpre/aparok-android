package io.aparok.app.domain.dataview.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for adding an existing relation to a data view.
 */
class AddRelationToDataView(
    private val repo: BlockRepository
) : BaseUseCase<Payload, AddRelationToDataView.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.addRelationToDataView(
            ctx = params.ctx,
            dv = params.dv,
            relation = params.relation
        )
    }

    /**
     * @property [ctx] operation context
     * @property [dv] data view's block id
     * @property [relation] id or key of the existing relation
     */
    class Params(
        val ctx: Id,
        val dv: Id,
        val relation: Key
    )
}