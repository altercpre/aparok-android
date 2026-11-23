package io.aparok.app.other

import io.aparok.app.BuildConfig
import io.aparok.app.domain.debugging.DebugConfig
import javax.inject.Inject

class DefaultDebugConfig @Inject constructor(): DebugConfig {
    override val traceSubscriptions: Boolean get() = BuildConfig.DEBUG
    override val setTimeouts: Boolean get() = BuildConfig.DEBUG
}