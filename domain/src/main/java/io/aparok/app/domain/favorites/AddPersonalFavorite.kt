package io.aparok.app.domain.favorites

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class AddPersonalFavorite @Inject constructor(
    private val repo: PersonalFavoritesRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddPersonalFavorite.Params, Payload>(dispatchers.io) {

    data class Params(
        val space: SpaceId,
        val target: Id
    )

    override suspend fun doWork(params: Params): Payload =
        repo.add(params.space, params.target)
}
