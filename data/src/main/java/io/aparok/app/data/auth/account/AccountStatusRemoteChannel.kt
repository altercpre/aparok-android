package io.aparok.app.data.auth.account

import io.aparok.app.core_models.AccountStatus
import kotlinx.coroutines.flow.Flow

interface AccountStatusRemoteChannel {
    fun observe(): Flow<AccountStatus>
}