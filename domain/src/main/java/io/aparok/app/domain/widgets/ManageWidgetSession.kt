package io.aparok.app.domain.widgets

import io.aparok.app.core_models.WidgetSession
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetWidgetSession @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, WidgetSession>(dispatchers.io) {
    override suspend fun doWork(params: Unit): WidgetSession = repo.getWidgetSession()
}

class SaveWidgetSession @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SaveWidgetSession.Params, Unit>(dispatchers.io) {
    override suspend fun doWork(params: Params) {
        repo.saveWidgetSession(params.session)
    }

    data class Params(val session: WidgetSession)
}