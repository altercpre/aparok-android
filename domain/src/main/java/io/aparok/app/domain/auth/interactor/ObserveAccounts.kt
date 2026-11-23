package io.aparok.app.domain.auth.interactor

import io.aparok.app.core_models.Account
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.FlowUseCase
import javax.inject.Inject

class ObserveAccounts @Inject constructor(
    private val repository: AuthRepository
) : FlowUseCase<Account, Unit>() {

    override fun build(
        params: Unit?
    ) = repository.observeAccounts()
}