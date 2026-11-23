package io.aparok.app.domain.spaces

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Struct
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for adding one object to a space.
 * Returns id of the object added to the given space.
 */
class AddObjectToSpace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddObjectToSpace.Params, AddObjectToSpace.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params): Result {
        val command = Command.AddObjectToSpace(
            objectId = params.obj,
            space = params.space
        )
        val result = repo.addObjectToSpace(command)
        return Result(
            id = result.first,
            type = result.second
        )
    }

    data class Params(
        val obj: Id,
        val space: Id
    )

    data class Result(
        val id: Id,
        val type: Struct?
    )
}

typealias AddObjectTypeToSpace = AddObjectToSpace