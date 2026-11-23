package io.aparok.app.domain.networkmode

import io.aparok.app.core_models.NetworkModeConfig
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class SetNetworkMode @Inject constructor(
    private val repository: AuthRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<SetNetworkMode.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repository.setNetworkMode(params.networkModeConfig)
    }

    data class Params(val networkModeConfig: NetworkModeConfig)
}