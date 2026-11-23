package io.aparok.app.domain.templates

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateTemplateFromObject @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateTemplateFromObject.Params, Id>(
    dispatchers.io
) {

    override suspend fun doWork(params: Params): Id {
        return repo.createTemplateFromObject(params.obj)
    }

    data class Params(val obj: Id)
}