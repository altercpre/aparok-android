package io.aparok.app.domain.primitives

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.primitives.SetObjectTypeHeaderRecommendedFields.Params
import javax.inject.Inject

class SetObjectTypeHeaderRecommendedFields @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ObjectTypeSetRecommendedHeaderFields(
            objectTypeId = params.objectTypeId,
            fields = params.fields
        )
        return repo.objectTypeSetRecommendedHeaderFields(command = command)
    }

    data class Params(
        val objectTypeId: String,
        val fields: List<Id>
    )
}