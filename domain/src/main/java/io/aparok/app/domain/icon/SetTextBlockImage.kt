package io.aparok.app.domain.icon

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Relations
import io.aparok.app.domain.block.repo.BlockRepository

class SetTextBlockImage(
    private val repo: BlockRepository
) : SetImageIcon<TextBlockTarget>() {

    override suspend fun run(
        params: Params<TextBlockTarget>
    ) = safe {
        val file = repo.uploadFile(
            command = Command.UploadFile(
                path = params.path,
                type = Block.Content.File.Type.IMAGE,
                space = params.spaceId,
                createdInContext = params.target.context,
                createdInContextRef = Relations.ICON_IMAGE
            )
        )
        val payload = repo.setTextIcon(
            command = Command.SetTextIcon(
                icon = Command.SetTextIcon.Icon.Image(file.id),
                context = params.target.context,
                blockId = params.target.blockId
            )
        )
        Pair(payload, file.id)
    }
}