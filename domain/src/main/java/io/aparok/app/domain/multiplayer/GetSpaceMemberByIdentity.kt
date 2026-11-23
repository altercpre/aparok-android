package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetSpaceMemberByIdentity @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<GetSpaceMemberByIdentity.Params, ObjectWrapper.SpaceMember?>(dispatchers.io) {

    override suspend fun doWork(params: Params): ObjectWrapper.SpaceMember? {
        val results = repo.searchObjects(
            space = params.space,
            limit = 0,
            filters = buildList {
                add(
                    DVFilter(
                        relation = Relations.LAYOUT,
                        value = ObjectType.Layout.PARTICIPANT.code.toDouble(),
                        condition = DVFilterCondition.EQUAL
                    )
                )
                add(
                    DVFilter(
                        relation = Relations.IDENTITY,
                        value = params.identity,
                        condition = DVFilterCondition.EQUAL
                    )
                )
            },
            keys = listOf(
                Relations.ID,
                Relations.IDENTITY,
                Relations.SPACE_ID,
                Relations.PARTICIPANT_PERMISSIONS,
                Relations.PARTICIPANT_STATUS,
                Relations.NAME,
                Relations.PLURAL_NAME,
                Relations.ICON_IMAGE,
                Relations.LAYOUT
            )
        )
        return if (results.isNotEmpty() && results.first().isNotEmpty()) {
            ObjectWrapper.SpaceMember(results.first())
        } else {
            null
        }
    }

    data class Params(val space: SpaceId, val identity: Id)
}