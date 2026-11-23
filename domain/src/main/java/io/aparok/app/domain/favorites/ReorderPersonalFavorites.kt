package io.aparok.app.domain.favorites

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class ReorderPersonalFavorites @Inject constructor(
    private val repo: PersonalFavoritesRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ReorderPersonalFavorites.Params, List<Payload>>(dispatchers.io) {

    data class Params(
        val space: SpaceId,
        val order: List<Id>
    )

    override suspend fun doWork(params: Params): List<Payload> =
        repo.reorder(params.space, params.order)
}
