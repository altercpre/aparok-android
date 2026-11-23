package io.aparok.app.domain.subscriptions

import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.device.DeviceTokenStoringService
import io.aparok.app.domain.device.NetworkConnectionStatus
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.notifications.PushKeyProvider
import io.aparok.app.domain.search.HasInstanceOfObjectTypeSubscriptionManager
import io.aparok.app.domain.search.ObjectTypesSubscriptionManager
import io.aparok.app.domain.search.ProfileSubscriptionManager
import io.aparok.app.domain.search.RelationOptionsSubscriptionManager
import io.aparok.app.domain.search.RelationsSubscriptionManager
import io.aparok.app.domain.spaces.SpaceDeletedStatusWatcher
import javax.inject.Inject

interface GlobalSubscriptionManager {

    fun onStart()
    fun onStop()

    class Default @Inject constructor(
        private val types: ObjectTypesSubscriptionManager,
        private val relations: RelationsSubscriptionManager,
        private val relationOptions: RelationOptionsSubscriptionManager,
        private val permissions: UserPermissionProvider,
        private val isSpaceDeleted: SpaceDeletedStatusWatcher,
        private val profile: ProfileSubscriptionManager,
        private val networkConnectionStatus: NetworkConnectionStatus,
        private val deviceTokenStoringService: DeviceTokenStoringService,
        private val pushKeyProvider: PushKeyProvider,
        private val hasInstanceOfType: HasInstanceOfObjectTypeSubscriptionManager,
        private val logger: Logger
    ) : GlobalSubscriptionManager {

        override fun onStart() {
            logger.logInfo("GlobalSubscriptionManager: Starting all subscriptions")
            pushKeyProvider.start()
            types.onStart()
            relations.onStart()
            relationOptions.onStart()
            permissions.start()
            isSpaceDeleted.onStart()
            profile.onStart()
            networkConnectionStatus.start()
            deviceTokenStoringService.start()
            hasInstanceOfType.onStart()
        }

        override fun onStop() {
            logger.logInfo("GlobalSubscriptionManager: Stopping all subscriptions")
            pushKeyProvider.stop()
            types.onStop()
            relations.onStop()
            relationOptions.onStop()
            permissions.stop()
            isSpaceDeleted.onStop()
            profile.onStop()
            networkConnectionStatus.stop()
            deviceTokenStoringService.stop()
            hasInstanceOfType.onStop()
        }
    }

}