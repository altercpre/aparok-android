package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id

open class UpdateText(
    private val repo: BlockRepository
) : BaseUseCase<Unit, UpdateText.Params>() {

    override suspend fun run(params: Params) = try {
        repo.updateText(
            command = Command.UpdateText(
                contextId = params.context,
                blockId = params.target,
                text = params.text,
                marks = params.marks
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    data class Params(
        val context: Id,
        val target: Id,
        val text: String,
        val marks: List<Block.Content.Text.Mark>
    )
}