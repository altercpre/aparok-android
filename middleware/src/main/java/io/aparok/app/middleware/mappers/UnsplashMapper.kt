package io.aparok.app.middleware.mappers

import anytype.Rpc.Unsplash.Search.Response.Picture
import io.aparok.app.core_models.UnsplashImage

fun Picture.core() : UnsplashImage = UnsplashImage(
    id = id,
    url = url,
    artist = UnsplashImage.Artist(
        name = artist,
        url = artistUrl
    )
)