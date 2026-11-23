package io.aparok.app.domain.`object`

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

// TODO rename to SetObjectDetails
class UpdateDetail(private val repo: BlockRepository): BaseUseCase<Payload, UpdateDetail.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setObjectDetail(
            ctx = params.target,
            key = params.key,
            value = params.value
        )
    }

    data class Params(
        val target: Id,
        val key: String,
        val value: Any?
    )
}