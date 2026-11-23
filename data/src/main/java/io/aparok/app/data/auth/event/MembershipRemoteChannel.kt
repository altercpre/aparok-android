package io.aparok.app.data.auth.event

import io.aparok.app.core_models.membership.Membership
import io.aparok.app.core_models.membership.MembershipTiers
import io.aparok.app.domain.workspace.MembershipChannel
import kotlinx.coroutines.flow.Flow

interface MembershipRemoteChannel {
    fun observe(): Flow<List<Membership.Event>>
    fun observeTiers(): Flow<List<MembershipTiers.Event>>
}

class MembershipDateChannel(
    private val channel: MembershipRemoteChannel
) : MembershipChannel {

    override fun observe(): Flow<List<Membership.Event>> {
        return channel.observe()
    }

    override fun observeTiers(): Flow<List<MembershipTiers.Event>> {
        return channel.observeTiers()
    }
}