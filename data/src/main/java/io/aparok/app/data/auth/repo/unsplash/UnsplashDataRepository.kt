package io.aparok.app.data.auth.repo.unsplash

import io.aparok.app.core_models.Hash
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.UnsplashImage
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.unsplash.UnsplashRepository

class UnsplashDataRepository(
    private val remote: UnsplashRemote
) : UnsplashRepository {
    override fun search(
        query: String,
        limit: Int
    ): List<UnsplashImage> = emptyList()

    override fun download(
        id: Id,
        space: SpaceId,
        createdInContext: Id?,
        createdInContextRef: String?
    ): Hash = ""
}