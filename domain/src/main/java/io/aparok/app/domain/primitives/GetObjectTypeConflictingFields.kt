package io.aparok.app.domain.primitives

import io.aparok.app.core_models.Command.ObjectTypeConflictingFields
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetObjectTypeConflictingFields @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetObjectTypeConflictingFields.Params, List<Id>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Id> {
        val command = ObjectTypeConflictingFields(
            spaceId = params.spaceId,
            objectTypeId = params.objectTypeId
        )
        return repo.objectTypeListConflictingRelations(command = command)
    }

    data class Params(
        val spaceId: String,
        val objectTypeId: String
    )
}