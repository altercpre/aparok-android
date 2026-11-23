package io.aparok.app.domain.block.interactor

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class SetLinkAppearance(
    private val repository: BlockRepository,
) : BaseUseCase<Payload, SetLinkAppearance.Params>() {

    data class Params(
        val contextId: String,
        val blockId: String,
        val content: Block.Content.Link
    )

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repository.setLinkAppearance(
            Command.SetLinkAppearance(
                contextId = params.contextId,
                blockId = params.blockId,
                content = params.content
            )
        )
    }
}