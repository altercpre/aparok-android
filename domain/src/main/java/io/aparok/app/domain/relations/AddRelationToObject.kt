package io.aparok.app.domain.relations

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for adding a relation to an object.
 */
class AddRelationToObject(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddRelationToObject.Params, Payload?>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload? {
        return repo.addRelationToObject(
            ctx = params.ctx,
            relation = params.relationKey
        )
    }

    /**
     * @param ctx id of the object
     * @param relationKey relation key
     */
    class Params(
        val ctx: Id,
        val relationKey: Key
    )
}