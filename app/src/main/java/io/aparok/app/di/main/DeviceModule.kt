package io.aparok.app.di.main

import android.content.Context
import io.aparok.app.data.auth.other.DataDownloader
import io.aparok.app.data.auth.other.Device
import io.aparok.app.device.providers.DefaultLocalProvider
import io.aparok.app.device.SharedFileUploader
import io.aparok.app.device.base.AndroidDevice
import io.aparok.app.device.download.AndroidDeviceDownloader
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.device.FileSharer
import io.aparok.app.domain.download.Downloader
import io.aparok.app.domain.misc.LocaleProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DeviceModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideDownloader(
        device: Device
    ): Downloader = DataDownloader(device = device)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDevice(
        downloader: AndroidDeviceDownloader
    ): Device = AndroidDevice(downloader = downloader)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDeviceDownloader(
        context: Context
    ): AndroidDeviceDownloader = AndroidDeviceDownloader(context = context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideLocaleProvider(context: Context): LocaleProvider = DefaultLocalProvider(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileSharer(
        context: Context,
        dispatchers: AppCoroutineDispatchers
    ): FileSharer = SharedFileUploader(
        context = context,
        dispatchers = dispatchers
    )
}