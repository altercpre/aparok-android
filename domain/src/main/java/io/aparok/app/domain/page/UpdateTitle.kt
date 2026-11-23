package io.aparok.app.domain.page

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id

/**
 * Use-case for updating document's title
 */
class UpdateTitle(
    private val repo: BlockRepository
) : BaseUseCase<Unit, UpdateTitle.Params>() {

    override suspend fun run(params: Params) = try {
        repo.updateDocumentTitle(
            command = Command.UpdateTitle(
                context = params.context,
                title = params.title
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for updating document's title
     * @property context id of the context
     * @property title new title for the document
     */
    data class Params(
        val context: Id,
        val title: String
    )
}