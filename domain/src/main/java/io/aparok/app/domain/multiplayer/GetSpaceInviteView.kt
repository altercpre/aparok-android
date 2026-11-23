package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.multiplayer.SpaceInviteView
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetSpaceInviteView @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetSpaceInviteView.Params, SpaceInviteView>(dispatchers.io) {

    override suspend fun doWork(params: Params): SpaceInviteView {
        return repo.getSpaceInviteView(
            inviteFileKey = params.inviteFileKey,
            inviteContentId = params.inviteContentId
        )
    }

    data class Params(
        val inviteContentId: String,
        val inviteFileKey: String
    )
}