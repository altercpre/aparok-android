package io.aparok.app.domain.all_content

import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class RestoreAllContentState @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RestoreAllContentState.Params, RestoreAllContentState.Response>(
    dispatchers.io
) {

    override suspend fun doWork(params: Params): Response {
        val res = settings.getAllContentSort(params.spaceId)
        return if (res == null) {
            Response.Empty
        } else {
            Response.Success(activeSort = res.first, isAsc = res.second)
        }
    }

    data class Params(
        val spaceId: SpaceId
    )

    sealed class Response {
        data class Success(
            val activeSort: String,
            val isAsc: Boolean
        ) : Response()
        data object Empty : Response()
    }
}