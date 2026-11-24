package io.aparok.app.presentation.multiplayer

import io.aparok.app.core_models.ObjectWrapper

/**
 * In sovereign offline Aparok, member limits and upgrade incentives are disabled.
 * All spaces operate with unlimited sovereign boundaries.
 */
fun ObjectWrapper.SpaceView.spaceLimitsState(
    isCurrentUserOwner: Boolean,
    spaceMembers: List<ObjectWrapper.SpaceMember>,
    sharedSpaceCount: Int,
    sharedSpaceLimit: Int,
): SpaceLimitsState = SpaceLimitsState.Init
