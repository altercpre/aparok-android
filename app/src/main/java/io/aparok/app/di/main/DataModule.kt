package io.aparok.app.di.main

import android.content.Context
import android.content.SharedPreferences
import io.aparok.app.app.DefaultAppActionManager
import io.aparok.app.app.DefaultInitialParamsProvider
import io.aparok.app.core_utils.tools.ThreadInfo
import io.aparok.app.data.auth.repo.AuthCache
import io.aparok.app.data.auth.repo.AuthCacheDataStore
import io.aparok.app.data.auth.repo.AuthDataRepository
import io.aparok.app.data.auth.repo.AuthDataStoreFactory
import io.aparok.app.data.auth.repo.AuthRemote
import io.aparok.app.data.auth.repo.AuthRemoteDataStore
import io.aparok.app.data.auth.repo.DebugSettingsCache
import io.aparok.app.data.auth.repo.InfrastructureDataRepository
import io.aparok.app.data.auth.repo.UserSettingsCache
import io.aparok.app.data.auth.repo.UserSettingsDataRepository
import io.aparok.app.data.auth.repo.block.BlockDataRepository
import io.aparok.app.data.auth.repo.block.BlockRemote
import io.aparok.app.data.auth.repo.favorites.PersonalFavoritesDataRepository
import io.aparok.app.data.auth.repo.unsplash.UnsplashDataRepository
import io.aparok.app.data.auth.repo.unsplash.UnsplashRemote
import io.aparok.app.data.auth.types.DefaultObjectTypesProvider
import io.aparok.app.device.AppStateService
import io.aparok.app.presentation.device.BuildProvider
import io.aparok.app.presentation.device.DefaultBuildProvider
import io.aparok.app.device.DefaultPathProvider
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.favorites.PersonalFavoritesRepository
import io.aparok.app.domain.config.InfrastructureRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.DebugConfig
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.misc.AppActionManager
import io.aparok.app.domain.`object`.ObjectTypesProvider
import io.aparok.app.domain.objects.DefaultObjectStore
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.unsplash.UnsplashRepository
import io.aparok.app.domain.invite.SpaceInviteLinkStore
import io.aparok.app.domain.invite.SpaceInviteLinkStoreImpl
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.widgets.OsWidgetDataCleaner
import io.aparok.app.domain.widgets.OsWidgetDataViewSync
import io.aparok.app.domain.widgets.OsWidgetSpacesSync
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.feature_os_widgets.persistence.OsWidgetDataCleanerImpl
import io.aparok.app.feature_os_widgets.persistence.OsWidgetDataViewSyncImpl
import io.aparok.app.feature_os_widgets.persistence.OsWidgetSpacesSyncImpl
import io.aparok.app.middleware.EventProxy
import io.aparok.app.middleware.UnsplashMiddleware
import io.aparok.app.middleware.auth.AuthMiddleware
import io.aparok.app.middleware.block.BlockMiddleware
import io.aparok.app.middleware.interactor.Middleware
import io.aparok.app.middleware.interactor.MiddlewareFactory
import io.aparok.app.middleware.interactor.MiddlewareProtobufLogger
import io.aparok.app.middleware.interactor.ProtobufConverterProvider
import io.aparok.app.middleware.service.MiddlewareService
import io.aparok.app.middleware.service.MiddlewareServiceImplementation
import io.aparok.app.persistence.db.AnytypeDatabase
import io.aparok.app.device.providers.AppDefaultDateFormatProvider
import io.aparok.app.device.providers.AppDefaultDateFormatProviderImpl
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.device.SetAppState
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.domain.network.NetworkModeProvider
import io.aparok.app.persistence.repo.DefaultAuthCache
import io.aparok.app.persistence.repo.DefaultDebugSettingsCache
import io.aparok.app.persistence.repo.DefaultUserSettingsCache
import io.aparok.app.presentation.util.downloader.UriFileProvider
import io.aparok.app.providers.DefaultUriFileProvider
import io.aparok.app.security.KeystoreManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module(includes = [DataModule.Bindings::class])
object DataModule {

