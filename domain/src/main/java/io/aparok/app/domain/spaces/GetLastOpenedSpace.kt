package io.aparok.app.domain.spaces

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetLastOpenedSpace @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<Unit, SpaceId?>(dispatchers.io) {

    override suspend fun doWork(params: Unit): SpaceId? {
        return repo.getCurrentSpace()
    }
}