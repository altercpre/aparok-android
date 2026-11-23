package io.aparok.app.domain.search

import io.aparok.app.core_models.GlobalSearchHistory
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class UpdateGlobalSearchHistory @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UpdateGlobalSearchHistory.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        settings.setGlobalSearchHistory(
            globalSearchHistory = GlobalSearchHistory(
                query = params.query,
                relatedObject = params.relatedObjectId
            ),
            space = params.spaceId
        )
    }

    data class Params(
        val spaceId: SpaceId,
        val query: String,
        val relatedObjectId: Id?
    )
}