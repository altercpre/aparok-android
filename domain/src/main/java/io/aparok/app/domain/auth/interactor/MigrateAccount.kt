package io.aparok.app.domain.auth.interactor

import io.aparok.app.core_models.Id
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.device.PathProvider
import javax.inject.Inject

class MigrateAccount @Inject constructor(
    private val repo: AuthRepository,
    private val path: PathProvider,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<MigrateAccount.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        when(params) {
            is Params.Current -> {
                val acc = repo.getCurrentAccount()
                val path = path.providePath()
                repo.migrateAccount(
                    account = acc.id,
                    path = path
                )
            }
            is Params.Other -> {
                val path = path.providePath()
                repo.migrateAccount(
                    account = params.acc,
                    path = path
                )
            }
        }
    }

    sealed class Params {
        data object Current : Params()
        data class Other(val acc: Id) : Params()
    }
}

