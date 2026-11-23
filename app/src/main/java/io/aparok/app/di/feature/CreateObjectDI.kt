package io.aparok.app.di.feature

import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.page.CreateObjectByTypeAndTemplate
import io.aparok.app.domain.templates.GetTemplates
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.objects.CreateObjectViewModel
import io.aparok.app.ui.editor.CreateObjectFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [CreateObjectModule::class])
@PerScreen
interface CreateObjectSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: CreateObjectModule): Builder
        fun build(): CreateObjectSubComponent
    }

    fun inject(fragment: CreateObjectFragment)
}

@Module
object CreateObjectModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun getCreateObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): CreateObjectByTypeAndTemplate = CreateObjectByTypeAndTemplate(
        repo = repo,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideGetDefaultPageType(
        blockRepository: BlockRepository,
        userSettingsRepository: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates = GetTemplates(
        repo = repo,
        spaceManager = spaceManager,
        dispatchers = dispatchers
    )


    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewModelFactory(
        createObject: CreateObjectByTypeAndTemplate,
        spaceManager: SpaceManager,
        awaitAccountStartManager: AwaitAccountStartManager
    ): CreateObjectViewModel.Factory = CreateObjectViewModel.Factory(
        createObject = createObject,
        spaceManager = spaceManager,
        awaitAccountStart = awaitAccountStartManager
    )
}