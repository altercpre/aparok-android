package io.aparok.app.di.feature

import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.GetDebugSettings
import io.aparok.app.domain.config.InfrastructureRepository
import io.aparok.app.domain.config.UseCustomContextMenu
import io.aparok.app.domain.debugging.DebugLocalStore
import io.aparok.app.domain.debugging.DebugSpace
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.ui.settings.DebugSettingsFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [DebugSettingsModule::class])
@PerScreen
interface DebugSettingsSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: DebugSettingsModule): Builder
        fun build(): DebugSettingsSubComponent
    }

    fun inject(fragment: DebugSettingsFragment)
}

@Module
class DebugSettingsModule {

    @Provides
    @PerScreen
    fun provideUseCustomContextMenu(repo: InfrastructureRepository): UseCustomContextMenu =
        UseCustomContextMenu(repo)

    @Provides
    @PerScreen
    fun provideGetDebugSettings(
        repo: InfrastructureRepository
    ): GetDebugSettings = GetDebugSettings(
        repo = repo
    )

    @Provides
    @PerScreen
    fun provideDebugSync(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager
    ): DebugSpace = DebugSpace(
        repo = repo,
        dispatchers = dispatchers,
        spaceManager = spaceManager
    )

    @Provides
    @PerScreen
    fun provideDebugLocalStore(repo: BlockRepository) : DebugLocalStore = DebugLocalStore(repo)
}