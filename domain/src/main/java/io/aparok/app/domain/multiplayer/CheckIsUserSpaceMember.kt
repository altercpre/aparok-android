package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.debugging.Logger
import javax.inject.Inject

class CheckIsUserSpaceMember @Inject constructor(
    private val repo: BlockRepository,
    private val auth: AuthRepository,
    private val logger: Logger,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, Boolean>(dispatchers.io) {

    override suspend fun doWork(params: SpaceId): Boolean {
        val account = auth.getCurrentAccountId()
        val results = repo.searchObjects(
            space = params,
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
                        value = account,
                        condition = DVFilterCondition.EQUAL
                    )
                )
            },
            keys = listOf(
                Relations.ID,
                Relations.NAME,
                Relations.PLURAL_NAME,
                Relations.SPACE_ID,
                Relations.PARTICIPANT_PERMISSIONS,
                Relations.PARTICIPANT_STATUS,
                Relations.IDENTITY,
                Relations.LAYOUT
            )
        )
        val struct = results.firstOrNull()
        return if (!struct.isNullOrEmpty()) {
            val member = ObjectWrapper.SpaceMember(struct)
            val permission = member.permissions
            if (permission == null || permission == SpaceMemberPermissions.NO_PERMISSIONS)
                false
            else
                true
        } else {
            false
        }
    }
}