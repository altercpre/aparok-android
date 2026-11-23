package io.aparok.app.domain.workspace

import io.aparok.app.core_models.membership.Membership
import io.aparok.app.core_models.membership.MembershipTiers
import kotlinx.coroutines.flow.Flow

interface MembershipChannel {
    fun observe(): Flow<List<Membership.Event>>
    fun observeTiers(): Flow<List<MembershipTiers.Event>>
}