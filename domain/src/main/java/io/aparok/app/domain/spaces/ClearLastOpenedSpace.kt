package io.aparok.app.domain.spaces

import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class ClearLastOpenedSpace @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<Unit, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Unit) {
        return repo.clearCurrentSpace()
    }
}