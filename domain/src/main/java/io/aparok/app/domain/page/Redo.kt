package io.aparok.app.domain.page

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for re-doing latest changes in document.
 */
class Redo @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Redo.Params, Redo.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params): Result {
        return repo.redo(
            command = Command.Redo(
                context = params.context
            )
        )
    }

    /**
     * Params for redoing latest changes in document.
     * @property context id of the context
     */
    data class Params(
        val context: Id
    )

    sealed class Result {
        data class Success(val payload: Payload) : Result()
        object Exhausted : Result()
    }
}