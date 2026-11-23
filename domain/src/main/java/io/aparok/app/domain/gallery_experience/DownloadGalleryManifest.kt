package io.aparok.app.domain.gallery_experience

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.ManifestInfo
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class DownloadGalleryManifest @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<DownloadGalleryManifest.Params, ManifestInfo?>(dispatchers.io) {

    override suspend fun doWork(params: Params): ManifestInfo? {
        val command = Command.DownloadGalleryManifest(
            url = params.url
        )
        return repo.downloadGalleryManifest(command)
    }

    class Params(val url: String)
}