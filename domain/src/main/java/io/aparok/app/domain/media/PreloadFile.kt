package io.aparok.app.domain.media

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Preloads file for uploading.
 * @return id of the file
 * @see [Params]
 */
class PreloadFile @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<PreloadFile.Params, Id>(dispatchers.io) {

    override suspend fun doWork(params: Params) : Id = repo.preloadFile(
        command = Command.PreloadFile(
            path = params.path,
            type = params.type,
            space = params.space
        )
    )

    data class Params(
        val path: String,
        val space: SpaceId,
        val type: Block.Content.File.Type = Block.Content.File.Type.FILE
    )
}