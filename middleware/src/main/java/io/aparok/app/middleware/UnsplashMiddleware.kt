package io.aparok.app.middleware

import anytype.Rpc.Unsplash.Download
import anytype.Rpc.Unsplash.Search
import io.aparok.app.core_models.Hash
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.UnsplashImage
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.data.auth.repo.unsplash.UnsplashRemote
import io.aparok.app.middleware.interactor.MiddlewareProtobufLogger
import io.aparok.app.middleware.mappers.core
import io.aparok.app.middleware.service.MiddlewareService
import javax.inject.Inject

class UnsplashMiddleware @Inject constructor(
    private val service: MiddlewareService,
    private val logger: MiddlewareProtobufLogger
) : UnsplashRemote {

    override fun search(query: String, limit: Int): List<UnsplashImage> {
        val request = Search.Request(
            query = query,
            limit = limit
        ).also { logger.logRequest(it) }
        val response = service.unsplashSearch(request = request).also { logger.logResponse(it) }
        return response.pictures.map { p -> p.core() }
    }

    override fun download(
        space: SpaceId,
        id: Id,
        createdInContext: Id?,
        createdInContextRef: String?
    ): Hash {
        val request = Download.Request(
            pictureId = id,
            spaceId = space.id,
            createdInContext = createdInContext.orEmpty(),
            createdInContextRef = createdInContextRef.orEmpty()
        ).also {
            logger.logRequest(it)
        }
        val response = service.unsplashDownload(request = request).also { logger.logResponse(it) }
        return response.objectId
    }
}