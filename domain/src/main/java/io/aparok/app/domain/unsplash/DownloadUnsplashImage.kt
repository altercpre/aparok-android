package io.aparok.app.domain.unsplash

import io.aparok.app.core_models.Hash
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.base.BaseUseCase

class DownloadUnsplashImage(
    private val repo: UnsplashRepository
) : BaseUseCase<Hash, DownloadUnsplashImage.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.download(
            id = params.picture,
            space = params.space,
            createdInContext = params.createdInContext,
            createdInContextRef = params.createdInContextRef
        )
    }

    class Params(
        val picture: Id,
        val space: SpaceId,
        val createdInContext: Id? = null,
        val createdInContextRef: String? = null
    )
}