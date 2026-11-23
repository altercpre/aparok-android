package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id

/**
 * Use-case for turning simple blocks into documents.
 * Each target is expected to be turned into a document.
 * Returns a list of link ids.
 */
class TurnIntoDocument(
    private val repo: BlockRepository
) : BaseUseCase<List<Id>, TurnIntoDocument.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.turnIntoDocument(
            command = Command.TurnIntoDocument(
                context = params.context,
                targets = params.targets
            )
        )
    }

    /**
     * Params for turning simple blocks into documents
     * @property context id of the context
     * @property targets id of the targets
     */
    data class Params(
        val context: Id,
        val targets: List<Id>
    )
}