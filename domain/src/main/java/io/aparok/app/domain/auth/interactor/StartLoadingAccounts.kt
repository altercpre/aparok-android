package io.aparok.app.domain.auth.interactor

import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import javax.inject.Inject

/**
 * Use case for starting loading remote accounts associated with current wallet.
 */
class StartLoadingAccounts @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<Unit, StartLoadingAccounts.Params>() {

    override suspend fun run(params: Params) = try {
        repository.startLoadingAccounts().let {
            Either.Right(it)
        }
    } catch (e: Throwable) {
        Either.Left(e)
    }

    class Params
}