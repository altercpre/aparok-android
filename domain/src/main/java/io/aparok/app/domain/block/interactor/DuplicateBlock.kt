package io.aparok.app.domain.block.interactor

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for block duplication.
 * Should return id of the new block.
 */
open class DuplicateBlock(
    private val repo: BlockRepository
) : BaseUseCase<Pair<List<Id>, Payload>, DuplicateBlock.Params>() {

    override suspend fun run(params: Params) = try {
        repo.duplicate(
            command = Command.Duplicate(
                context = params.context,
                target = params.target,
                blocks = params.blocks
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * @property context context id
     * @property blocks id of the target blocks, which we need to duplicate
     */
    data class Params(
        val context: Id,
        val target: Id,
        val blocks: List<Id>
    )
}