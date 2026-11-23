package io.aparok.app.domain.cover

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class RemoveDocCover(private val repo: BlockRepository) : BaseUseCase<Payload, RemoveDocCover.Params>() {
    override suspend fun run(params: Params) = safe {
        repo.removeDocumentCover(ctx = params.ctx)
    }

    class Params(val ctx: Id)
}