package io.aparok.app.domain.debugging

import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class DebugAccountSelectTrace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DebugAccountSelectTrace.Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        return repo.debugAccountSelectTrace(params.dir)
    }

    data class Params(val dir: String)
}