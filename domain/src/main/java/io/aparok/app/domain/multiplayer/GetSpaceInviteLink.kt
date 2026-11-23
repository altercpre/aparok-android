package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.multiplayer.SpaceInviteError
import io.aparok.app.core_models.multiplayer.SpaceInviteLink
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetSpaceInviteLink @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, SpaceInviteLink>(dispatchers.io) {

    override suspend fun doWork(params: SpaceId): SpaceInviteLink {
        val invite = repo.getSpaceInviteLink(spaceId = params)
        // A member's device gets a successful response with empty cid + key when
        // the invite is held by the owner. There is no link to hand out in that case.
        if (!invite.isLinkVisible) throw SpaceInviteError.InviteNotActive
        return invite
    }
}