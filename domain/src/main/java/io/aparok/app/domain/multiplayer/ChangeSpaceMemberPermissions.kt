package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class ChangeSpaceMemberPermissions @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ChangeSpaceMemberPermissions.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.changeSpaceMemberPermissions(
            space = params.space,
            identity = params.identity,
            permission = params.permission
        )
    }

    data class Params(
        val space: SpaceId,
        val identity: Id,
        val permission: SpaceMemberPermissions
    )
}