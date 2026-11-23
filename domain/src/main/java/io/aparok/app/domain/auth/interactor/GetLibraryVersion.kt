package io.aparok.app.domain.auth.interactor

import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either

open class GetLibraryVersion(
    private val repo: AuthRepository
) : BaseUseCase<String, BaseUseCase.None>() {

    override suspend fun run(params: BaseUseCase.None): Either<Throwable, String> = safe {
        repo.getVersion().removePrefix(PREFIX)
    }

    companion object {
        const val PREFIX = "v"
    }
}