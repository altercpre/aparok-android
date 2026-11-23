package io.aparok.app.di.main

import io.aparok.app.data.auth.status.SyncAndP2PStatusDataChannel
import io.aparok.app.data.auth.status.SyncAndP2PStatusEventsStore
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.domain.workspace.SyncAndP2PStatusChannel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WorkspaceModule {

    @JvmStatic
    @Provides
    @Singleton
    fun spaces(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): SpaceManager = SpaceManager.Impl(
        dispatchers = dispatchers,
        repo = repo,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun spaceSyncStatusChannel(
        store: SyncAndP2PStatusEventsStore
    ): SyncAndP2PStatusChannel = SyncAndP2PStatusDataChannel(store)

    @JvmStatic
    @Provides
    @Singleton
    fun provideMembershipProvider(): io.aparok.app.presentation.membership.provider.MembershipProvider =
        io.aparok.app.presentation.membership.provider.MembershipProvider.Default()
}