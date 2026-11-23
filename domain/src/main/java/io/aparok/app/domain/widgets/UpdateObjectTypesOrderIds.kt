package io.aparok.app.domain.widgets

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class UpdateObjectTypesOrderIds @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UpdateObjectTypesOrderIds.Params, List<Id>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Id> {
        val command = Command.ObjectTypesSetOrder(
            spaceId = params.spaceId.id,
            orderedIds = params.orderedIds
        )
        return repository.objectTypesSetOrder(command)
    }

    data class Params(
        val spaceId: SpaceId,
        val orderedIds: List<String>
    )
}