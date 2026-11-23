package io.aparok.app.domain.templates

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.CreateObjectResult
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectTypeUniqueKeys
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeKey
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateTemplate @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateTemplate.Params, CreateObjectResult>(dispatchers.io) {

    override suspend fun doWork(params: Params): CreateObjectResult {
        val command = Command.CreateObject(
            template = null,
            prefilled = mapOf(Relations.TARGET_OBJECT_TYPE to params.targetObjectTypeId),
            internalFlags = emptyList(),
            space = params.spaceId,
            typeKey = TypeKey(ObjectTypeUniqueKeys.TEMPLATE)
        )
        val result = repo.createObject(command)
        return result
    }

    data class Params(
        val targetObjectTypeId: Id,
        val spaceId: SpaceId
    )
}