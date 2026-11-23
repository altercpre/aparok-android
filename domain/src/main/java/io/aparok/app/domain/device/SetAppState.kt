package io.aparok.app.domain.device

import io.aparok.app.core_models.AppState
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetAppState @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<SetAppState.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repository.setAppState(params.state)
    }

    data class Params(val state: AppState)
} 