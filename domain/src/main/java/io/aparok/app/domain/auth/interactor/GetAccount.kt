package io.aparok.app.domain.auth.interactor

import io.aparok.app.core_models.Account
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class GetAccount @Inject constructor(
    private val repo: AuthRepository,
    dispatcher: AppCoroutineDispatchers
) : ResultInteractor<Unit, Account>(dispatcher.io) {

    override suspend fun doWork(params: Unit): Account {
        return repo.getCurrentAccount()
    }
}