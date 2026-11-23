package io.aparok.app.di.feature.relations

import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.relations.LimitObjectTypeViewModel
import io.aparok.app.presentation.relations.model.CreateFromScratchState
import io.aparok.app.presentation.relations.model.StateHolder
import io.aparok.app.ui.relations.LimitObjectTypeFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [LimitObjectTypeModule::class])
@PerDialog
interface LimitObjectTypeSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: LimitObjectTypeModule): Builder
        fun build(): LimitObjectTypeSubComponent
    }

    fun inject(fragment: LimitObjectTypeFragment)
}

@Module
object LimitObjectTypeModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        searchObjects: SearchObjects,
        urlBuilder: UrlBuilder,
        state: StateHolder<CreateFromScratchState>,
        spaceManager: SpaceManager
    ): LimitObjectTypeViewModel.Factory = LimitObjectTypeViewModel.Factory(
        searchObjects = searchObjects,
        urlBuilder = urlBuilder,
        state = state,
        spaceManager = spaceManager
    )
}