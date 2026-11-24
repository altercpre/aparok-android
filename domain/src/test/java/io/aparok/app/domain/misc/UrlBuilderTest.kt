package io.aparok.app.domain.misc

import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.config.Gateway
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UrlBuilderTest {

    @Mock
    lateinit var gateway: Gateway

    private lateinit var urlBuilder: UrlBuilder

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        urlBuilder = UrlBuilderImpl(gateway)
    }

    @Test
    fun `should return image url`() {
        val hash = "image001"

        val expected =
            gateway.provide() + UrlBuilderImpl.IMAGE_PATH + hash + UrlBuilderImpl.LARGE_WIDTH_ALT_PARAM
        val actual = urlBuilder.large(hash)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return url without hash when image hash is empty`() {
        val hash = ""

        val expected = gateway.provide() + UrlBuilderImpl.IMAGE_PATH + UrlBuilderImpl.LARGE_WIDTH_ALT_PARAM
        val actual = urlBuilder.large(hash)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return file url`() {
        val hash = "file001"

        val expected = gateway.provide() + UrlBuilderImpl.FILE_PATH + hash
        val actual = urlBuilder.file(hash)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return url without hash when file hash is empty`() {
        val hash = ""

        val expected = gateway.provide() + UrlBuilderImpl.FILE_PATH
        val actual = urlBuilder.file(hash)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return video url`() {
        val hash = "video001"

        val expected = gateway.provide() + UrlBuilderImpl.FILE_PATH + hash
        val actual = urlBuilder.video(hash)
        assertEquals(expected, actual)
    }

    @Test
    fun `should return url without hash when video hash is empty`() {
        val hash = ""

        val expected = gateway.provide() + UrlBuilderImpl.FILE_PATH
        val actual = urlBuilder.video(hash)
        assertEquals(expected, actual)
    }

    @Test
    fun `should append session token as query param to file and video urls`() {
        val token = "session_token_xyz"
        val tokenizedBuilder = UrlBuilderImpl(gateway, tokenProvider = { token })
        val hash = "video002"

        val expectedFile = gateway.provide() + UrlBuilderImpl.FILE_PATH + hash + "?token=$token"
        assertEquals(expectedFile, tokenizedBuilder.file(hash))
        assertEquals(expectedFile, tokenizedBuilder.video(hash))
    }

    @Test
    fun `should append session token with ampersand when url already has query params`() {
        val token = "session_token_xyz"
        val tokenizedBuilder = UrlBuilderImpl(gateway, tokenProvider = { token })
        val hash = "image002"

        val expectedLarge = gateway.provide() + UrlBuilderImpl.IMAGE_PATH + hash + UrlBuilderImpl.LARGE_WIDTH_ALT_PARAM + "&token=$token"
        assertEquals(expectedLarge, tokenizedBuilder.large(hash))

        val expectedMedium = gateway.provide() + UrlBuilderImpl.IMAGE_PATH + hash + UrlBuilderImpl.MEDIUM_WIDTH_PARAM + "&token=$token"
        assertEquals(expectedMedium, tokenizedBuilder.medium(hash))
    }
}
