package io.aparok.app.domain.payments

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.membership.MembershipTierData
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipTiers @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetMembershipTiers.Params, List<MembershipTierData>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<MembershipTierData> {
        val command = Command.Membership.GetTiers(
            locale = params.locale,
            noCache = params.noCache
        )
        return repo.membershipGetTiers(command)
    }

    data class Params(
        val locale: String,
        val noCache: Boolean
    )
}