package io.aparok.app.domain.download

import io.aparok.app.core_models.FileDownloadLimit
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetFileDownloadLimit @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<FileDownloadLimit, Unit>(dispatchers.io) {

    override suspend fun doWork(params: FileDownloadLimit) {
        settings.setFileDownloadLimit(params)
    }
}
