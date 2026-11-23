package io.aparok.app.domain.gallery_experience

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class ImportExperience @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<ImportExperience.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ImportExperience(
            space = params.spaceId,
            url = params.url,
            title = params.title,
            isNewSpace = params.isNewSpace
        )
        repo.importExperience(command)
    }

    data class Params(
        val spaceId: SpaceId,
        val url: String,
        val title: String,
        val isNewSpace: Boolean
    )
}