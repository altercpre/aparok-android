package io.aparok.app.domain.block.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class SetObjectType(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetObjectType.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.setObjectTypeToObject(
            ctx = params.context,
            objectTypeKey = params.objectTypeKey
        )
    }

    class Params(
        val context: Id,
        val objectTypeKey: Key
    )
}