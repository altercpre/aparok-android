package io.aparok.app.domain.publishing

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class RemovePublishing @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RemovePublishing.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.Publishing.Remove(
            space = params.space,
            objectId = params.objectId
        )
        repository.publishingRemove(command)
    }

    data class Params(
        val space: SpaceId,
        val objectId: Id
    )
}
