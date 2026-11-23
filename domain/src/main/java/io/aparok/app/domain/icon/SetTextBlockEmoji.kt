package io.aparok.app.domain.icon

import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository

class SetTextBlockEmoji(
    private val repo: BlockRepository
) : SetEmojiIcon<TextBlockTarget>() {

    override suspend fun run(
        params: Params<TextBlockTarget>
    ) = safe {
        repo.setTextIcon(
            command = Command.SetTextIcon(
                context = params.target.context,
                blockId = params.target.blockId,
                icon = Command.SetTextIcon.Icon.Emoji(params.emoji)
            )
        )
    }
}