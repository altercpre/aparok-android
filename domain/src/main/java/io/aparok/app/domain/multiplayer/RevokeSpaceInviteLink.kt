package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.multiplayer.SpaceInviteLinkAccessLevel
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.invite.SpaceInviteLinkStore
import javax.inject.Inject

class RevokeSpaceInviteLink @Inject constructor(
    private val repo: BlockRepository,
    private val store: SpaceInviteLinkStore,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, Unit>(dispatchers.io) {
    override suspend fun doWork(params: SpaceId) {
        val result = repo.revokeSpaceInviteLink(space = params)

        // Update the store to reflect that the invite link is now disabled
        store.update(params, SpaceInviteLinkAccessLevel.LinkDisabled())

        return result
    }
}