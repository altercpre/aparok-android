package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class DuplicateObject(
    private val repo: BlockRepository
) : BaseUseCase<Id, Id>() {

    override suspend fun run(params: Id) = safe {
        repo.duplicateObject(params)
    }
}