package io.aparok.app.domain.types

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TypeId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetPinnedObjectTypes @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetPinnedObjectTypes.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setPinnedObjectTypes(
            space = params.space,
            types = params.types
        )
    }

    class Params(
        val space: SpaceId,
        val types: List<TypeId>
    )
}