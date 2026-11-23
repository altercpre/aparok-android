package io.aparok.app.domain.notifications

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class ReplyNotifications @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
): ResultInteractor<List<Id>, Unit>(dispatchers.io) {
    override suspend fun doWork(params: List<Id>) {
        repo.replyNotifications(notifications = params)
    }
}