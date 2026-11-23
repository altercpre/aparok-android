package io.aparok.app.domain.spaces

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.SpaceCreationUseCase
import io.aparok.app.core_models.Struct
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateSpace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateSpace.Params, Command.CreateSpace.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.createWorkspace(
        command = Command.CreateSpace(
            details = params.details,
            useCase = params.useCase
        )
    )

    data class Params(
        val details: Struct,
        val useCase: SpaceCreationUseCase = SpaceCreationUseCase.GET_STARTED_MOBILE
    )
}