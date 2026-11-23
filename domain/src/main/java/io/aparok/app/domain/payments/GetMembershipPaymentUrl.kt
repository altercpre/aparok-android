package io.aparok.app.domain.payments

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.membership.GetPaymentUrlResponse
import io.aparok.app.core_models.membership.MembershipPaymentMethod
import io.aparok.app.core_models.membership.NameServiceNameType
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipPaymentUrl @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetMembershipPaymentUrl.Params, GetPaymentUrlResponse>(dispatchers.io) {

    override suspend fun doWork(params: Params): GetPaymentUrlResponse {
        val command = Command.Membership.GetPaymentUrl(
            tier = params.tierId,
            name = params.name,
            nameType = params.nameType,
            paymentMethod = params.paymentMethod
        )
        return repo.membershipGetPaymentUrl(command)
    }

    data class Params(
        val tierId: Int,
        val name: String,
        val nameType: NameServiceNameType = NameServiceNameType.ANY_NAME,
        val paymentMethod: MembershipPaymentMethod = MembershipPaymentMethod.METHOD_INAPP_GOOGLE
    )
}