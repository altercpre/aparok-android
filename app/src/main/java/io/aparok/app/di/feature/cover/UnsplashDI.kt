package io.aparok.app.di.feature.cover

import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.cover.SetDocCoverImage
import io.aparok.app.domain.unsplash.SearchUnsplashImage
import io.aparok.app.domain.unsplash.UnsplashRepository
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.editor.cover.UnsplashViewModel
import io.aparok.app.ui.editor.cover.UnsplashBaseFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [UnsplashModule::class])
@PerDialog
interface UnsplashSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: UnsplashModule): Builder
        fun build(): UnsplashSubComponent
    }

    fun inject(fragment: UnsplashBaseFragment)
}

@Module
object UnsplashModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        search: SearchUnsplashImage,
        delegator: Delegator<Action>
    ): UnsplashViewModel.Factory {
        return UnsplashViewModel.Factory(
            search = search,
            delegator = delegator
        )
    }

    @JvmStatic
    @Provides
    @PerDialog
    fun provideSearch(repo: UnsplashRepository): SearchUnsplashImage = SearchUnsplashImage(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideSetDocCoverImageUseCase(
        repo: BlockRepository
    ): SetDocCoverImage = SetDocCoverImage(repo)
}