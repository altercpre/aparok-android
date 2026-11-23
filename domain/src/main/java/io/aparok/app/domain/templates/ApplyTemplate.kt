package io.aparok.app.domain.templates

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class ApplyTemplate(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<ApplyTemplate.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ApplyTemplate(
            objectId = params.ctx,
            template = params.template
        )
        return repo.applyTemplate(command)
    }

    /**
     * @param ctx id of the object to apply template to
     * @param template id of the template to apply, could be null, in that case the blank template will be applied
     */
    data class Params(
        val ctx: Id,
        val template: Id?
    )
}