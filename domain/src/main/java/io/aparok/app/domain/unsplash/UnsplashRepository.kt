package io.aparok.app.domain.unsplash

import io.aparok.app.core_models.Hash
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.UnsplashImage
import io.aparok.app.core_models.primitives.SpaceId

interface UnsplashRepository {
    fun search(query: String, limit: Int) : List<UnsplashImage>
    fun download(
        id: Id,
        space: SpaceId,
        createdInContext: Id? = null,
        createdInContextRef: String? = null
    ): Hash
}