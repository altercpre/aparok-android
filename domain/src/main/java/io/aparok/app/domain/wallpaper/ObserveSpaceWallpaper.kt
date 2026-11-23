package io.aparok.app.domain.wallpaper

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Wallpaper
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.FlowInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ObserveSpaceWallpaper @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : FlowInteractor<ObserveSpaceWallpaper.Params, Wallpaper>(dispatchers.io) {

    override fun build(): Flow<Wallpaper> {
        return userSettingsRepository.observeWallpaper("")
            .catch { 
                emit(Wallpaper.Default)
            }
    }

    override fun build(params: Params): Flow<Wallpaper> {
        return userSettingsRepository.observeWallpaper(params.space)
    }

    data class Params(
        val space: Id
    )
}