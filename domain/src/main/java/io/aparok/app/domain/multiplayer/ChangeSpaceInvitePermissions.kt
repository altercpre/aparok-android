package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use case for changing space invite link permissions
 * This is used for Editor ↔ Viewer transitions without regenerating the link
 */
class ChangeSpaceInvitePermissions @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<ChangeSpaceInvitePermissions.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.SpaceChangeInvite(
            space = params.space,
            permissions = params.permissions
        )
        repo.spaceChangeInvite(command)
    }

    data class Params(
        val space: SpaceId,
        val permissions: SpaceMemberPermissions
    )
}