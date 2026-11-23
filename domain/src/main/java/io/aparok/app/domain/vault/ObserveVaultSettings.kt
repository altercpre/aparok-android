package io.aparok.app.domain.vault

import io.aparok.app.core_models.settings.VaultSettings
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.FlowInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest

class ObserveVaultSettings @Inject constructor(
    private val awaitAccountStart: AwaitAccountStartManager,
    private val settings: UserSettingsRepository,
    private val auth: AuthRepository,
    private val logger: Logger,
    dispatchers: AppCoroutineDispatchers
): FlowInteractor<Unit, VaultSettings>(dispatchers.io) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun build(): Flow<VaultSettings> = awaitAccountStart
        .awaitStart()
        .flatMapLatest {
            val acc = auth.getCurrentAccount()
            settings.observeVaultSettings(acc)
        }.catch {
            logger.logException(it, "Error while observing vault settings")
            emit(VaultSettings.default())
        }

    override fun build(params: Unit): Flow<VaultSettings> = build()
}