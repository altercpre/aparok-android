package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.CoroutineTestRule
import io.aparok.app.core_models.multiplayer.InviteType
import io.aparok.app.core_models.multiplayer.SpaceInviteError
import io.aparok.app.core_models.multiplayer.SpaceInviteLink
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.util.dispatchers
import io.aparok.app.test_utils.MockDataFactory
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

class GetSpaceInviteLinkTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var rule = CoroutineTestRule()

    @Mock
    lateinit var repo: BlockRepository

    private lateinit var usecase: GetSpaceInviteLink

    private val space = SpaceId(MockDataFactory.randomUuid())

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        usecase = GetSpaceInviteLink(repo = repo, dispatchers = dispatchers)
    }

    @Test
    fun `returns invite when link is visible`() = runBlocking {
        val invite = SpaceInviteLink(
            contentId = MockDataFactory.randomUuid(),
            fileKey = MockDataFactory.randomUuid(),
            inviteType = InviteType.MEMBER,
            permissions = SpaceMemberPermissions.READER
        )
        repo.stub {
            onBlocking { getSpaceInviteLink(space) } doReturn invite
        }

        val result = usecase.execute(space).getOrNull()

        assertEquals(invite, result)
    }

    @Test
    fun `fails with InviteNotActive when invite is held by owner and cid is empty`() = runBlocking {
        // A member's device receives a successful response with empty cid + key —
        // there is no link to hand out, so consumers must treat it as no invite.
        repo.stub {
            onBlocking { getSpaceInviteLink(space) } doReturn SpaceInviteLink(
                contentId = "",
                fileKey = "",
                inviteType = InviteType.MEMBER,
                permissions = SpaceMemberPermissions.READER,
                heldByOwner = true
            )
        }

        val result = usecase.execute(space)

        assertNull(result.getOrNull())
        assertTrue(result.exceptionOrNull() is SpaceInviteError.InviteNotActive)
    }

    @Test
    fun `fails with InviteNotActive when cid is empty even if not held by owner`() = runBlocking {
        repo.stub {
            onBlocking { getSpaceInviteLink(space) } doReturn SpaceInviteLink(
                contentId = "",
                fileKey = "",
                inviteType = InviteType.MEMBER,
                permissions = SpaceMemberPermissions.READER,
                heldByOwner = false
            )
        }

        val result = usecase.execute(space)

        assertNull(result.getOrNull())
        assertTrue(result.exceptionOrNull() is SpaceInviteError.InviteNotActive)
    }
}
