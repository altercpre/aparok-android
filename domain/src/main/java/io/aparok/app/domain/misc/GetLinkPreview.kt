package io.aparok.app.domain.misc

import io.aparok.app.core_models.LinkPreview
import io.aparok.app.core_models.Url
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetLinkPreview @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Url, LinkPreview>(dispatchers.io) {
    override suspend fun doWork(params: Url): LinkPreview {
        return repo.getLinkPreview(url = params)
    }
}