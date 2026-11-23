package io.aparok.app.domain.relations

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVSort
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.block.repo.BlockRepository

class GetRelations(
    private val repo: BlockRepository
) : BaseUseCase<List<ObjectWrapper.Relation>, GetRelations.Params>() {

    override suspend fun run(params: Params) = safe { proceedWithUseCase(params) }

    suspend fun execute(params: Params) : List<ObjectWrapper.Relation> {
        return proceedWithUseCase(params)
    }

    private suspend fun proceedWithUseCase(params: Params) = repo.searchObjects(
        space = params.space,
        keys = params.keys,
        filters = params.filters,
        sorts = params.sorts,
        limit = params.limit,
        offset = params.offset,
        fulltext = params.query
    ).map { struct ->
        ObjectWrapper.Relation(struct)
    }

    data class Params(
        val space: SpaceId,
        val sorts: List<DVSort> = emptyList(),
        val filters: List<DVFilter> = emptyList(),
        val keys: List<Key> = emptyList(),
        val offset: Int = 0,
        val limit: Int = 0,
        val query: String = ""
    )
}