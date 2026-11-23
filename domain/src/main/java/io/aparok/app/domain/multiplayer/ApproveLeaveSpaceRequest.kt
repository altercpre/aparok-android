package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class ApproveLeaveSpaceRequest @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ApproveLeaveSpaceRequest.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.approveSpaceLeaveRequest(
            command = Command.ApproveSpaceLeaveRequest(
                space = params.space,
                identities = params.identities
            )
        )
    }

    data class Params(
        val space: SpaceId,
        val identities: List<Id>
    )
}