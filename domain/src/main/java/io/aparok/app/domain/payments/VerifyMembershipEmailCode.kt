package io.aparok.app.domain.payments

import io.aparok.app.core_models.Command
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class VerifyMembershipEmailCode @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<VerifyMembershipEmailCode.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.Membership.VerifyEmailCode(
            code = params.code
        )
        repo.membershipVerifyEmailCode(command)
    }

    data class Params(
        val code: String
    )
}