package io.aparok.app.device.network_type

import android.content.Context
import io.aparok.app.core_models.DeviceNetworkType
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.device.NetworkConnectionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class NetworkConnectionStatusImpl(
    context: Context,
    private val coroutineScope: CoroutineScope,
    private val blockRepository: BlockRepository,
    private val dispatchers: AppCoroutineDispatchers
) : NetworkConnectionStatus {

    override fun start() {
        coroutineScope.launch(dispatchers.io) {
            try {
                blockRepository.setDeviceNetworkState(DeviceNetworkType.NOT_CONNECTED, "")
            } catch (e: Throwable) {
                Timber.w(e, "Failed to set device network state")
            }
        }
    }

    override fun stop() {
        // No-op for offline-only mode
    }

    override fun getCurrentNetworkType(): DeviceNetworkType = DeviceNetworkType.NOT_CONNECTED
}
