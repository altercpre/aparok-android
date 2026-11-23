package io.aparok.app.data.auth.other

import io.aparok.app.core_models.Url
import io.aparok.app.domain.download.Downloader

class DataDownloader(private val device: Device) : Downloader {

    override fun download(url: Url, name: String) = device.download(url, name)
}