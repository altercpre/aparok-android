package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload

open class UpdateAlignment(
    private val repo: BlockRepository
) : BaseUseCase<Payload, UpdateAlignment.Params>() {

    override suspend fun run(params: Params) = try {
        repo.updateAlignment(
            command = Command.UpdateAlignment(
                context = params.context,
                targets = params.targets,
                alignment = params.alignment
            )
        ).let {
            Either.Right(it)
        }
    } catch (e: Exception) {
        Either.Left(e)
    }

    /**
     * Params for updating alignment of the whole block.
     * @property context context id
     * @property targets id of the target block, whose alignment we need to update.
     * @property alignment new alignment
     */
    data class Params(
        val context: Id,
        val targets: List<Id>,
        val alignment: Block.Align
    )
}