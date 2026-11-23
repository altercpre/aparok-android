package com.anytypeio.anytype.persistence.networkmode

import android.content.SharedPreferences
import com.anytypeio.anytype.core_models.NetworkMode
import com.anytypeio.anytype.core_models.NetworkModeConfig
import com.anytypeio.anytype.domain.network.NetworkModeProvider

class DefaultNetworkModeProvider(private val sharedPreferences: SharedPreferences) :
    NetworkModeProvider {

    override fun set(networkModeConfig: NetworkModeConfig) {
        // No-op: Aparok is local-only sovereign offline.
    }

    override fun get(): NetworkModeConfig {
        return NetworkModeConfig(
            networkMode = NetworkMode.LOCAL,
            userFilePath = null,
            storedFilePath = null,
            useReserveMultiplexLib = false
        )
    }

    override fun clear() {
        // No-op
    }

    object NetworkModeConstants {
        const val NETWORK_MODE_PREF = "pref.network_mode"
        const val NETWORK_MODE_APP_FILE_PATH_PREF = "pref.network_config_file_path"
        const val NETWORK_MODE_USER_FILE_PATH_PREF = "pref.network_mode_user_config_file_path"
        const val USE_RESERVE_MULTIPLEX_LIBRARY_PREF = "pref.use_reserve_multiplex_library_1"

        const val NAMED_NETWORK_MODE_PREFS = "network_mode"
    }
}
