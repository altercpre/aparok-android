package io.aparok.app.di.main

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.aparok.app.device.network_type.NetworkConnectionStatusImpl
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.device.NetworkConnectionStatus
import io.aparok.app.domain.network.NetworkModeProvider
import io.aparok.app.persistence.networkmode.DefaultNetworkModeProvider
import io.aparok.app.persistence.networkmode.DefaultNetworkModeProvider.NetworkModeConstants.NAMED_NETWORK_MODE_PREFS
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module
object NetworkModeModule {

    @JvmStatic
    @Provides
    @Singleton
    @Named(NAMED_NETWORK_MODE_PREFS)
    fun provideNetworkModeSharedPreferences(
        context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provider(
        @Named(NAMED_NETWORK_MODE_PREFS) sharedPreferences: SharedPreferences
    ): NetworkModeProvider = DefaultNetworkModeProvider(sharedPreferences)

    @JvmStatic
    @Provides
    @Singleton
    fun provideNetworkConnectionStatus(
        context: Context,
        dispatcher: AppCoroutineDispatchers,
        blockRepository: BlockRepository,
        @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): NetworkConnectionStatus = NetworkConnectionStatusImpl(
        context = context,
        dispatchers = dispatcher,
        coroutineScope = scope,
        blockRepository = blockRepository
    )
}