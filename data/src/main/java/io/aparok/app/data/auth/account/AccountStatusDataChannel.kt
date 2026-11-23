package io.aparok.app.data.auth.account

import io.aparok.app.core_models.AccountStatus
import io.aparok.app.domain.account.AccountStatusChannel
import kotlinx.coroutines.flow.Flow

class AccountStatusDataChannel(
    private val remote: AccountStatusRemoteChannel
) : AccountStatusChannel {
    override fun observe(): Flow<AccountStatus> = remote.observe()
}