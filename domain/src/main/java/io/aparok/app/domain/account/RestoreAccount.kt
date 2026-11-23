package io.aparok.app.domain.account

import io.aparok.app.core_models.AccountStatus
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.BaseUseCase

class RestoreAccount(
    private val repo: AuthRepository,
    dispatchers: AppCoroutineDispatchers,
) : BaseUseCase<AccountStatus, BaseUseCase.None>(context = dispatchers.io) {

    override suspend fun run(params: None) = safe {
        repo.restoreAccount()
    }
}