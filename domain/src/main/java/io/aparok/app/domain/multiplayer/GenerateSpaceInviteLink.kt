package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.multiplayer.InviteType
import io.aparok.app.core_models.multiplayer.SpaceInviteLink
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GenerateSpaceInviteLink @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GenerateSpaceInviteLink.Params, SpaceInviteLink>(dispatchers.io) {

    override suspend fun doWork(params: Params): SpaceInviteLink {
        return repo.generateSpaceInviteLink(
            space = params.space,
            inviteType = params.inviteType,
            permissions = params.permissions,
            shareWithinSpace = params.shareWithinSpace
        )
    }

    data class Params(
        val space: SpaceId,
        val inviteType: InviteType?,
        val permissions: SpaceMemberPermissions?,
        val shareWithinSpace: Boolean = false
    )
}