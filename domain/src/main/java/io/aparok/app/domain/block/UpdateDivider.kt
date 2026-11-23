package io.aparok.app.domain.block

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Payload

/**
 * Use-case for updating a divider style
 */
open class UpdateDivider(
    private val repo: BlockRepository
) : BaseUseCase<Payload, UpdateDivider.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.updateDivider(
            command = Command.UpdateDivider(
                style = params.style,
                context = params.context,
                targets = params.targets
            )
        )
    }

    /**
     * @property context context id
     * @property targets id of the target blocks, whose style we need to update.
     * @property style new style for the target blocks.
     */
    data class Params(
        val context: Id,
        val targets: List<Id>,
        val style: Block.Content.Divider.Style
    )
}