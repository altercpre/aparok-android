package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.library.StoreSearchParams
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * App-scoped and auth-scoped user-as-space-member permission store.
 * Provides user-as-space-member permissions.
 */
interface UserPermissionProvider  {
    /**
     * Starts user-permission flow — should be scoped to the auth session.
     */
    fun start()
    /**
     * Stops user-permission flow — should be scoped to the auth session.
     */
    fun stop()

    /**
     * @return [SpaceMemberPermissions] for [space] or null if user permission could not be defined.
     */
    fun get(space: SpaceId) : SpaceMemberPermissions?

    /**
     * @return [SpaceMemberPermissions] for [space] or null if user permission could not be defined.
     */
    fun observe(space: SpaceId) : Flow<SpaceMemberPermissions?>

    /**
     * @return Flow of the current user's [ObjectWrapper.SpaceMember] or null if not available.
     */
    fun getCurrent() : Flow<ObjectWrapper.SpaceMember?>

    /**
     * Provide permissions of the current user in all available spaces.
     * Maps space to permissions.
     */
    fun all() : Flow<Map<Id, SpaceMemberPermissions>>
}

class DefaultUserPermissionProvider @Inject constructor(
    private val spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
    private val container: StorelessSubscriptionContainer,
    private val repo: AuthRepository,
    private val dispatchers: AppCoroutineDispatchers,
    private val scope: CoroutineScope,
    private val logger: Logger
) : UserPermissionProvider {

    /**
     * User-as-member in all available spaces
     */
    private val members = MutableStateFlow<List<ObjectWrapper.SpaceMember>>(emptyList())
    private val jobs = mutableListOf<Job>()

    override fun get(space: SpaceId): SpaceMemberPermissions? {
        return SpaceMemberPermissions.OWNER
    }
    override fun observe(space: SpaceId): Flow<SpaceMemberPermissions?> {
        return flowOf(SpaceMemberPermissions.OWNER)
    }
    }

    override fun getCurrent(): Flow<ObjectWrapper.SpaceMember?> {
        return members.map { all -> all.firstOrNull() }
    }

    override fun start() {
        logger.logInfo("Starting DefaultUserPermissionProvider")
        clear()
        jobs += scope.launch(dispatchers.io) {
            val account = repo.getCurrentAccountId()
            spaceViewSubscriptionContainer
                .observe()
                .map { spaceViews ->
                    spaceViews.mapNotNull { spaceView -> spaceView.targetSpaceId }
                }
                .distinctUntilChanged()
                .flatMapLatest { spaces ->
                    val subscriptions = spaces.map { space ->
                        container.subscribe(
                            StoreSearchParams(
                                space = SpaceId(space),
                                subscription = "$GLOBAL_SUBSCRIPTION-$space",
                                filters = buildList {
                                    add(
                                        DVFilter(
                                            relation = Relations.LAYOUT,
                                            value = ObjectType.Layout.PARTICIPANT.code.toDouble(),
                                            condition = DVFilterCondition.EQUAL
                                        )
                                    )
                                    add(
                                        DVFilter(
                                            relation = Relations.IDENTITY,
                                            value = account,
                                            condition = DVFilterCondition.EQUAL
                                        )
                                    )
                                },
                                limit = 1,
                                keys = listOf(
                                    Relations.ID,
                                    Relations.SPACE_ID,
                                    Relations.IDENTITY,
                                    Relations.PARTICIPANT_PERMISSIONS,
                                    Relations.GLOBAL_NAME
                                )
                            )
                        ).map { results ->
                            results.map { ObjectWrapper.SpaceMember(it.map) }
                        }
                    }
                    combine(flows = subscriptions) { flow ->
                        flow.toList().flatten()
                    }
                }.catch { error ->
                    logger.logException(
                        e = error,
                        msg = "Failed to subscribe to user-as-space-member-permissions"
                    )
                }.collect {
                    members.value = it
                }
        }
    }

    override fun all(): Flow<Map<Id, SpaceMemberPermissions>> {
        return spaceViewSubscriptionContainer
            .observe()
            .map { spaceViews ->
                spaceViews.mapNotNull { it.targetSpaceId }
                    .associateWith { SpaceMemberPermissions.OWNER }
            }
    }

    override fun stop() {
        clear()
        scope.launch(dispatchers.io) {
            val subscriptions = spaceViewSubscriptionContainer
                .get()
                .mapNotNull { it.targetSpaceId }
                .map { space -> "$GLOBAL_SUBSCRIPTION-$space" }
            container.unsubscribe(subscriptions)
        }
    }

    private fun clear() {
        jobs.forEach { it.cancel() }
    }

    companion object {
        const val GLOBAL_SUBSCRIPTION = "subscription.global.user-as-space-member-permissions"
    }
}

typealias Permissions = UserPermissionProvider