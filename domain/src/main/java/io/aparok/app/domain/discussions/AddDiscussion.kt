package io.aparok.app.domain.discussions

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class AddDiscussion @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Id, Id>(dispatchers.io) {

    override suspend fun doWork(params: Id): Id {
        return repo.addDiscussion(params)
    }
}
