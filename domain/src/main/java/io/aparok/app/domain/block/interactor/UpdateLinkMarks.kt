package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.ext.rangeIntersection

/**
 * Adds new link mark to the list of marks and
 * remove all link marks with intersected ranges
 * with new mark.
 */

open class UpdateLinkMarks : BaseUseCase<List<Block.Content.Text.Mark>, UpdateLinkMarks.Params>() {

    override suspend fun run(params: Params): Either<Throwable, List<Block.Content.Text.Mark>> =
        try {
            val result = mutableListOf<Block.Content.Text.Mark>()
            params.marks.forEach {
                if (it.type != Block.Content.Text.Mark.Type.LINK) {
                    result.add(it)
                } else {
                    if (it.rangeIntersection(params.newMark.range) == 0) {
                        result.add(it)
                    }
                }
            }
            result.add(params.newMark)
            Either.Right(result.toList())
        } catch (t: Throwable) {
            Either.Left(t)
        }

    /**
     * @property marks Collection of marks to update
     * @property newMark Link mark to add
     */
    class Params(
        val marks: List<Block.Content.Text.Mark>,
        val newMark: Block.Content.Text.Mark
    )
}