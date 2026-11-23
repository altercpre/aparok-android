package io.aparok.app.domain.dataview

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetDataViewProperties @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetDataViewProperties.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val params = Command.SetDataViewProperties(
            objectId = params.objectId,
            blockId = DEFAULT_DATA_VIEW_BLOCK_ID,
            properties = params.properties
        )
        return repo.setDataViewProperties(command = params)
    }

    companion object {
        const val DEFAULT_DATA_VIEW_BLOCK_ID = "dataview"
    }

    data class Params(
        val objectId: String,
        val properties: List<Key>
    )
}