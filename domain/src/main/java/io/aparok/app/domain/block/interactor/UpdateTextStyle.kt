package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.core_models.Block.Content.Text
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload

/**
 * Use-case for updating a block's text style
 */
open class UpdateTextStyle(
    private val repo: BlockRepository
) : BaseUseCase<Payload, UpdateTextStyle.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.updateTextStyle(
            command = Command.UpdateStyle(
                style = params.style,
                context = params.context,
                targets = params.targets
            )
        )
    }

    /**
     * @property context context id
     * @property targets id of the target blocks, whose style we need to update.
     * @property style new style for the target block.
     */
    data class Params(
        val context: Id,
        val targets: List<Id>,
        val style: Text.Style
    )
}