package io.aparok.app.domain.account

import io.aparok.app.core_models.AccountStatus
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class InterceptAccountStatus(
    private val channel: AccountStatusChannel,
    private val dispatchers: AppCoroutineDispatchers
) : FlowUseCase<AccountStatus, BaseUseCase.None>() {
    override fun build(params: BaseUseCase.None?): Flow<AccountStatus> {
        return channel.observe().flowOn(dispatchers.io)
    }
}