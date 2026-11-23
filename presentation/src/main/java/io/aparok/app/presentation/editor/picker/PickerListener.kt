package io.aparok.app.presentation.editor.picker

import android.net.Uri
import io.aparok.app.core_models.Id

interface PickerListener {

    fun onStartCopyFileToCacheDir(uri: Uri)

    fun onCancelCopyFileToCacheDir()

    fun onPickedDocImageFromDevice(ctx: Id, path: String)

    fun onProceedWithFilePath(filePath: String?)
}