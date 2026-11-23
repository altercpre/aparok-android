package io.aparok.app.data.auth.repo.config

import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.Gateway
import io.aparok.app.domain.debugging.Logger

class GatewayProvider(
    private val configStorage: ConfigStorage,
    private val logger: Logger
) : Gateway {
    override fun provide(): String {
        val gateway = configStorage.getOrNull()?.gateway
        if (gateway == null) {
            logger.logException(IllegalStateException("Gateway not found"))
        }
        return gateway.orEmpty()
    }

}