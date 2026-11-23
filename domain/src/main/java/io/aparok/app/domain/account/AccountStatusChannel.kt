package io.aparok.app.domain.account

import io.aparok.app.core_models.AccountStatus
import kotlinx.coroutines.flow.Flow

interface AccountStatusChannel {
    fun observe(): Flow<AccountStatus>
}