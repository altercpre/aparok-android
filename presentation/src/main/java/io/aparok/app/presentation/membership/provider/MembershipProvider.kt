package io.aparok.app.presentation.membership.provider

import io.aparok.app.core_models.membership.MembershipStatus
import io.aparok.app.core_models.membership.TierId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sovereign no-op MembershipProvider.
 * In Aparok, membership, paywalls, and tiers are completely removed.
 * No spoofed tiers or remote billing checks.
 */
interface MembershipProvider {

    fun status(forceRefresh: Boolean = false): Flow<MembershipStatus> = emptyFlow()
    fun activeTier(): Flow<TierId> = emptyFlow()

    @Singleton
    class Default @Inject constructor() : MembershipProvider
}
