package io.aparok.app.domain.unsplash

import io.aparok.app.core_models.UnsplashImage
import io.aparok.app.domain.base.BaseUseCase

class SearchUnsplashImage(
    private val repo: UnsplashRepository
) : BaseUseCase<List<UnsplashImage>, SearchUnsplashImage.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.search(query = params.query, limit = params.limit)
    }

    class Params(
        val query: String,
        val limit: Int = DEFAULT_LIMIT
    )

     companion object {
         const val DEFAULT_LIMIT = 36
     }
}