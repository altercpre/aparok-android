package io.aparok.app.domain.debugging

import io.aparok.app.core_models.Filepath
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class DebugSpaceShareDownloader @Inject constructor(
    private val debugSpace: DebugSpace,
    private val debugSpaceContentSaver: DebugSpaceContentSaver,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<Unit, Filepath>(dispatchers.io) {
    override suspend fun doWork(params: Unit): Filepath {
        val content = debugSpace.run(Unit)
        val file = debugSpaceContentSaver.save(content = content)
        return file.path
    }
}