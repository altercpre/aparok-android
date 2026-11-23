package io.aparok.app.device

import android.content.Context
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.storage.AparokStorageManager

class DefaultPathProvider(
    private val context: Context
) : PathProvider {
    override fun providePath(): String = AparokStorageManager.resolveStorageDir(context).path
    override fun cachePath(): String = context.cacheDir.path
}