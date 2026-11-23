package io.aparok.app.domain.layout

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class SetObjectLayout(private val repo: BlockRepository): BaseUseCase<Payload, SetObjectLayout.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setObjectLayout(
            ctx = params.ctx,
            layout = params.layout
        )
    }

    data class Params(
        val ctx: Id,
        val layout: ObjectType.Layout
    )
}