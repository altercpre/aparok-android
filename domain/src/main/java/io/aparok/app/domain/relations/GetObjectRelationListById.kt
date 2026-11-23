package io.aparok.app.domain.relations

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.RelationListWithValueItem
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.relations.GetObjectRelationListById.Params
import javax.inject.Inject

class GetObjectRelationListById @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, List<RelationListWithValueItem>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<RelationListWithValueItem> {
        val command = Command.RelationListWithValue(
            space = params.space,
            value = params.value
        )
        return repo.objectRelationListWithValue(command)
    }

    data class Params(val space: SpaceId, val value: Id)
}