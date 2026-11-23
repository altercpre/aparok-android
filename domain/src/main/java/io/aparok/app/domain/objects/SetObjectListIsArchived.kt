package io.aparok.app.domain.objects

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.objects.SetObjectListIsArchived.Params
import javax.inject.Inject

/**
 * Use-case for archiving (or restoring from archive) a list of objects.
 */
class SetObjectListIsArchived @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Unit>(dispatchers.io) {

    /**
     * Params for archiving a list of objects.
     * @property [targets] id of the objects to archive/restore.
     */
    data class Params(
        val targets: List<Id>,
        val isArchived: Boolean
    )

    override suspend fun doWork(params: Params) = repo.setObjectListIsArchived(
        params.targets, params.isArchived
    )
}