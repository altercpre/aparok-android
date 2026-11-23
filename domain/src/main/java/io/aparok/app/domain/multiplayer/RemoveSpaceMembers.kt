package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class RemoveSpaceMembers @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<RemoveSpaceMembers.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.removeSpaceMembers(
            space = params.space,
            identities = params.identities
        )
    }

    /**
     * @property [identities] identities of members of a given space
     */
    data class Params(val space: SpaceId, val identities: List<Id>)
}