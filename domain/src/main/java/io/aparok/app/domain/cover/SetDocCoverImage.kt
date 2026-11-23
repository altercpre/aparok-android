package io.aparok.app.domain.cover

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Hash
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class SetDocCoverImage(
    private val repo: BlockRepository
) : BaseUseCase<Payload, SetDocCoverImage.Params>() {

    override suspend fun run(params: Params) = safe {
        when (params) {
            is Params.FromPath -> {
                val file = repo.uploadFile(
                    command = Command.UploadFile(
                        path = params.path,
                        type = Block.Content.File.Type.IMAGE,
                        space = params.space,
                        createdInContext = params.context,
                        createdInContextRef = Relations.COVER_ID
                    )
                )
                repo.setDocumentCoverImage(
                    ctx = params.context,
                    hash = file.id
                )
            }
            is Params.FromHash -> {
                repo.setDocumentCoverImage(
                    ctx = params.context,
                    hash = params.hash
                )
            }
        }
    }

    /**
     * Params for for setting document's image cover
     */
    sealed class Params {
        abstract val context: String
        /**
         * @property path image path in file system
         * @property context id of the context for this operation
         * @property space target space
         */
        data class FromPath(
            override val context: String,
            val path: String,
            val space: SpaceId
        ) : Params()
        data class FromHash(
            override val context: String,
            val hash: Hash
        ) : Params()
    }
}