package io.aparok.app.domain.block.interactor

import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Command
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload

class UpdateFields(private val repo: BlockRepository) :
    BaseUseCase<Payload, UpdateFields.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setFields(
            command = Command.SetFields(
                context = params.context,
                fields = params.fields
            )
        )
    }

    data class Params(
        val context: Id,
        val fields: List<Pair<Id, Block.Fields>>
    )
}