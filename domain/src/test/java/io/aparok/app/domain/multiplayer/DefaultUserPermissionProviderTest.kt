package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.SpaceView
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DefaultUserPermissionProviderTest {

    private lateinit var spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
    private lateinit var container: StorelessSubscriptionContainer
    private lateinit var repo: AuthRepository
    private lateinit var dispatchers: AppCoroutineDispatchers
    private lateinit var logger: Logger
    private lateinit var provider: DefaultUserPermissionProvider

    @Before
    fun setup() {
        spaceViewSubscriptionContainer = mock(SpaceViewSubscriptionContainer::class.java)
        container = mock(StorelessSubscriptionContainer::class.java)
        repo = mock(AuthRepository::class.java)
        dispatchers = mock(AppCoroutineDispatchers::class.java)
        logger = mock(Logger::class.java)

        `when`(dispatchers.io).thenReturn(Dispatchers.Unconfined)
        `when`(dispatchers.main).thenReturn(Dispatchers.Unconfined)

        provider = DefaultUserPermissionProvider(
            spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
            container = container,
            repo = repo,
            dispatchers = dispatchers,
            scope = CoroutineScope(Dispatchers.Unconfined),
            logger = logger
        )
    }

    @Test
    fun `get always returns OWNER even with no member state or empty space`() {
        val perm = provider.get(SpaceId("any_space_id"))
        assertEquals(SpaceMemberPermissions.OWNER, perm)
    }

    @Test
    fun `observe always emits OWNER for any spaceId`() = runBlocking {
        val perm = provider.observe(SpaceId("space_123")).first()
        assertEquals(SpaceMemberPermissions.OWNER, perm)
    }

    @Test
    fun `all maps every space view target to OWNER`() = runBlocking {
        val sv1 = mock(SpaceView::class.java)
        `when`(sv1.targetSpaceId).thenReturn("space_a")
        val sv2 = mock(SpaceView::class.java)
        `when`(sv2.targetSpaceId).thenReturn("space_b")

        `when`(spaceViewSubscriptionContainer.observe()).thenReturn(flowOf(listOf(sv1, sv2)))

        val allPerms = provider.all().first()
        assertNotNull(allPerms)
        assertEquals(2, allPerms.size)
        assertEquals(SpaceMemberPermissions.OWNER, allPerms["space_a"])
        assertEquals(SpaceMemberPermissions.OWNER, allPerms["space_b"])
    }
}
