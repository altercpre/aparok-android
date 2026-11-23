package io.aparok.app.domain.payments

import io.aparok.app.core_models.Command
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetMembershipEmail @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<SetMembershipEmail.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        if (params.isFromOnboarding) {
            // Onboarding path always subscribes to newsletter;
            // params.subscribeToNewsletter is intentionally ignored here.
            repo.membershipSubscribeToUpdates(params.email)
        } else {
            val command = Command.Membership.GetVerificationEmail(
                email = params.email,
                subscribeToNewsletter = params.subscribeToNewsletter,
                isFromOnboarding = params.isFromOnboarding
            )
            repo.membershipGetVerificationEmail(command)
        }
    }

    data class Params(
        val email: String,
        val subscribeToNewsletter: Boolean,
        val isFromOnboarding: Boolean = false
    )
}