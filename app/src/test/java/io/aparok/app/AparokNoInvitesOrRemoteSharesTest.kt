package io.aparok.app

import io.aparok.app.other.DefaultDeepLinkResolver
import io.aparok.app.other.DefaultSpaceInviteResolver
import io.aparok.app.other.DEEP_LINK_INVITE_DOMAIN
import io.aparok.app.other.DEEP_LINK_ONE_TO_ONE_CHAT_DOMAIN
import io.aparok.app.other.DEEP_LINK_TO_OBJECT_BASE_URL
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

class AparokNoInvitesOrRemoteSharesTest {

    @Test
    fun `deep link domains are strictly local and contain no any coop or external hosts`() {
        assertFalse(
            "Invite domain must not reference any.coop",
            DEEP_LINK_INVITE_DOMAIN.contains("any.coop")
        )
        assertFalse(
            "One-to-one chat domain must not reference any.coop",
            DEEP_LINK_ONE_TO_ONE_CHAT_DOMAIN.contains("any.coop")
        )
        assertFalse(
            "Object base URL must not reference any.coop",
            DEEP_LINK_TO_OBJECT_BASE_URL.contains("any.coop")
        )
        assertTrue(
            "Object base URL must use aparok local scheme",
            DEEP_LINK_TO_OBJECT_BASE_URL.startsWith("aparok://")
        )
    }

    @Test
    fun `space invite link creation is completely disabled in sovereign offline build`() {
        val inviteLink = DefaultSpaceInviteResolver.createInviteLink("cid123", "key456")
        assertEquals(
            "Invite link creation must return empty string in offline mode",
            "",
            inviteLink
        )
    }

    @Test
    fun `remote any coop urls do not resolve to valid deep link actions`() {
        val remoteInvite = "https://invite.any.coop/cid123#key456"
        val action = DefaultDeepLinkResolver.resolve(remoteInvite)
        assertEquals(
            "Remote invite link from any.coop must resolve to Unknown",
            io.aparok.app.domain.misc.DeepLinkResolver.Action.Unknown,
            action
        )
    }
}
