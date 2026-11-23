package io.aparok.app.domain.all_content

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class UpdateAllContentState @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UpdateAllContentState.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        settings.setAllContentSort(
            space = params.spaceId,
            sort = params.sort,
            isAsc = params.isAsc
        )
    }

    data class Params(
        val spaceId: SpaceId,
        val sort: Id,
        val isAsc: Boolean
    )
}