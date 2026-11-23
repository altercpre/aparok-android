package io.aparok.app.domain.wallpaper

import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.FlowInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.workspace.SpaceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Pipeline flow for restoring per-space wallpaper.
 * Reacts to currently active space changes.
 */
class RestoreWallpaper(
    private val repo: UserSettingsRepository,
    private val spaceManager: SpaceManager,
    private val store: WallpaperStore,
    dispatchers: AppCoroutineDispatchers
) : FlowInteractor<Unit, Unit>(dispatchers.io) {

    override fun build(): Flow<Unit> = spaceManager
        .observe()
        .map { config ->
            val wallpaper = repo.getWallpaper(space = config.space)
            store.set(wallpaper)
        }
        .catch {
            // Do nothing.
        }

    override fun build(params: Unit): Flow<Unit> = build()
}