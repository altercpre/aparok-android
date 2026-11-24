package io.aparok.app.core_models.ext

import io.aparok.app.core_models.membership.MembershipUpgradeReason
import io.aparok.app.core_models.membership.TierId

fun TierId.isPossibleToUpgrade(
    reason: MembershipUpgradeReason
): Boolean = false

fun TierId.isPossibleToUpgradeNumberOfSpaceMembers(): Boolean = false

fun TierId.isPossibleToUpgradeStorageSpace(): Boolean = false
