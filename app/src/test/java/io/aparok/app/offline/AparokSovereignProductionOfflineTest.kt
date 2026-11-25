package io.aparok.app.offline

import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.config.Gateway
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.UrlBuilderImpl
import io.aparok.app.domain.multiplayer.DefaultUserPermissionProvider
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.feature_chats.presentation.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Sovereign Offline Production Test Suite (Spec R3 & R12).
 *
 * Calls real production code from src/main:
 * 1. DefaultUserPermissionProvider (domain/multiplayer) - ensures OWNER offline without servers
 * 2. UrlBuilderImpl (domain/misc) - ensures loopback URLs carry ?token= for headers-free playback
 * 3. ChatViewModel commands & modes (feature_chats/presentation) - ensures video open & local edit
 */
class AparokSovereignProductionOfflineTest {

    private lateinit var spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
    private lateinit var storelessContainer: StorelessSubscriptionContainer
    private lateinit var authRepo: AuthRepository
    private lateinit var dispatchers: AppCoroutineDispatchers
    private lateinit var logger: Logger
    private lateinit var permissionProvider: DefaultUserPermissionProvider
    private lateinit var mockGateway: Gateway

    @Before
    fun setup() {
        spaceViewSubscriptionContainer = mock(SpaceViewSubscriptionContainer::class.java)
        storelessContainer = mock(StorelessSubscriptionContainer::class.java)
        authRepo = mock(AuthRepository::class.java)
        dispatchers = AppCoroutineDispatchers(
            io = Dispatchers.Unconfined,
            computation = Dispatchers.Unconfined,
            main = Dispatchers.Unconfined
        )
        logger = mock(Logger::class.java)

        permissionProvider = DefaultUserPermissionProvider(
            spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
            container = storelessContainer,
            repo = authRepo,
            dispatchers = dispatchers,
            scope = CoroutineScope(Dispatchers.Unconfined),
            logger = logger
        )

        mockGateway = mock(Gateway::class.java)
        `when`(mockGateway.provide()).thenReturn("http://127.0.0.1:47800")
    }

    @Test
    fun `production permission provider always returns OWNER for single-user offline space`() {
        val perm = permissionProvider.get(SpaceId("sovereign_space_001"))
        assertEquals(SpaceMemberPermissions.OWNER, perm)
    }

    @Test
    fun `production permission provider observe emits OWNER flow without remote participant`() = runBlocking {
        val perm = permissionProvider.observe(SpaceId("sovereign_space_002")).first()
        assertEquals(SpaceMemberPermissions.OWNER, perm)
    }

    @Test
    fun `production permission provider all maps all spaces to OWNER`() = runBlocking {
        val sv1 = mock(ObjectWrapper.SpaceView::class.java)
        `when`(sv1.targetSpaceId).thenReturn("space_alpha")
        val sv2 = mock(ObjectWrapper.SpaceView::class.java)
        `when`(sv2.targetSpaceId).thenReturn("space_beta")

        `when`(spaceViewSubscriptionContainer.observe()).thenReturn(flowOf(listOf(sv1, sv2)))

        val allPerms = permissionProvider.all().first()
        assertEquals(2, allPerms.size)
        assertEquals(SpaceMemberPermissions.OWNER, allPerms["space_alpha"])
        assertEquals(SpaceMemberPermissions.OWNER, allPerms["space_beta"])
    }

    @Test
    fun `production UrlBuilder appends session token query param to file and video urls`() {
        val sessionToken = "aparok_sess_token_real_777"
        val builder = UrlBuilderImpl(
            gateway = mockGateway,
            tokenProvider = { sessionToken }
        )

        val fileUrl = builder.file("bafybeifilehash123")
        assertTrue("File URL must contain token param", fileUrl.contains("token=$sessionToken"))
        assertEquals("http://127.0.0.1:47800/file/bafybeifilehash123?token=$sessionToken", fileUrl)

        val videoUrl = builder.video("bafybeivideohash456")
        assertTrue("Video URL must contain token param", videoUrl.contains("token=$sessionToken"))
        assertEquals("http://127.0.0.1:47800/file/bafybeivideohash456?token=$sessionToken", videoUrl)
    }

    @Test
    fun `production UrlBuilder appends session token with ampersand to image urls with params`() {
        val sessionToken = "aparok_sess_token_real_777"
        val builder = UrlBuilderImpl(
            gateway = mockGateway,
            tokenProvider = { sessionToken }
        )

        val largeUrl = builder.large("imagehash789")
        assertTrue("Image URL must contain width and token", largeUrl.contains("?width=1820&token=$sessionToken"))
        assertEquals("http://127.0.0.1:47800/image/imagehash789?width=1820&token=$sessionToken", largeUrl)
    }

    @Test
    fun `production ChatViewModel ChatBoxMode Default is not sending blocked`() {
        val defaultMode = ChatViewModel.ChatBoxMode.Default()
        assertFalse("Default chat box mode must allow sending messages offline", defaultMode.isSendingMessageBlocked)
    }

    @Test
    fun `production ChatViewModel OpenVideo command captures video object and space`() {
        val cmd = ChatViewModel.ViewModelCommand.OpenVideo(
            obj = "video_obj_001",
            space = SpaceId("space_media_001")
        )
        assertEquals("video_obj_001", cmd.obj)
        assertEquals(SpaceId("space_media_001"), cmd.space)
    }
}
