package io.aparok.app.domain.sets

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Result
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository

@Deprecated("Legacy. Need to migrate to OpenObject useCase")
class OpenObjectSet(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository
) : BaseUseCase<Result<Payload>, OpenObjectSet.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.openObjectSet(
            id = params.obj,
            space = params.space
        ).also {
            settings.setLastOpenedObject(
                id = params.obj,
                space = params.space
            )
        }
    }

    data class Params(
        val space: SpaceId,
        val obj: Id
    )
}