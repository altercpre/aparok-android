package io.aparok.app.domain.widgets

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Position
import io.aparok.app.core_models.WidgetLayout
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateWidget @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateWidget.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.createWidget(
            ctx = params.ctx,
            source = params.source,
            layout = params.type,
            target = params.target,
            position = params.position
        )
    }

    data class Params(
        val ctx: Id,
        val source: Id,
        val type: WidgetLayout,
        val target: Id? = null,
        val position: Position = Position.NONE
    )
}