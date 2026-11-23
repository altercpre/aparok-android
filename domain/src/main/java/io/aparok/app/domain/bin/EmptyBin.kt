package io.aparok.app.domain.bin

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.workspace.SpaceManager

class EmptyBin(
    private val repo: BlockRepository,
    private val spaceManager: SpaceManager,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, List<Id>>(dispatchers.io) {
    override suspend fun doWork(params: Unit) : List<Id> {
        val archived = repo.searchObjects(
            // TODO DROID-2916 Move space id to use case params
            space = SpaceId(spaceManager.get()),
            filters = listOf(
                DVFilter(
                    relation = Relations.IS_ARCHIVED,
                    condition = DVFilterCondition.EQUAL,
                    value = true
                ),
                DVFilter(
                    relation = Relations.IS_DELETED,
                    condition = DVFilterCondition.NOT_EQUAL,
                    value = true
                ),
                DVFilter(
                    relation = Relations.IS_HIDDEN,
                    condition = DVFilterCondition.NOT_EQUAL,
                    value = true
                ),
                DVFilter(
                    relation = Relations.IS_HIDDEN_DISCOVERY,
                    condition = DVFilterCondition.NOT_EQUAL,
                    value = true
                )
            ),
            sorts = emptyList(),
            fulltext = "",
            limit = 0,
            offset = 0
        ).map { struct ->
            ObjectWrapper.Basic(struct).id
        }
        repo.deleteObjects(
            targets = archived
        )
        return archived
    }
}