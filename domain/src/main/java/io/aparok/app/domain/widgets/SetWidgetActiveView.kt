package io.aparok.app.domain.widgets

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for setting active view id for widget with list or compact list layout.
 */
class SetWidgetActiveView @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetWidgetActiveView.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.setWidgetViewId(
        ctx = params.ctx,
        widget = params.widget,
        view = params.view
    )

    data class Params(
        val ctx: Id,
        val widget: Id,
        val view: Id
    )
}