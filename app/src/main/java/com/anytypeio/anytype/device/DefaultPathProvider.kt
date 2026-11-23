package com.anytypeio.anytype.device

import android.content.Context
import com.anytypeio.anytype.domain.device.PathProvider
import com.anytypeio.anytype.storage.AparokStorageManager

class DefaultPathProvider(
    private val context: Context
) : PathProvider {
    override fun providePath(): String = AparokStorageManager.resolveStorageDir(context).path
    override fun cachePath(): String = context.cacheDir.path
}