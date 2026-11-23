package io.aparok.app.domain.payments

import io.aparok.app.core_models.membership.MembershipFeatures
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipFeatures @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, MembershipFeatures>(dispatchers.io) {

    override suspend fun doWork(params: Unit): MembershipFeatures {
        return repo.membershipV2GetFeatures()
    }
}
