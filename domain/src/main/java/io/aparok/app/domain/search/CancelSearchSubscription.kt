package io.aparok.app.domain.search

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.objects.ObjectStore

class CancelSearchSubscription(
    private val repo: BlockRepository,
    private val store: ObjectStore
) : BaseUseCase<Unit, CancelSearchSubscription.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.cancelObjectSearchSubscription(params.subscriptions)
        store.unsubscribe(params.subscriptions)
    }

    class Params(val subscriptions: List<Id>)
}