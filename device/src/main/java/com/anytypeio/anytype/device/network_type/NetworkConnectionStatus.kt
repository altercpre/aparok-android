package com.anytypeio.anytype.device.network_type

import android.content.Context
import com.anytypeio.anytype.core_models.DeviceNetworkType
import com.anytypeio.anytype.domain.base.AppCoroutineDispatchers
import com.anytypeio.anytype.domain.block.repo.BlockRepository
import com.anytypeio.anytype.domain.device.NetworkConnectionStatus
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
