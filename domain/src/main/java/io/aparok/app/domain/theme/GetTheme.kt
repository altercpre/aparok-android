package io.aparok.app.domain.theme

import io.aparok.app.core_models.ThemeMode
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.config.UserSettingsRepository

class GetTheme(
    private val repo: UserSettingsRepository
) : BaseUseCase<ThemeMode, BaseUseCase.None>() {
    override suspend fun run(params: None) = safe {
        repo.getThemeMode()
    }
}