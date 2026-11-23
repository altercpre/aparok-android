package io.aparok.app.domain.launch

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetDefaultObjectType @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetDefaultObjectType.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setDefaultObjectType(
            space = params.space,
            type = params.type
        )
    }

    data class Params(
        val space: SpaceId,
        val type: TypeId,
    )
}