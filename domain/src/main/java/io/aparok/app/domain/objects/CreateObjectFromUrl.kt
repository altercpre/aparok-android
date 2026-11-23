package io.aparok.app.domain.objects

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Url
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateObjectFromUrl @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateObjectFromUrl.Params, ObjectWrapper.Basic>(dispatchers.io) {

    override suspend fun doWork(params: Params): ObjectWrapper.Basic {
        return repository.createObjectFromUrl(
            space = params.space,
            url = params.url,
            createdInContext = params.createdInContext
        )
    }

    data class Params(
        val space: SpaceId,
        val url: Url,
        val createdInContext: Id? = null
    )
}