package io.aparok.app.domain.spaces

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetHomepage @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<SetHomepage.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setHomepage(
            Command.SetHomepage(
                spaceId = params.spaceId,
                homepage = params.homepage
            )
        )
    }

    data class Params(val spaceId: Id, val homepage: Id)
}
