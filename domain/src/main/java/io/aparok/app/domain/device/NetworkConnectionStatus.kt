package io.aparok.app.domain.device

import io.aparok.app.core_models.DeviceNetworkType

interface NetworkConnectionStatus {
    fun start()
    fun stop()
    fun getCurrentNetworkType(): DeviceNetworkType
}