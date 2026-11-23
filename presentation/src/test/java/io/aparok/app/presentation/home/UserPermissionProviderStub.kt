package io.aparok.app.presentation.home

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserPermissionProviderStub : UserPermissionProvider {
    private var permission: SpaceMemberPermissions? = null
    private var spaceId: SpaceId? = null

    override fun start() {}

    override fun stop() {}

    override fun get(space: SpaceId): SpaceMemberPermissions? {
        return permission
    }

    override fun observe(space: SpaceId): Flow<SpaceMemberPermissions?> = flowOf(permission)
    fun stubObserve(spaceId: SpaceId, permission: SpaceMemberPermissions) {
        this.spaceId = spaceId
        this.permission = permission
    }

    override fun all(): Flow<Map<Id, SpaceMemberPermissions>> {
        return flowOf(emptyMap())
    }

    override fun getCurrent(): Flow<ObjectWrapper.SpaceMember?> {
        return flowOf(null)
    }
}
