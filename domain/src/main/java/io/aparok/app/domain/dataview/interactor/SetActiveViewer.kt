package io.aparok.app.domain.dataview.interactor

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class SetActiveViewer(private val repo: BlockRepository, dispatchers: AppCoroutineDispatchers) :
    ResultInteractor<SetActiveViewer.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.DataViewSetActiveView(
            ctx = params.ctx,
            viewerId = params.viewer,
            dataViewId = params.dv
        )
        return repo.dataViewSetActiveView(command)
    }

    data class Params(
        val ctx: Id,
        val viewer: Id,
        val dv: Id
    )
}