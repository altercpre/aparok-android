package io.aparok.app.di.main

import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.device.DeviceTokenStoringServiceImpl
import io.aparok.app.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.interactor.sets.GetObjectTypes
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.chats.ChatEventChannel
import io.aparok.app.domain.chats.ChatPreviewContainer
import io.aparok.app.domain.chats.ChatsDetailsSubscriptionContainer
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.debugging.DebugAccountSelectTrace
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.deeplink.PendingIntentStore
import io.aparok.app.domain.device.DeviceTokenStoringService
import io.aparok.app.domain.device.NetworkConnectionStatus
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.library.CrossSpaceSubscriptionContainer
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import io.aparok.app.domain.multiplayer.DefaultUserPermissionProvider
import io.aparok.app.domain.multiplayer.ParticipantSubscriptionContainer
import io.aparok.app.domain.objects.CrossSpaceObjectTypesContainer
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.notifications.PushKeyProvider
import io.aparok.app.domain.objects.DefaultStoreOfObjectTypes
import io.aparok.app.domain.objects.DefaultStoreOfRelationOptions
import io.aparok.app.domain.objects.DefaultStoreOfRelations
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelationOptions
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.search.HasInstanceOfObjectTypeSubscriptionContainer
import io.aparok.app.domain.search.HasInstanceOfObjectTypeSubscriptionManager
import io.aparok.app.domain.search.ObjectTypesSubscriptionContainer
import io.aparok.app.domain.search.ObjectTypesSubscriptionManager
import io.aparok.app.domain.search.ProfileSubscriptionManager
import io.aparok.app.domain.search.RelationOptionsSubscriptionContainer
import io.aparok.app.domain.search.RelationOptionsSubscriptionManager
import io.aparok.app.domain.search.RelationsSubscriptionContainer
import io.aparok.app.domain.search.RelationsSubscriptionManager
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.search.SubscriptionEventChannel
import io.aparok.app.domain.spaces.SpaceDeletedStatusWatcher
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.domain.workspace.SyncAndP2PStatusChannel
import io.aparok.app.other.DefaultDeepLinkResolver
import io.aparok.app.presentation.sync.SpaceSyncAndP2PStatusProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module
object SubscriptionsModule {

