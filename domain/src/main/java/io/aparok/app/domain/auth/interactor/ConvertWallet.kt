package io.aparok.app.domain.auth.interactor

import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import javax.inject.Inject

class ConvertWallet @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<String, ConvertWallet.Request>() {

    override suspend fun run(params: Request): Either<Throwable, String> = safe {
        val mnemonic = authRepository.convertWallet(entropy = params.entropy)
        authRepository.saveMnemonic(mnemonic)
        mnemonic
    }

    data class Request(val entropy: String)
}