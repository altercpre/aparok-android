package io.aparok.app.domain.theme

import io.aparok.app.core_models.ThemeMode
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.config.UserSettingsRepository

class SetTheme(
    private val repo: UserSettingsRepository
) : BaseUseCase<Unit, ThemeMode>() {
    override suspend fun run(params: ThemeMode) = safe {
        repo.setThemeMode(params)
    }
}