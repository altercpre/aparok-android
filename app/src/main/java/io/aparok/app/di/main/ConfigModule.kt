package io.aparok.app.di.main

import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.data.auth.repo.config.GatewayProvider
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.config.CacheStorage
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.Gateway
import io.aparok.app.domain.config.TechSpaceProvider
import io.aparok.app.domain.auth.interactor.PreloadRemainingSpaces
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.launch.RemainingSpacesPreloader
import io.aparok.app.domain.misc.UrlBuilderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
object ConfigModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideGateway(
        configStorage: ConfigStorage,
        logger: Logger
    ): Gateway = GatewayProvider(
        configStorage = configStorage,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideUrlBuilder(
        getGateway: Gateway,
        configStorage: ConfigStorage
    ): UrlBuilder = UrlBuilderImpl(
        gateway = getGateway,
        tokenProvider = { configStorage.getOrNull()?.sessionToken.orEmpty() }
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideConfigProvider(): ConfigStorage = CacheStorage()

    @JvmStatic
    @Provides
    @Singleton
    fun provideTechSpaceProvider(
        configStorage: ConfigStorage
    ): TechSpaceProvider = configStorage

    @JvmStatic
    @Provides
    @Singleton
    fun dispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @JvmStatic
    @Provides
    @Singleton
    @Named(DEFAULT_APP_COROUTINE_SCOPE)
    fun applicationCoroutineScope() : CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    @JvmStatic
    @Provides
    @Singleton
    fun awaitAccountStartedManager(): AwaitAccountStartManager = AwaitAccountStartManager.Default

    @JvmStatic
    @Provides
    @Singleton
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder = PreferredSpaceIdHolder.Default

    @JvmStatic
    @Provides
    @Singleton
    fun remainingSpacesPreloader(
        preloadRemainingSpaces: PreloadRemainingSpaces
    ): RemainingSpacesPreloader = RemainingSpacesPreloader(preloadRemainingSpaces)

    const val DEFAULT_APP_COROUTINE_SCOPE = "Default application coroutine scope"
}