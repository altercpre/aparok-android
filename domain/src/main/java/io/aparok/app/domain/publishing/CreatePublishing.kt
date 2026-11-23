package io.aparok.app.domain.publishing

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreatePublishing @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreatePublishing.Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        val command = Command.Publishing.Create(
            space = params.space,
            objectId = params.objectId,
            uri = params.uri,
            showJoinSpaceBanner = params.showJoinSpaceBanner
        )
        return repository.publishingCreate(command)
    }

    data class Params(
        val space: SpaceId,
        val objectId: Id,
        val uri: String,
        val showJoinSpaceBanner: Boolean = false
    )
}
