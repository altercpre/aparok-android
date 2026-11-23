package io.aparok.app.domain.cover

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class SetDocCoverColor(private val repo: BlockRepository) : BaseUseCase<Payload, SetDocCoverColor.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setDocumentCoverColor(
            ctx = params.ctx,
            color = params.color
        )
    }

    class Params(
        val ctx: Id,
        val color: String
    )
}