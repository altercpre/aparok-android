package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class ImportGetStartedUseCase(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<ImportGetStartedUseCase.Params, Command.ImportUseCase.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params): Command.ImportUseCase.Result {
        return repo.importGetStartedUseCase(params.space)
    }

    data class Params(val space: Id)
}