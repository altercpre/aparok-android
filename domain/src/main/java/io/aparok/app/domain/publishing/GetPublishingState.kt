package io.aparok.app.domain.publishing

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.publishing.Publishing
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetPublishingState @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetPublishingState.Params, Publishing.State?>(dispatchers.io) {

    override suspend fun doWork(params: Params): Publishing.State? {
        val command = Command.Publishing.GetStatus(
            space = params.space,
            objectId = params.objectId
        )
        return repository.publishingGetStatus(command)
    }

    data class Params(
        val space: SpaceId,
        val objectId: Id
    )
}
