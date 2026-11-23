package com.anytypeio.anytype.middleware.discovery

import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import service.DiscoveryObserver

class MDNSResolver(
    private val nsdManager: NsdManager,
    wifiManager: WifiManager,
    private val discoveryListener: NsdDiscoveryListener,
    private val registrationListener: NsdRegistrationListener,
) {
    fun start(observer: DiscoveryObserver) {
        // No-op for offline-only operation
    }

    fun stop() {
        // No-op
    }
}