    @JvmStatic
    @Provides
    fun providePathProvider(context: Context): PathProvider {
        return DefaultPathProvider(context)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthRepository(
        factory: AuthDataStoreFactory,
        debugConfig: DebugConfig
    ): AuthRepository {
        return AuthDataRepository(
            factory = factory,
            debugConfig = debugConfig
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthDataStoreFactory(
        authCacheDataStore: AuthCacheDataStore,
        authRemoteDataStore: AuthRemoteDataStore
    ): AuthDataStoreFactory {
        return AuthDataStoreFactory(
            cache = authCacheDataStore,
            remote = authRemoteDataStore
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthCacheDataStore(
        authCache: AuthCache
    ): AuthCacheDataStore {
        return AuthCacheDataStore(
            cache = authCache
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthCache(
        db: AnytypeDatabase,
        @Named("default") defaultPrefs: SharedPreferences,
        @Named("encrypted") encryptedPrefs: SharedPreferences,
        networkModeProvider: NetworkModeProvider
    ): AuthCache {
        return DefaultAuthCache(
            db = db,
            defaultPrefs = defaultPrefs,
            encryptedPrefs = encryptedPrefs,
            networkModeProvider = networkModeProvider
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideDebugSettingsCache(
        @Named("default") prefs: SharedPreferences,
    ): DebugSettingsCache {
        return DefaultDebugSettingsCache(
            prefs = prefs
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideInfrastructureRepository(
        cache: DebugSettingsCache
    ): InfrastructureRepository {
        return InfrastructureDataRepository(
            cache = cache
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAnytypeDatabase(context: Context): AnytypeDatabase {
        return AnytypeDatabase.get(context)
    }

    @JvmStatic
    @Provides
    @Singleton
    @Named("default")
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @JvmStatic
    @Provides
    @Singleton
    @Named("encrypted")
    fun provideEncryptedSharedPreferences(
        keystoreManager: KeystoreManager
    ): SharedPreferences = keystoreManager.initializeEncryptedPreferences()

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthRemoteDataStore(
        authRemote: AuthRemote
    ): AuthRemoteDataStore {
        return AuthRemoteDataStore(
            authRemote = authRemote
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthRemote(
        middleware: Middleware,
        proxy: EventProxy
    ): AuthRemote {
        return AuthMiddleware(
            middleware = middleware,
            events = proxy
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideBlockRepository(
        blockRemote: BlockRemote,
        debugConfig: DebugConfig,
        logger: Logger
    ): BlockRepository {
        return BlockDataRepository(
            remote = blockRemote,
            debugConfig = debugConfig,
            logger = logger
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideBlockRemote(
        middleware: Middleware
    ): BlockRemote = BlockMiddleware(middleware = middleware)

    @JvmStatic
    @Provides
    @Singleton
    fun providePersonalFavoritesRepository(
        blocks: BlockRepository
    ): PersonalFavoritesRepository = PersonalFavoritesDataRepository(blockRepo = blocks)

    @JvmStatic
    @Provides
    @Singleton
    fun provideMiddleware(
        service: MiddlewareService,
        factory: MiddlewareFactory,
        logger: MiddlewareProtobufLogger,
        protobufConverter: ProtobufConverterProvider,
        threadInfo: ThreadInfo
    ): Middleware = Middleware(
        service = service,
        factory = factory,
        logger = logger,
        protobufConverter = protobufConverter,
        threadInfo = threadInfo
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideMiddlewareFactory(): MiddlewareFactory = MiddlewareFactory()

    @JvmStatic
    @Provides
    @Singleton
    fun provideObjectTypesProvider(): ObjectTypesProvider {
        return DefaultObjectTypesProvider()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideUserSettingsCache(
        @Named("default") prefs: SharedPreferences,
        context: Context,
        appDefaultDateFormatProvider: AppDefaultDateFormatProvider,
        osWidgetDataCleaner: OsWidgetDataCleaner
    ): UserSettingsCache = DefaultUserSettingsCache(
        prefs = prefs,
        context = context,
        appDefaultDateFormatProvider = appDefaultDateFormatProvider,
        osWidgetDataCleaner = osWidgetDataCleaner
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideOsWidgetDataCleaner(
        context: Context
    ): OsWidgetDataCleaner = OsWidgetDataCleanerImpl(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideUserSettingsRepo(
        cache: UserSettingsCache
    ): UserSettingsRepository = UserSettingsDataRepository(cache)

    @JvmStatic
    @Provides
    @Singleton
    fun provideOsWidgetSpacesSync(
        context: Context,
        urlBuilder: UrlBuilder
    ): OsWidgetSpacesSync = OsWidgetSpacesSyncWithUpdate(
        delegate = OsWidgetSpacesSyncImpl(context, urlBuilder),
        context = context
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideOsWidgetDataViewSync(
        context: Context,
        getObject: GetObject,
        searchObjects: SearchObjects,
        blockRepository: BlockRepository
    ): OsWidgetDataViewSync = OsWidgetDataViewSyncWithUpdate(
        delegate = OsWidgetDataViewSyncImpl(
            context = context,
            getObject = getObject,
            searchObjects = searchObjects,
            blockRepository = blockRepository
        ),
        context = context
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppActionManager(context: Context): AppActionManager = DefaultAppActionManager(
        context = context
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideObjectStore(): ObjectStore = DefaultObjectStore()

    //region Unsplash

    @JvmStatic
    @Provides
    @Singleton
    fun provideUnsplashRepo(
        remote: UnsplashRemote
    ): UnsplashRepository = UnsplashDataRepository(
        remote = remote
    )
    //endregion

    @JvmStatic
    @Provides
    @Singleton
    fun provideBuildProvider(): BuildProvider = DefaultBuildProvider()

    @JvmStatic
    @Provides
    @Singleton
    fun provideMetricsProvider(
        context: Context,
        encryptionManager: io.aparok.app.security.AparokEncryptionManager
    ): InitialParamsProvider = DefaultInitialParamsProvider(context, encryptionManager)

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileProvider(
        context: Context,
        logger: Logger
    ): UriFileProvider = DefaultUriFileProvider(context, logger)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppDefaultDateFormatProvider(
        localeProvider: LocaleProvider
    ): AppDefaultDateFormatProvider = AppDefaultDateFormatProviderImpl(
        localeProvider = localeProvider
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSetDeviceState(
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetAppState = SetAppState(blockRepository, dispatchers)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppStateService(
        setAppState: SetAppState,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        userSettingsRepository: UserSettingsRepository
    ): AppStateService = AppStateService(
        setAppState = setAppState,
        coroutineScope = scope,
        settings = userSettingsRepository
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSpaceInviteLinkStore(
    ): SpaceInviteLinkStore = SpaceInviteLinkStoreImpl()

    @Provides
    @Singleton
    fun provideKeystoreManager(context: Context): KeystoreManager = KeystoreManager(context)

    @Provides
    @Singleton
    fun provideAparokEncryptionManager(keystoreManager: KeystoreManager): io.aparok.app.security.AparokEncryptionManager =
        io.aparok.app.security.AparokEncryptionManager(keystoreManager)

    @Provides
    @Singleton
    fun provideAparokStorageManager(context: Context): io.aparok.app.storage.AparokStorageManager =
        io.aparok.app.storage.AparokStorageManager(context)

    @Provides
    @Singleton
    fun provideAparokBackupManager(): io.aparok.app.backup.AparokBackupManager =
        io.aparok.app.backup.AparokBackupManager()

    @Module
    interface Bindings {

        @Binds
        @Singleton
        fun bindUnsplashRemote(middleware: UnsplashMiddleware): UnsplashRemote

        @Binds
        @Singleton
        fun bindMiddlewareService(middleware: MiddlewareServiceImplementation): MiddlewareService
    }
}

const val ENCRYPTED_PREFS_NAME = "encrypted_prefs"
