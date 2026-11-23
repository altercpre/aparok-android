package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Payload

class UploadBlock(private val repo: BlockRepository) : BaseUseCase<Payload, UploadBlock.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.uploadBlock(
            command = Command.UploadBlock(
                contextId = params.contextId,
                blockId = params.blockId,
                url = params.url,
                filePath = params.filePath
            )
        )
    }

    data class Params(
        val contextId: String,
        val blockId: String,
        val url: String,
        val filePath: String
    )
}