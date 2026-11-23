package io.aparok.app.domain.auth.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

class CancelAccountMigration @Inject constructor(
    private val repo: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CancelAccountMigration.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        when(params) {
            is Params.Current -> {
                val acc = repo.getCurrentAccount()
                repo.cancelAccountMigration(
                    account = acc.id
                )
            }
            is Params.Other -> {
                repo.cancelAccountMigration(
                    account = params.acc
                )
            }
        }
    }

    sealed class Params {
        data object Current : Params()
        data class Other(val acc: Id) : Params()
    }
}