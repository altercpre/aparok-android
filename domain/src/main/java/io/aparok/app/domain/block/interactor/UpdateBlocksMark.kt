package io.aparok.app.domain.block.interactor

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

open class UpdateBlocksMark(
    private val repo: BlockRepository
) : BaseUseCase<Payload, UpdateBlocksMark.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.updateBlocksMark(
            command = Command.UpdateBlocksMark(
                context = params.context,
                targets = params.targets,
                mark = params.mark
            )
        )
    }

    /**
     * Params for updating the whole block's mark.
     * @property context context id
     * @property targets ids of the target blocks, whose mark we need to update.
     * @property mark new mark
     */
    data class Params(
        val context: Id,
        val targets: List<Id>,
        val mark: Block.Content.Text.Mark
    )
}