package io.aparok.app.domain.media

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class UploadFile @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UploadFile.Params, ObjectWrapper.File>(dispatchers.io) {

    override suspend fun doWork(params: Params) : ObjectWrapper.File = repo.uploadFile(
        command = Command.UploadFile(
            path = params.path,
            type = params.type,
            space = params.space,
            preloadFileId = params.preloadFileId,
            createdInContext = params.createdInContext,
            createdInContextRef = params.createdInContextRef
        )
    )

    data class Params(
        val path: String,
        val space: SpaceId,
        val type: Block.Content.File.Type = Block.Content.File.Type.FILE,
        val preloadFileId: Id? = null,
        val createdInContext: Id? = null,
        val createdInContextRef: String? = null
    )
}