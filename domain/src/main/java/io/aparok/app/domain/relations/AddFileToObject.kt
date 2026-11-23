package io.aparok.app.domain.relations

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.ext.addIds
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.block.repo.BlockRepository

class AddFileToObject(
    private val repo: BlockRepository
) : BaseUseCase<Payload, AddFileToObject.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        val file = repo.uploadFile(
            command = Command.UploadFile(
                path = params.path,
                type = null,
                space = params.space,
                createdInContext = params.ctx,
                createdInContextRef = params.relation
            )
        )
        val obj = params.obj
        val remaining = obj[params.relation].addIds(listOf(file.id))
        repo.setObjectDetail(
            ctx = params.ctx,
            key = params.relation,
            value = remaining
        )
    }

    class Params(
        val ctx: Id,
        val relation: Id,
        val obj: Map<String, Any?>,
        val path: String,
        val space: SpaceId
    )
}