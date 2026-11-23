package io.aparok.app.domain.objects

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Struct
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.objects.GetDateObjectByTimestamp.Params
import javax.inject.Inject

class GetDateObjectByTimestamp @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Struct?>(dispatchers.io) {

    override suspend fun doWork(params: Params): Struct? {
        val command = Command.ObjectDateByTimestamp(
            space = params.space,
            timeInSeconds = params.timestampInSeconds
        )
        return repo.objectDateByTimestamp(command)
    }

    data class Params(val space: SpaceId, val timestampInSeconds: Long)
}