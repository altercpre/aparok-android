package io.aparok.app.domain.icon

import io.aparok.app.core_models.Id
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for remove icon from object.
 */
class RemoveDocumentIcon(
    private val repo: BlockRepository
) : RemoveIcon<Id>() {

    override suspend fun run(params: Id) = safe {
        repo.removeDocumentIcon(params)
    }
}