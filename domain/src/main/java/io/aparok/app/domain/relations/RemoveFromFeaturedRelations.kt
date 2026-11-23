package io.aparok.app.domain.relations

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for removing one or more relations from featured relations list.
 */
class RemoveFromFeaturedRelations(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RemoveFromFeaturedRelations.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.removeFromFeaturedRelations(
            ctx = params.ctx,
            relations = params.relations
        )
    }

    /**
     * @property [relations] list of ids to remove from existing ones.
     */
    class Params(
        val ctx: Id,
        val relations: List<Id>
    )
}