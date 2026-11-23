package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectView
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

/**
 * Use-case for opening an object as preview — without subscribing to its subsequent changes.
 * If you want to receive payload events, you should use [OpenObject] instead.
 */
class GetObject @Inject constructor(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetObject.Params, ObjectView>(dispatchers.io) {
    override suspend fun doWork(params: Params): ObjectView = repo.getObject(
        id = params.target,
        space = params.space
    )

    data class Params(
        val target: Id,
        val space: SpaceId,
        val saveAsLastOpened: Boolean = false
    )
}