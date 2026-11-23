package io.aparok.app.domain.search

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.SearchResult
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.ResultatInteractor
import io.aparok.app.domain.block.repo.BlockRepository

class SearchObjectsByIdWithSubscription(
    private val repo: BlockRepository
) : ResultatInteractor<SearchObjectsByIdWithSubscription.Params, SearchResult>() {

    override suspend fun execute(params: Params): SearchResult {
        return repo.searchObjectsByIdWithSubscription(
            space = params.space,
            subscription = params.subscription,
            ids = params.ids,
            keys = params.keys
        )
    }

    class Params(
        val space: SpaceId,
        val subscription: Id,
        val ids: List<Id>,
        val keys: List<String>
    )
}