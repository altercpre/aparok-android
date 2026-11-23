package io.aparok.app.domain.spaces

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class SaveCurrentSpace @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: UserSettingsRepository
) : ResultInteractor<SaveCurrentSpace.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setCurrentSpace(params.space)
    }

    class Params(val space: SpaceId)
}