package io.aparok.app.domain.auth.interactor

import io.aparok.app.core_models.Account
import io.aparok.app.domain.auth.model.AuthStatus
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

/**
 * Use case for checking authorisation status.
 * User can be either authorized or unauthorized based on number of available accounts.
 * @param repository repository containing information about available accounts
 */
class CheckAuthorizationStatus @Inject constructor(
    private val repository: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, Pair<AuthStatus, Account?>>(dispatchers.io) {

    override suspend fun doWork(params: Unit): Pair<AuthStatus, Account?> {
        return repository.getAccounts().let { accounts ->
            val mnemonic = repository.getMnemonic()
            if (accounts.isNotEmpty() && !mnemonic.isNullOrBlank())
                AuthStatus.AUTHORIZED to accounts.firstOrNull()
            else
                AuthStatus.UNAUTHORIZED to null
        }
    }
}