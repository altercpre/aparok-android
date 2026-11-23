package io.aparok.app.data.auth.repo

import io.aparok.app.domain.config.InfrastructureRepository

class InfrastructureDataRepository(
    private val cache: DebugSettingsCache
) : InfrastructureRepository {

    override suspend fun enableAnytypeContextMenu() {
        cache.enableAnytypeContextMenu()
    }

    override suspend fun disableAnytypeContextMenu() {
        cache.disableAnytypeContextMenu()
    }

    override suspend fun getAnytypeContextMenu(): Boolean = cache.getAnytypeContextMenu()
}