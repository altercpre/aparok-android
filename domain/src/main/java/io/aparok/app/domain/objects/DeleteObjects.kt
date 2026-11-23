package io.aparok.app.domain.objects

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for deleting objects.
 * @see SetObjectListIsArchived
 */
class DeleteObjects @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DeleteObjects.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.deleteObjects(params.targets)

    /**
     * @property [targets] id of the objects to delete.
     */
    class Params(val targets: List<Id>)
}