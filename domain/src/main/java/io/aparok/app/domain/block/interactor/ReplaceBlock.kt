package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload

/**
 * Use-case for replacing target block by a new block (created from prototype)
 */
class ReplaceBlock(
    private val repo: BlockRepository
) : BaseUseCase<Pair<Id, Payload>, ReplaceBlock.Params>() {

    override suspend fun run(params: Params) = try {
        repo.replace(
            command = Command.Replace(
                context = params.context,
                target = params.target,
                prototype = params.prototype
            )
        ).let {
            Either.Right(it)
        }
    } catch (e: Throwable) {
        Either.Left(e)
    }

    /**
     * Params for replacing target block by a new block (created from prototype)
     * @property context id of the context
     * @property target id of the block, which we need to replace
     * @property prototype prototype of the new block
     */
    data class Params(
        val context: Id,
        val target: Id,
        val prototype: Block.Prototype
    )
}