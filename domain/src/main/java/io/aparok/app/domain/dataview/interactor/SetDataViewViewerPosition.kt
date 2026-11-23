package io.aparok.app.domain.dataview.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository

/**
 * Use-case for ordering views of the data view.
 */
class SetDataViewViewerPosition(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetDataViewViewerPosition.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload = repo.setDataViewViewerPosition(
        ctx = params.ctx,
        view = params.viewer,
        dv = params.dv,
        pos = params.pos
    )

    /**
     * @property [ctx] set or collection id
     * @property [dv] data view block id
     * @property [viewer] view id, whose position is changed
     * @property [pos] new position of the [viewer]
     */
    data class Params(
        val ctx: Id,
        val dv: Id,
        val viewer: Id,
        val pos: Int
    )
}