package io.aparok.app.domain.payments

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.membership.NameServiceNameType
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class IsMembershipNameValid @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<IsMembershipNameValid.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.Membership.IsNameValid(
            tier = params.tier,
            name = params.name,
            nameType = params.nameType
        )
        repo.membershipIsNameValid(command)
    }

    data class Params(
        val tier: Int,
        val name: String,
        val nameType: NameServiceNameType = NameServiceNameType.ANY_NAME
    )
}