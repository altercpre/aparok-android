package io.aparok.app.domain.relations

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for deleting a relation from an object.
 */
class DeleteRelationFromObject(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DeleteRelationFromObject.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.deleteRelationFromObject(
            ctx = params.ctx,
            relations = params.relations
        )
    }

    /**
     * @param ctx id of the object
     * @param relations relation keys
     */
    class Params(
        val ctx: Id,
        val relations: List<Key>
    )
}