package io.aparok.app.domain.cover

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class SetDocCoverGradient(private val repo: BlockRepository) : BaseUseCase<Payload, SetDocCoverGradient.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setDocumentCoverGradient(
            ctx = params.ctx,
            gradient = params.gradient
        )
    }

    class Params(
        val ctx: Id,
        val gradient: String
    )
}