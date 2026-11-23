package io.aparok.app.device

import io.aparok.app.domain.device.DeviceTokenStoringService
import javax.inject.Inject

class DeviceTokenStoringServiceImpl @Inject constructor() : DeviceTokenStoringService {

    override fun saveToken(token: String) {
        // No-op: push notification tokens removed for offline privacy
    }

    override fun start() {
        // No-op
    }

    override fun stop() {
        // No-op
    }

    override suspend fun getToken(): String? = null
}
