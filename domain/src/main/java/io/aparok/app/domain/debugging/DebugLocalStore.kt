package io.aparok.app.domain.debugging

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class DebugLocalStore(private val repo: BlockRepository) : BaseUseCase<String, DebugLocalStore.Params>() {

    override suspend fun run(params: Params): Either<Throwable, String> = safe {
        repo.debugLocalStore(path = params.path)
    }

    class Params(val path: String)
}