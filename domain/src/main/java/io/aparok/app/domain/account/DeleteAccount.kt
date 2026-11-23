package io.aparok.app.domain.account

import io.aparok.app.core_models.AccountStatus
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.BaseUseCase

class DeleteAccount(private val repo: AuthRepository) : BaseUseCase<AccountStatus, BaseUseCase.None>() {
    override suspend fun run(params: None) = safe { repo.deleteAccount() }
}