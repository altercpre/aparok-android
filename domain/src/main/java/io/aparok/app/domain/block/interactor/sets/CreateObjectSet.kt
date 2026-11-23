package io.aparok.app.domain.block.interactor.sets

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Struct
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class CreateObjectSet(
    private val repo: BlockRepository
) : BaseUseCase<CreateObjectSet.Response, CreateObjectSet.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.createSet(
            space = params.space,
            objectType = params.type,
            details = params.details
        )
    }

    data class Params(
        val space: Id,
        val type: Id? = null,
        val details: Struct? = null
    )

    /**
     * @property [target] id of the new set
     */
    data class Response(
        val target: Id,
        val payload: Payload,
        val details: Struct
    )
}