package io.aparok.app.domain.media

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.NO_VALUE
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Position
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class FileDrop @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<FileDrop.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload = repo.fileDrop(
        command = Command.FileDrop(
            ctx = params.ctx,
            space = params.space,
            dropTarget = params.dropTarget,
            blockPosition = params.blockPosition,
            localFilePaths = params.localFilePaths
        )
    )

    data class Params(
        val space: SpaceId,
        val ctx: Id,
        val dropTarget: Id = NO_VALUE,
        val blockPosition: Position = Position.NONE,
        val localFilePaths: List<String>
    )
}