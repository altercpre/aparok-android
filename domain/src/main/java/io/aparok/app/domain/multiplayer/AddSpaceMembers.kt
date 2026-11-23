package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class AddSpaceMembers @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddSpaceMembers.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.addSpaceMembers(
            space = params.space,
            identities = params.identities,
            permissions = params.permissions
        )
    }

    /**
     * @property [identities] identities of members to add to a given space
     * @property [permissions] permissions to assign to the added members
     */
    data class Params(
        val space: SpaceId,
        val identities: List<Id>,
        val permissions: SpaceMemberPermissions = SpaceMemberPermissions.READER
    )
}
