package io.aparok.app.domain.templates

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.Result
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.templates.OpenTemplate.Params
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Use-case for opening a template object.
 * @see Params
 */
class OpenTemplate(
    private val repo: BlockRepository,
    private val dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Result<Payload>>(dispatchers.io) {

    override suspend fun doWork(params: Params): Result<Payload> {
        return withContext(dispatchers.io) {
            repo.openObjectPreview(
                id = params.id,
                space = params.space
            )
        }
    }

    /**
     * @property [id] id of the template object.
     */
    class Params(val id: Id, val space: SpaceId)
}