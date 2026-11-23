package io.aparok.app.domain.payments

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.membership.Membership
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipStatus @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetMembershipStatus.Params, Membership?>(dispatchers.io) {

    override suspend fun doWork(params: Params): Membership? {
        val command = Command.Membership.GetStatus(
            noCache = params.noCache
        )
        return repo.membershipStatus(command)
    }

    data class Params(
        val noCache: Boolean
    )
}