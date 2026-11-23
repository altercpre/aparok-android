package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectView
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class OpenObject @Inject constructor(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<OpenObject.Params, ObjectView>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.openObject(
        id = params.obj,
        space = params.spaceId
    )

    data class Params(
        val obj: Id,
        val saveAsLastOpened: Boolean = true,
        val spaceId: SpaceId
    )
}