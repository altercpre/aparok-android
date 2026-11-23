package io.aparok.app.domain.auth.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.config.UserSettingsRepository

/**
 * Use case for saving last open object's id for restoring user session.
 * @see GetLastOpenedObject
 * @see ClearLastOpenedObject
 */
class SaveLastOpenedObject(
    private val repo: UserSettingsRepository
) : BaseUseCase<Unit, SaveLastOpenedObject.Params>() {


    override suspend fun run(params: Params) = safe {
        repo.setLastOpenedObject(
            id = params.obj,
            space = params.space
        )
    }

    data class Params(val obj: Id, val space: SpaceId)
}