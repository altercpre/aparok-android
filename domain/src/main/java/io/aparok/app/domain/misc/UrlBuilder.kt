package io.aparok.app.domain.misc

import io.aparok.app.core_models.Url
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.config.Gateway

/**
 * Helper class for building urls for files and images
 * @property gateway gateway data
 * @property tokenProvider optional session token provider appended as query parameter (?token=...)
 */
class UrlBuilderImpl(
    val gateway: Gateway,
    private val tokenProvider: (() -> String)? = null
): UrlBuilder {

    private fun appendToken(url: String): String {
        val token = tokenProvider?.invoke().orEmpty()
        if (token.isEmpty()) return url
        val separator = if (url.contains("?")) "&" else "?"
        return "$url${separator}token=$token"
    }

    /**
     * Builds large image url for given [path]
     */
    override fun large(path: String): Url = appendToken(gateway.provide() + IMAGE_PATH + path + LARGE_WIDTH_ALT_PARAM)

    /**
     * Builds original image url for given [path]
     */
    override fun original(path: String): Url = appendToken(gateway.provide() + IMAGE_PATH + path)

    /**
     * Builds small image url for given [path]
     */
    override fun thumbnail(path: String): Url = medium(path)

    /**
     * Builds medium image url for given [path]
     */
    override fun medium(path: String): Url = appendToken(gateway.provide() + IMAGE_PATH + path + MEDIUM_WIDTH_PARAM)

    /**
     * Builds file url for given [path]
     */
    override fun file(path: String): Url = appendToken(gateway.provide() + FILE_PATH + path)

    /**
     * Builds video url for given [path]
     */
    override fun video(path: String): Url = appendToken(gateway.provide() + FILE_PATH + path)

    companion object {
        const val IMAGE_PATH = "/image/"
        const val FILE_PATH = "/file/"
        const val LARGE_WIDTH_PARAM = "?width=1920"
        const val LARGE_WIDTH_ALT_PARAM = "?width=1820"
        const val MEDIUM_WIDTH_PARAM = "?width=320"
    }
}