    @JvmStatic
    @Provides
    @Singleton
    fun relationsSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        store: StoreOfRelations,
        logger: Logger
    ): RelationsSubscriptionContainer = RelationsSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun objectTypesSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        store: StoreOfObjectTypes,
        logger: Logger
    ): ObjectTypesSubscriptionContainer = ObjectTypesSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun relationOptionsSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        store: StoreOfRelationOptions,
        logger: Logger
    ): RelationOptionsSubscriptionContainer = RelationOptionsSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun relationsStore(): StoreOfRelations = DefaultStoreOfRelations()

    @JvmStatic
    @Provides
    @Singleton
    fun objectTypesStore(): StoreOfObjectTypes = DefaultStoreOfObjectTypes()

    @JvmStatic
    @Provides
    @Singleton
    fun relationOptionsStore(): StoreOfRelationOptions = DefaultStoreOfRelationOptions()

    @JvmStatic
    @Provides
    @Singleton
    fun relationsSubscriptionManager(
        subscription: RelationsSubscriptionContainer,
        spaceManager: SpaceManager,
    ): RelationsSubscriptionManager = RelationsSubscriptionManager(
        container = subscription,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun objectTypesSubscriptionManager(
        subscription: ObjectTypesSubscriptionContainer,
        spaceManager: SpaceManager
    ): ObjectTypesSubscriptionManager = ObjectTypesSubscriptionManager(
        container = subscription,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun hasInstanceOfObjectTypeSubscriptionContainer(
        storelessContainer: StorelessSubscriptionContainer,
        storeOfObjectTypes: StoreOfObjectTypes,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): HasInstanceOfObjectTypeSubscriptionContainer = HasInstanceOfObjectTypeSubscriptionContainer(
        storelessContainer = storelessContainer,
        storeOfObjectTypes = storeOfObjectTypes,
        dispatchers = dispatchers,
        logger = logger,
        scope = scope
    )

    @JvmStatic
    @Provides
    @Singleton
    fun hasInstanceOfObjectTypeSubscriptionManager(
        container: HasInstanceOfObjectTypeSubscriptionContainer,
        spaceManager: SpaceManager
    ): HasInstanceOfObjectTypeSubscriptionManager = HasInstanceOfObjectTypeSubscriptionManager(
        container = container,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun relationOptionsSubscriptionManager(
        subscription: RelationOptionsSubscriptionContainer,
        spaceManager: SpaceManager
    ): RelationOptionsSubscriptionManager = RelationOptionsSubscriptionManager(
        container = subscription,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun spaceStatusWatcher(
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        configStorage: ConfigStorage,
        container: StorelessSubscriptionContainer,
        logger: Logger
    ): SpaceDeletedStatusWatcher = SpaceDeletedStatusWatcher(
        spaceManager = spaceManager,
        dispatchers = dispatchers,
        configStorage = configStorage,
        scope = scope,
        container = container,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun userPermissionProvider(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: StorelessSubscriptionContainer,
        repo: AuthRepository,
        logger: Logger,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
    ): UserPermissionProvider = DefaultUserPermissionProvider(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        repo = repo,
        logger = logger,
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer
    )

    @JvmStatic
    @Provides
    @Singleton
    fun spaceViewSubscriptionContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: StorelessSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger,
        configStorage: ConfigStorage
    ): SpaceViewSubscriptionContainer = SpaceViewSubscriptionContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger,
        config = configStorage
    )

    @JvmStatic
    @Provides
    @Singleton
    fun chatsViewSubscriptionContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: CrossSpaceSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger,
    ): ChatsDetailsSubscriptionContainer = ChatsDetailsSubscriptionContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger,
    )

    @JvmStatic
    @Provides
    @Singleton
    fun participantSubscriptionContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: CrossSpaceSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger,
    ): ParticipantSubscriptionContainer = ParticipantSubscriptionContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger,
    )

    @JvmStatic
    @Provides
    @Singleton
    fun crossSpaceObjectTypesContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: CrossSpaceSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger,
    ): CrossSpaceObjectTypesContainer = CrossSpaceObjectTypesContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger,
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideCrossSpaceSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): CrossSpaceSubscriptionContainer =
        CrossSpaceSubscriptionContainer.Impl(repo, channel, dispatchers, logger)

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatPreviewContainer(
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        dispatchers: AppCoroutineDispatchers,
        repo: BlockRepository,
        logger: Logger,
        events: ChatEventChannel,
        subscription: StorelessSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager
    ): ChatPreviewContainer = ChatPreviewContainer.Default(
        repo = repo,
        dispatchers = dispatchers,
        scope = scope,
        logger = logger,
        events = events,
        subscription = subscription,
        awaitAccountStart = awaitAccountStartManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun profileSubscriptionManager(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: StorelessSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        configStorage: ConfigStorage,
        logger: Logger
    ): ProfileSubscriptionManager = ProfileSubscriptionManager.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStartManager = awaitAccountStartManager,
        configStorage = configStorage,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun activeSpaceMemberSubscriptionContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: StorelessSubscriptionContainer,
        spaceManager: SpaceManager,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger
    ): ActiveSpaceMemberSubscriptionContainer = ActiveSpaceMemberSubscriptionContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        manager = spaceManager,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSpaceSyncStatusProvider(
        syncChannel: SyncAndP2PStatusChannel,
        spaceManager: SpaceManager
    ): SpaceSyncAndP2PStatusProvider = SpaceSyncAndP2PStatusProviderImpl(
        spaceManager = spaceManager,
        spaceSyncStatusChannel = syncChannel
    )

    @JvmStatic
    @Provides
    @Singleton
    fun globalSubscriptionManager(
        types: ObjectTypesSubscriptionManager,
        relations: RelationsSubscriptionManager,
        relationOptions: RelationOptionsSubscriptionManager,
        permissions: UserPermissionProvider,
        isSpaceDeleted: SpaceDeletedStatusWatcher,
        profileSubscriptionManager: ProfileSubscriptionManager,
        networkConnectionStatus: NetworkConnectionStatus,
        deviceTokenStoringService: DeviceTokenStoringService,
        pushKeyProvider: PushKeyProvider,
        hasInstanceOfType: HasInstanceOfObjectTypeSubscriptionManager,
        logger: Logger
    ): GlobalSubscriptionManager = GlobalSubscriptionManager.Default(
        types = types,
        relations = relations,
        relationOptions = relationOptions,
        permissions = permissions,
        isSpaceDeleted = isSpaceDeleted,
        profile = profileSubscriptionManager,
        networkConnectionStatus = networkConnectionStatus,
        deviceTokenStoringService = deviceTokenStoringService,
        pushKeyProvider = pushKeyProvider,
        hasInstanceOfType = hasInstanceOfType,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun searchObjects(
        repo: BlockRepository
    ): SearchObjects = SearchObjects(repo = repo)

    @JvmStatic
    @PerScreen
    @Singleton
    fun getObjectTypes(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetObjectTypes = GetObjectTypes(repo, dispatchers)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDebugAccountSelectTrace(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DebugAccountSelectTrace =
        DebugAccountSelectTrace(
            repo = repo,
            dispatchers = dispatchers
        )

    @JvmStatic
    @Provides
    @Singleton
    fun deviceTokenStoreService(): DeviceTokenStoringService = DeviceTokenStoringServiceImpl()

    @JvmStatic
    @Provides
    @Singleton
    fun provideDeeplinkResolver() : DeepLinkResolver = DefaultDeepLinkResolver

    @JvmStatic
    @Provides
    @Singleton
    fun providePendingIntentStore(): PendingIntentStore = PendingIntentStore()
}