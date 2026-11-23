package io.aparok.app.domain.cover

import io.aparok.app.domain.base.BaseUseCase

class GetCoverImageCollection(private val provider: CoverCollectionProvider) : BaseUseCase<List<CoverImage>, Unit>() {
    override suspend fun run(params: Unit) = safe {
        provider.provide()
    }
}