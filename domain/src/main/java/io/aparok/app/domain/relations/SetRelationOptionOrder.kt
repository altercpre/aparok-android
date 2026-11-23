package io.aparok.app.domain.relations

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.primitives.RelationKey
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetRelationOptionOrder @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetRelationOptionOrder.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.SetRelationOptionsOrder(
            space = params.spaceId,
            relationKey = params.relationKey,
            orderedIds = params.orderedIds
        )
        repo.setRelationOptionOrder(
            command
        )
    }

    data class Params(
        val spaceId: SpaceId,
        val relationKey: RelationKey,
        val orderedIds: List<Id>
    )
}
