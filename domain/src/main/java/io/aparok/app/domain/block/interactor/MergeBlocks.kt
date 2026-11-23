package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload

/**
 * Use-case for merging a pair of blocks.
 */
open class MergeBlocks(private val repo: BlockRepository) :
    BaseUseCase<Payload, MergeBlocks.Params>() {

    override suspend fun run(params: Params) = try {
        repo.merge(
            command = Command.Merge(
                context = params.context,
                pair = params.pair
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for merging two blocks into one block
     * @property context context id
     * @property pair pair of the blocks, which we need to merge
     */
    data class Params(
        val context: Id,
        val pair: Pair<Id, Id>
    )
}