package io.aparok.app.middleware.interactor
import io.aparok.app.middleware.EventGroup

import io.aparok.app.core_models.AccountStatus
import io.aparok.app.data.auth.account.AccountStatusRemoteChannel
import io.aparok.app.middleware.EventProxy
import io.aparok.app.middleware.mappers.core
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class AccountStatusMiddlewareChannel(
    private val events: EventProxy
) : AccountStatusRemoteChannel {

    override fun observe(): Flow<AccountStatus> = events.flow(EventGroup.ACCOUNT).mapNotNull { e ->
        val updates = e.messages.filter { m ->
            m.accountUpdate != null
        }
        val lastUpdate = updates.lastOrNull()?.accountUpdate

        lastUpdate?.status?.core()
    }
}