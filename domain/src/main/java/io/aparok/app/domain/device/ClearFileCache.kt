package io.aparok.app.domain.device

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Interactor
import io.aparok.app.domain.block.repo.BlockRepository

class ClearFileCache(
    private val repo: BlockRepository
): Interactor<BaseUseCase.None>() {
    override suspend fun run(params: BaseUseCase.None) { repo.clearFileCache() }
}