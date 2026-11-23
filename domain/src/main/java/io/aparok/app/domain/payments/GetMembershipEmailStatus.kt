package io.aparok.app.domain.payments

import io.aparok.app.core_models.membership.EmailVerificationStatus
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipEmailStatus @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<Unit, EmailVerificationStatus>(dispatchers.io) {

    override suspend fun doWork(params: Unit): EmailVerificationStatus {
        return repo.membershipGetVerificationEmailStatus()
    }
}