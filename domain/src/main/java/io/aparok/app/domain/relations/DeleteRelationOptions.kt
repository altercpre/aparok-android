package io.aparok.app.domain.relations

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class DeleteRelationOptions @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DeleteRelationOptions.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.DeleteRelationOptions(optionIds = params.optionIds)
        repo.deleteRelationOption(command)
    }

    data class Params(val optionIds: List<Id>)
}