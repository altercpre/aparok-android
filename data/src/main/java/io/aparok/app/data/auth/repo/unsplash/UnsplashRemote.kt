package io.aparok.app.data.auth.repo.unsplash

import io.aparok.app.core_models.Hash
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.UnsplashImage
import io.aparok.app.core_models.primitives.SpaceId

interface UnsplashRemote {
    fun search(query: String, limit: Int) : List<UnsplashImage>
    fun download(
        space: SpaceId,
        id: Id,
        createdInContext: Id? = null,
        createdInContextRef: String? = null
    ): Hash
}