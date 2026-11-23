package io.aparok.app.domain.spaces

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.DVSort
import io.aparok.app.core_models.DVSortType
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.restrictions.SpaceStatus
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetSpaceViews @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<SpaceId, List<ObjectWrapper.SpaceView>>(dispatchers.io) {
    override suspend fun doWork(params: SpaceId): List<ObjectWrapper.SpaceView> {
        val result = repo.searchObjects(
            space = params,
            keys = listOf(
                Relations.ID,
                Relations.TARGET_SPACE_ID,
                Relations.NAME,
                Relations.LAYOUT,
                Relations.ICON_IMAGE,
                Relations.ICON_OPTION,
                Relations.SPACE_ACCOUNT_STATUS,
                Relations.SPACE_LOCAL_STATUS,
                Relations.LAST_OPENED_DATE
            ),
            filters = listOf(
                DVFilter(
                    relation = Relations.LAYOUT,
                    value = ObjectType.Layout.SPACE_VIEW.code.toDouble(),
                    condition = DVFilterCondition.EQUAL
                ),
                DVFilter(
                    relation = Relations.SPACE_ACCOUNT_STATUS,
                    value = buildList {
                        add(SpaceStatus.SPACE_DELETED.code.toDouble())
                        add(SpaceStatus.SPACE_REMOVING.code.toDouble())
                    },
                    condition = DVFilterCondition.NOT_IN
                ),
                DVFilter(
                    relation = Relations.SPACE_LOCAL_STATUS,
                    value = SpaceStatus.OK.code.toDouble(),
                    condition = DVFilterCondition.EQUAL
                )
            ),
            sorts = listOf(
                DVSort(
                    relationKey = Relations.LAST_OPENED_DATE,
                    type = DVSortType.DESC,
                    includeTime = true,
                    relationFormat = RelationFormat.DATE
                )
            ),
            limit = 0
        )
        return result.map {
            ObjectWrapper.SpaceView(it)
        }
    }
}