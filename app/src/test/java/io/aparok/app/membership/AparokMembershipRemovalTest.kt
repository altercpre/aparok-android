package io.aparok.app.membership

import io.aparok.app.core_models.ext.isPossibleToUpgrade
import io.aparok.app.core_models.ext.isPossibleToUpgradeNumberOfSpaceMembers
import io.aparok.app.core_models.ext.isPossibleToUpgradeStorageSpace
import io.aparok.app.core_models.membership.MembershipFeatures
import io.aparok.app.core_models.membership.MembershipUpgradeReason
import io.aparok.app.core_models.membership.TierId
import io.aparok.app.presentation.membership.provider.MembershipProvider
import io.aparok.app.presentation.multiplayer.SpaceLimitsState
import io.aparok.app.presentation.multiplayer.spaceLimitsState
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Phase R4 Verification: Real Membership Removal.
 *
 * Verifies:
 * 1. MembershipFeatures default to UNLIMITED (Int.MAX_VALUE)
 * 2. All upgrade checks return false (no upgrade flows exist)
 * 3. Space limits state always resolves to Init (no upgrade/paywall banners)
 * 4. MembershipProvider is a clean no-op empty flow (no spoofed tiers)
 */
class AparokMembershipRemovalTest {

    @Test
    fun testMembershipFeaturesDefaultsAreUnlimited() {
        val features = MembershipFeatures()

        assertEquals("spaceWriters must be unlimited", Int.MAX_VALUE, features.spaceWriters)
        assertEquals("spaceReaders must be unlimited", Int.MAX_VALUE, features.spaceReaders)
        assertEquals("sharedSpaces must be unlimited", Int.MAX_VALUE, features.sharedSpaces)
    }

    @Test
    fun testNoUpgradesPossible() {
        val tier = TierId(1)

        assertFalse(tier.isPossibleToUpgrade(MembershipUpgradeReason.NumberOfEditors))
        assertFalse(tier.isPossibleToUpgrade(MembershipUpgradeReason.NumberOfReaders))
        assertFalse(tier.isPossibleToUpgrade(MembershipUpgradeReason.NumberOfSharedSpaces))
        assertFalse(tier.isPossibleToUpgrade(MembershipUpgradeReason.StorageSpace))
        assertFalse(tier.isPossibleToUpgradeNumberOfSpaceMembers())
        assertFalse(tier.isPossibleToUpgradeStorageSpace())
    }

    @Test
    fun testMembershipProviderProducesNoTiersOrSpoofing() = runBlocking {
        val provider = MembershipProvider.Default()

        val activeTier = provider.activeTier().firstOrNull()
        val status = provider.status().firstOrNull()

        assertNull("activeTier must be emptyFlow (no spoofed tier)", activeTier)
        assertNull("status must be emptyFlow", status)
    }
}
