package io.aparok.app.domain.workspace

import io.aparok.app.core_models.NodeUsageInfo
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class SpacesUsageInfo(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, NodeUsageInfo>(dispatchers.io) {

    override suspend fun doWork(params: Unit): NodeUsageInfo {
        return repo.nodeUsage()
    }
}