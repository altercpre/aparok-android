package io.aparok.app.domain.block.interactor

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class TurnIntoStyle(
    private val repo: BlockRepository
) : BaseUseCase<Payload, TurnIntoStyle.Params>() {

    override suspend fun run(
        params: Params
    ) = safe {
        repo.turnInto(
            context = params.context,
            targets = params.targets,
            style = params.style
        )
    }

    data class Params(
        val context: Id,
        val targets: List<Id>,
        val style: Block.Content.Text.Style
    )
}