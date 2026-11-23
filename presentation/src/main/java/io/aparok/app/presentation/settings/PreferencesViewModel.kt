package io.aparok.app.presentation.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.analytics.base.EventsDictionary
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.NetworkMode
import io.aparok.app.core_models.NetworkModeConfig
import io.aparok.app.core_models.NetworkModeConstants.NETWORK_MODE_CUSTOM
import io.aparok.app.core_models.NetworkModeConstants.NETWORK_MODE_LOCAL
import io.aparok.app.core_utils.ext.cancel
import io.aparok.app.domain.base.fold
import io.aparok.app.domain.debugging.DebugExportLogs
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.networkmode.GetNetworkMode
import io.aparok.app.domain.networkmode.SetNetworkMode
import io.aparok.app.presentation.editor.picker.PickerListener
import io.aparok.app.presentation.extension.sendAnalyticsSelectNetworkEvent
import io.aparok.app.presentation.extension.sendAnalyticsUploadConfigFileEvent
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.util.CopyFileToCacheStatus
import io.aparok.app.presentation.util.downloader.UriFileProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PreferencesViewModel(
    private val copyFileToCache: CopyFileToCacheDirectory,
    private val getNetworkMode: GetNetworkMode,
    private val setNetworkMode: SetNetworkMode,
    private val analytics: Analytics,
    private val debugExportLogs: DebugExportLogs,
    private val pathProvider: PathProvider,
    private val uriFileProvider: UriFileProvider,
) : ViewModel(), PickerListener {

    val networkModeState = MutableStateFlow(NetworkModeConfig(NetworkMode.DEFAULT, "", ""))
    val reserveMultiplexSetting = MutableStateFlow(false)

    private val jobs = mutableListOf<Job>()
    val commands = MutableSharedFlow<Command>()

    fun onStart() {
        Timber.d("onStart")
        viewModelScope.launch {
            getNetworkMode.async(Unit).fold(
                onSuccess = { config ->
                    networkModeState.value = config
                    reserveMultiplexSetting.value = config.useReserveMultiplexLib
                    Timber.d("Successfully get network mode on Start: $config")
                },
                onFailure = {
                    Timber.e(it, "Failed to get network mode")
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobs.cancel()
    }

    fun proceedWithNetworkMode(mode: String?) {
        viewModelScope.launch {
            val config = when (mode) {
                NETWORK_MODE_LOCAL -> {
                    sendAnalyticsSelectNetworkEvent(
                        analytics = analytics,
                        type = EventsDictionary.Type.localOnly,
                        route = EventsDictionary.Routes.settings
                    )
                    NetworkModeConfig(NetworkMode.LOCAL)
                }
                NETWORK_MODE_CUSTOM -> {
                    sendAnalyticsSelectNetworkEvent(
                        analytics = analytics,
                        type = EventsDictionary.Type.selfHost,
                        route = EventsDictionary.Routes.settings
                    )
                    NetworkModeConfig(NetworkMode.CUSTOM)
                }
                else -> {
                    sendAnalyticsSelectNetworkEvent(
                        analytics = analytics,
                        type = EventsDictionary.Type.anytype,
                        route = EventsDictionary.Routes.settings
                    )
                    NetworkModeConfig()
                }
            }
            networkModeState.value = config
            setNetworkMode.async(SetNetworkMode.Params(config)).fold(
                onSuccess = { Timber.d("Successfully update network mode with config:$config") },
                onFailure = { Timber.e(it, "Failed to set network mode") }
            )
        }
    }

    fun proceedWithConfigFiles(userFilePath: String?, storedFilePath: String?) {
        viewModelScope.launch {
            val config = NetworkModeConfig(
                networkMode = NetworkMode.CUSTOM,
                userFilePath = userFilePath,
                storedFilePath = storedFilePath
            )
            val params = SetNetworkMode.Params(config)
            setNetworkMode.async(params).fold(
                onSuccess = {
                    networkModeState.value = config
                    sendAnalyticsUploadConfigFileEvent(analytics)
                    Timber.d("Successfully update network mode with config:$config")
                },
                onFailure = { Timber.e(it, "Failed to set network mode") }
            )
        }
    }

    fun onExportLogsClick() {
        Timber.d("onExportLogsClick: ")
        jobs += viewModelScope.launch {
            val dir = pathProvider.providePath()
            val params = DebugExportLogs.Params(dir = dir)
            debugExportLogs.async(params).fold(
                onSuccess = { fileName ->
                    Timber.d("On debug logs success")
                    sendCommand(Command.ShareDebugLogs(fileName, uriFileProvider))
                },
                onFailure = {
                    Timber.e(it, "Error while collecting debug logs")
                    sendCommand(Command.ShowToast("Error while collecting debug logs: "))
                }
            )
        }
    }

    private fun sendCommand(command: Command) {
        viewModelScope.launch {
            commands.emit(command)
        }
    }

    override fun onStartCopyFileToCacheDir(uri: Uri) {
        Timber.d("onStartCopyFileToCacheDir: $uri")
        copyFileToCache.execute(
            uri = uri,
            scope = viewModelScope,
            listener = copyFileListener
        )
    }

    override fun onCancelCopyFileToCacheDir() {
        copyFileToCache.cancel()
    }

    fun onChangeQuickLibraryState() {
        val newValue = !networkModeState.value.useReserveMultiplexLib
        Timber.d("onChangeQuickLibraryState: $newValue")
        viewModelScope.launch {
            val mode = networkModeState.value.copy(useReserveMultiplexLib = newValue)
            val params = SetNetworkMode.Params(mode)
            setNetworkMode.async(params).fold(
                onSuccess = { networkModeState.value = mode },
                onFailure = { Timber.e(it, "Failed to update network mode ") }
            )
        }
    }

    override fun onPickedDocImageFromDevice(ctx: Id, path: String) {}
    override fun onProceedWithFilePath(filePath: String?) {}

    private val copyFileListener = object : CopyFileToCacheStatus {
        override fun onCopyFileStart() {}
        override fun onCopyFileError(msg: String) {}

        override fun onCopyFileResult(result: String?, fileName: String?) {
            proceedWithConfigFiles(
                userFilePath = fileName,
                storedFilePath = result
            )
        }
    }

    sealed class Command {
        data class ShowToast(val message: String) : Command()
        data class ShareDebugLogs(val path: String, val uriFileProvider: UriFileProvider) : Command()
    }

    class Factory(
        private val copyFileToCacheDirectory: CopyFileToCacheDirectory,
        private val getNetworkMode: GetNetworkMode,
        private val setNetworkMode: SetNetworkMode,
        private val analytics: Analytics,
        private val debugExportLogs: DebugExportLogs,
        private val pathProvider: PathProvider,
        private val uriFileProvider: UriFileProvider,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T = PreferencesViewModel(
            copyFileToCache = copyFileToCacheDirectory,
            getNetworkMode = getNetworkMode,
            setNetworkMode = setNetworkMode,
            analytics = analytics,
            debugExportLogs = debugExportLogs,
            pathProvider = pathProvider,
            uriFileProvider = uriFileProvider
        ) as T
    }
}