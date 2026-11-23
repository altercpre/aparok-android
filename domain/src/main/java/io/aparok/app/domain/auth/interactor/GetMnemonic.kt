package io.aparok.app.domain.auth.interactor

import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either

/**
 * Use case for retrieving mnemonic associated with current user.
 */
class GetMnemonic(
    private val repository: AuthRepository
) : BaseUseCase<String, Unit>() {

    override suspend fun run(params: Unit) = try {
        repository.getMnemonic().let { mnemonic ->
            if (mnemonic.isNullOrBlank()) throw IllegalStateException("Mnemonic is empty")
            Either.Right(mnemonic)
        }
    } catch (e: Throwable) {
        Either.Left(e)
    }
}