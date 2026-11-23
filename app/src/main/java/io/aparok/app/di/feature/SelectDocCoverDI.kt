package io.aparok.app.di.feature;

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.device.DefaultGradientCollectionProvider
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.cover.GetCoverGradientCollection
import io.aparok.app.domain.cover.RemoveDocCover
import io.aparok.app.domain.cover.SetDocCoverColor
import io.aparok.app.domain.cover.SetDocCoverGradient
import io.aparok.app.domain.cover.SetDocCoverImage
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.editor.cover.SelectCoverObjectSetViewModel
import io.aparok.app.presentation.editor.cover.SelectCoverObjectViewModel
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.editor.cover.SelectCoverObjectFragment
import io.aparok.app.ui.editor.cover.SelectCoverObjectSetFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [SelectCoverObjectModule::class])
@PerModal
interface SelectCoverObjectSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: SelectCoverObjectModule): Builder
        fun build(): SelectCoverObjectSubComponent
    }

    fun inject(fragment: SelectCoverObjectFragment)
}

@Module
object SelectCoverObjectModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDocCoverColorUseCase(
        repo: BlockRepository
    ): SetDocCoverColor = SetDocCoverColor(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDocCoverGradientUseCase(
        repo: BlockRepository
    ): SetDocCoverGradient = SetDocCoverGradient(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideRemoveDocCoverUseCase(
        repo: BlockRepository
    ): RemoveDocCover = RemoveDocCover(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSelectDocCoverViewModelFactory(
        setCoverImage: SetDocCoverImage,
        setCoverColor: SetDocCoverColor,
        setCoverGradient: SetDocCoverGradient,
        removeCover: RemoveDocCover,
        dispatcher: Dispatcher<Payload>,
        getCoverGradientCollection: GetCoverGradientCollection,
        analytics: Analytics,
        spaceManager: SpaceManager
    ): SelectCoverObjectViewModel.Factory = SelectCoverObjectViewModel.Factory(
        setCoverImage = setCoverImage,
        setCoverColor = setCoverColor,
        setCoverGradient = setCoverGradient,
        removeCover = removeCover,
        dispatcher = dispatcher,
        getCoverGradientCollection = getCoverGradientCollection,
        analytics = analytics,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideGetCoverGradientCollectionUseCase(
    ): GetCoverGradientCollection = GetCoverGradientCollection(DefaultGradientCollectionProvider)
}

@Subcomponent(modules = [SelectCoverObjectSetModule::class])
@PerModal
interface SelectCoverObjectSetSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: SelectCoverObjectSetModule): Builder
        fun build(): SelectCoverObjectSetSubComponent
    }

    fun inject(fragment: SelectCoverObjectSetFragment)
}

@Module
object SelectCoverObjectSetModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDocCoverColorUseCase(
        repo: BlockRepository
    ): SetDocCoverColor = SetDocCoverColor(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDocCoverGradientUseCase(
        repo: BlockRepository
    ): SetDocCoverGradient = SetDocCoverGradient(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideRemoveDocCoverUseCase(
        repo: BlockRepository
    ): RemoveDocCover = RemoveDocCover(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSelectDocCoverViewModelFactory(
        setCoverImage: SetDocCoverImage,
        setCoverColor: SetDocCoverColor,
        setCoverGradient: SetDocCoverGradient,
        removeCover: RemoveDocCover,
        dispatcher: Dispatcher<Payload>,
        getCoverGradientCollection: GetCoverGradientCollection,
        analytics: Analytics,
        spaceManager: SpaceManager
    ): SelectCoverObjectSetViewModel.Factory = SelectCoverObjectSetViewModel.Factory(
        setCoverImage = setCoverImage,
        setCoverColor = setCoverColor,
        setCoverGradient = setCoverGradient,
        removeCover = removeCover,
        dispatcher = dispatcher,
        getCoverGradientCollection = getCoverGradientCollection,
        analytics = analytics,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideGetCoverGradientCollectionUseCase(
    ): GetCoverGradientCollection = GetCoverGradientCollection(DefaultGradientCollectionProvider)
}