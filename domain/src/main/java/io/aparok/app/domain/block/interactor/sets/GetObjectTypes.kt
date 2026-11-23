package io.aparok.app.domain.block.interactor.sets

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVSort
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.ext.mapToObjectWrapperType
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetObjectTypes @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetObjectTypes.Params, List<ObjectWrapper.Type>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<ObjectWrapper.Type> {
        val result = repo.searchObjects(
            space = params.space,
            keys = params.keys,
            filters = params.filters,
            sorts = params.sorts,
            limit = params.limit,
            offset = params.offset,
            fulltext = params.query
        )
        return result.mapNotNull { it.mapToObjectWrapperType() }
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