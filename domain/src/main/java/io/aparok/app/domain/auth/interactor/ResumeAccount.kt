package io.aparok.app.domain.auth.interactor

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.workspace.SpaceManager
import javax.inject.Inject

/**
 * Thrown by [ResumeAccount] when the stored mnemonic is missing on resume.
 *
 * Expected on legitimate states — e.g. the user logged out (or their account was
 * deleted) and the OS killed the process before the activity could navigate to
 * splash, leaving us to attempt resume against a wiped wallet on the next launch.
 * Callers should treat it as "not signed in" rather than as a bug to log.
 */
class MnemonicEmptyException : IllegalStateException("Mnemonic is empty")

/**
 * Blocking use-case for resuming account session when application is killed by OS.
 */
class ResumeAccount @Inject constructor(
    private val repository: AuthRepository,
    private val pathProvider: PathProvider,
    private val configStorage: ConfigStorage,
    private val initialParamsProvider: InitialParamsProvider,
    private val awaitAccountStartManager: AwaitAccountStartManager,
    private val settings: UserSettingsRepository,
    private val spaceManager: SpaceManager
) : BaseUseCase<Id, BaseUseCase.None>() {

    override suspend fun run(params: None) = proceedWithResuming()

    private suspend fun proceedWithResuming() = safe {
        repository.setInitialParams(initialParamsProvider.toCommand())
        val mnemonic = repository.getMnemonic()
        if (mnemonic.isNullOrBlank()) throw MnemonicEmptyException()
        repository.recoverWallet(
            path = pathProvider.providePath(),
            mnemonic = mnemonic
        )

        val networkMode = repository.getNetworkMode()

        val currentAccountId = repository.getCurrentAccountId()

        val command = Command.AccountSelect(
            id = currentAccountId,
            path = pathProvider.providePath(),
            networkMode = networkMode.networkMode,
            networkConfigFilePath = networkMode.storedFilePath
        )
        val result = repository.selectAccount(command).let { setup ->
            configStorage.set(config = setup.config, accountId = currentAccountId)
            val lastSessionSpace = settings.getCurrentSpace()
            if (lastSessionSpace != null) {
                val result = spaceManager.set(lastSessionSpace.id)
                if (result.isFailure) {
                    // Falling back to the default space
                    spaceManager.set(setup.config.space)
                }
            }
            setup.account.id
        }
        awaitAccountStartManager.setState(AwaitAccountStartManager.State.Started)
        result
    }
}