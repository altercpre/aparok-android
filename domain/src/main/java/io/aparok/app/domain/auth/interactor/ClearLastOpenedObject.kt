package io.aparok.app.domain.auth.interactor

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

/**
 * Use case for clearing last open object's id from user session.
 * @see SaveLastOpenedObject
 */
class ClearLastOpenedObject @Inject constructor(
    private val repo: UserSettingsRepository
) : BaseUseCase<Unit, ClearLastOpenedObject.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.clearLastOpenedObject(params.space)
    }

    data class Params(val space: SpaceId)
}