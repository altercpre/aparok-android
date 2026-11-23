package io.aparok.app.domain.cover

interface CoverCollectionProvider {
    fun provide(): List<CoverImage>
}