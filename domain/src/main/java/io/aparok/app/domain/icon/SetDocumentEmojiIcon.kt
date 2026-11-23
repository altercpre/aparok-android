package io.aparok.app.domain.icon

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for setting emoji icon.
 */
class SetDocumentEmojiIcon(
    private val repo: BlockRepository
) : SetEmojiIcon<Id>() {

    override suspend fun run(params: SetEmojiIcon.Params<Id>) = safe {
        repo.setDocumentEmojiIcon(
            command = Command.SetDocumentEmojiIcon(
                context = params.target,
                emoji = params.emoji
            )
        )
    }
}