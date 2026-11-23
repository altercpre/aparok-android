package io.aparok.app.domain.primitives

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Command.ObjectTypeConflictingFields
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.primitives.SetObjectTypeRecommendedFields.Params
import javax.inject.Inject

class SetObjectTypeRecommendedFields @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ObjectTypeSetRecommendedFields(
            objectTypeId = params.objectTypeId,
            fields = params.fields
        )
        return repo.objectTypeSetRecommendedFields(command = command)
    }

    data class Params(
        val objectTypeId: String,
        val fields: List<Id>
    )
}