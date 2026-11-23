package io.aparok.app.domain.networkmode

import io.aparok.app.core_models.NetworkModeConfig
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class GetNetworkMode @Inject constructor(
    private val repository: AuthRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<Unit, NetworkModeConfig>(dispatchers.io) {

    override suspend fun doWork(params: Unit): NetworkModeConfig {
        return repository.getNetworkMode()
    }
}