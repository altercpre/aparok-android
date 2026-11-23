package io.aparok.app.domain.wallpaper

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Wallpaper
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetSpaceWallpapers @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, Map<Id, Wallpaper>>(dispatchers.io) {

    override suspend fun doWork(params: Unit): Map<Id, Wallpaper> {
        return repo.getWallpapers()
    }
}