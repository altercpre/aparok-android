package io.aparok.app.domain.icon

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Relations
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetDocumentImageIcon @Inject constructor(
    private val repo: BlockRepository
) : SetImageIcon<Id>() {

    override suspend fun run(params: Params<Id>) = safe {
        val file = repo.uploadFile(
            command = Command.UploadFile(
                path = params.path,
                type = Block.Content.File.Type.IMAGE,
                space = params.spaceId,
                createdInContext = params.target,
                createdInContextRef = Relations.ICON_IMAGE
            )
        )
        val payload = repo.setDocumentImageIcon(
            command = Command.SetDocumentImageIcon(
                id = file.id,
                context = params.target
            )
        )
        Pair(payload, file.id)
    }
}