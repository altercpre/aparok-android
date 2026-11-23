package io.aparok.app.domain.search

import io.aparok.app.core_models.GlobalSearchHistory
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class RestoreGlobalSearchHistory @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RestoreGlobalSearchHistory.Params, RestoreGlobalSearchHistory.Response>(
    dispatchers.io
) {

    override suspend fun doWork(params: Params): Response {
        return Response(
            globalSearchHistory = settings.setGlobalSearchHistory(
                space = params.spaceId
            )
        )
    }

    data class Params(
        val spaceId: SpaceId
    )

    data class Response(
        val globalSearchHistory: GlobalSearchHistory?
    )
}