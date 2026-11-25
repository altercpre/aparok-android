package io.aparok.app.app

import android.content.Context
import io.aparok.app.BuildConfig
import io.aparok.app.core_models.Command
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.security.AparokEncryptionManager
import io.aparok.app.storage.AparokStorageManager
import timber.log.Timber

class DefaultInitialParamsProvider(
    context: Context,
    private val encryptionManager: AparokEncryptionManager? = null
) : InitialParamsProvider {

    init {
        // Wire Keystore-wrapped key for core database & file encryption at initialization (Spec R7)
        try {
            encryptionManager?.let { mgr ->
                val storageKey = mgr.getOrCreateFileEncryptionKey()
                try {
                    io.aparok.mw.service.Service.setStorageKey(storageKey)
                    Timber.i("Supplied Keystore-wrapped storage key to sovereign core")
                } catch (t: Throwable) {
                    Timber.w("Core setStorageKey not yet available in current runtime: ${t.message}")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error initializing sovereign encryption key")
        }
    }

    override val workDir = AparokStorageManager.resolveStorageDir(context).absolutePath
    override val defaultLogLevel =
        "common.commonspace.headsync=INFO;core.block.editor.spaceview=INFO;*=WARN"

    override fun getVersion(): String {
        return if (BuildConfig.DEBUG)
            BuildConfig.VERSION_NAME + DEV_PREFIX
        else {
            return BuildConfig.VERSION_NAME
        }
    }

    override fun getPlatform(): String = PLATFORM_NAME

    companion object {
        const val PLATFORM_NAME = "android"
        const val DEV_PREFIX = "-dev"
    }

    override fun toCommand() = Command.SetInitialParams(
        version = getVersion(),
        platform = getPlatform(),
        workDir = workDir,
        defaultLogLevel = defaultLogLevel
    )
}