package io.aparok.app.domain.vault

import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

/**
 * Marks the one-time vault search glow as seen, device-wide.
 *
 * Called from two places:
 * - the vault, when the user taps the highlighted search bar;
 * - the splash, when it finds no account on the device. A fresh install has never
 *   seen the vault, so the cross-space search is not new to them and the glow is
 *   pre-dismissed before they ever reach the vault.
 */
class SetVaultSearchHighlightSeen @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Unit) {
        settings.setHasSeenVaultSearchHighlight(true)
    }
}
