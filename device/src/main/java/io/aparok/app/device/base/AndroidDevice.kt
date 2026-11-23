package io.aparok.app.device.base

import io.aparok.app.data.auth.other.Device
import io.aparok.app.device.download.AndroidDeviceDownloader

class AndroidDevice(
    private val downloader: AndroidDeviceDownloader
) : Device {
    override fun download(url: String, name: String) = downloader.download(url = url, name = name)
}