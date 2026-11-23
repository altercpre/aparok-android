package io.aparok.app.domain.page

import io.aparok.app.core_models.Id
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.Result
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

open class OpenPage @Inject constructor(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<OpenPage.Params, Result<Payload>>(dispatchers.io) {

    override suspend fun doWork(params: Params): Result<Payload> {
        return repo.openPage(
            id = params.obj,
            space = params.space
        )
    }

    data class Params(
        val obj: Id,
        val saveAsLastOpened: Boolean,
        val space: SpaceId
    )
}