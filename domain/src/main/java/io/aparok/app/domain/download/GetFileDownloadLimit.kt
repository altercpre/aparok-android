package io.aparok.app.domain.download

import io.aparok.app.core_models.FileDownloadLimit
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetFileDownloadLimit @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, FileDownloadLimit>(dispatchers.io) {

    override suspend fun doWork(params: Unit): FileDownloadLimit {
        return settings.getFileDownloadLimit()
    }
}
