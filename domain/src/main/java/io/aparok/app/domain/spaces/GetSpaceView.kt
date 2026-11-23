package io.aparok.app.domain.spaces

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import javax.inject.Inject

class GetSpaceView @Inject constructor(
    private val repo: BlockRepository,
    private val configStorage: ConfigStorage,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<GetSpaceView.Params, ObjectWrapper.Basic?>(dispatchers.io) {

    override suspend fun doWork(params: Params): ObjectWrapper.Basic? {
        when(params) {
            is Params.BySpaceViewId -> {
                val techSpace = configStorage.getOrNull()?.techSpace ?: return null
                val result = repo.searchObjects(
                    space = SpaceId(techSpace),
                    filters = buildList {
                        add(
                            DVFilter(
                                relation = Relations.ID,
                                value = params.spaceViewId,
                                condition = DVFilterCondition.EQUAL
                            )
                        )
                    },
                    limit = 1
                )
                return if (result.isNotEmpty()) {
                    ObjectWrapper.Basic(result.first())
                } else {
                    null
                }
            }
            is Params.BySpaceId -> {
                val result = repo.searchObjects(
                    space = params.space,
                    filters = buildList {
                        add(
                            DVFilter(
                                relation = Relations.TARGET_SPACE_ID,
                                value = params.space.id,
                                condition = DVFilterCondition.EQUAL
                            )
                        )
                    },
                    limit = 1
                )
                return if (result.isNotEmpty()) {
                    ObjectWrapper.Basic(result.first())
                } else {
                    null
                }
            }
        }
    }

    sealed class Params {
        data class BySpaceViewId(val spaceViewId: Id) : Params()
        data class BySpaceId(val space: SpaceId) : Params()
    }
}