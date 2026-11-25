package io.aparok.app.middleware.discovery

import javax.inject.Inject
import io.aparok.mw.service.Service.setDiscoveryProxy

class MDNSProvider @Inject constructor(
    private val delegate: MDNSDelegate
) {

    fun setup() {
        setDiscoveryProxy(delegate)
    }

    fun start() {
        delegate.start()
    }

    fun stop() {
        delegate.stop()
    }

}

