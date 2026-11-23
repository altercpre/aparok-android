package io.aparok.app.domain.vault

import io.aparok.app.core_models.settings.VaultSettings
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetVaultSettings @Inject constructor(
    private val settings: UserSettingsRepository,
    private val auth: AuthRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<Unit, VaultSettings>(dispatchers.io) {

    override suspend fun doWork(params: Unit): VaultSettings {
        val acc = auth.getCurrentAccount()
        return settings.getVaultSettings(acc)
    }
}