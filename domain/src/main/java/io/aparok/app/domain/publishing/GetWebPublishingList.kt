package io.aparok.app.domain.publishing

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.publishing.Publishing
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetWebPublishingList @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetWebPublishingList.Params, List<Publishing.State>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Publishing.State> {
        val command = Command.Publishing.GetList(
            space = params.space
        )
        return repository.publishingGetList(command)
    }

    data class Params(
        val space: SpaceId?
    )
}