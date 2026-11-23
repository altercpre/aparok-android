package io.aparok.app.domain.dashboard.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetObjectListIsFavorite @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetObjectListIsFavorite.Params, Unit>(dispatchers.io) {

    data class Params(
        val objectIds: List<Id>,
        val isFavorite: Boolean
    )

    override suspend fun doWork(params: Params) =
        repo.setObjectListIsFavorite(params.objectIds, params.isFavorite)
}