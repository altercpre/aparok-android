package io.aparok.app.di.feature.sets

import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.presentation.sets.SelectFilterRelationViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.ui.sets.modals.filter.SelectFilterRelationFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [SelectFilterRelationModule::class])
@PerModal
interface SelectFilterRelationSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: SelectFilterRelationModule): Builder
        fun build(): SelectFilterRelationSubComponent
    }

    fun inject(fragment: SelectFilterRelationFragment)
}

@Module
object SelectFilterRelationModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideSelectSortRelationViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        storeOfRelations: StoreOfRelations
    ): SelectFilterRelationViewModel.Factory = SelectFilterRelationViewModel.Factory(
        objectState = state,
        storeOfRelations = storeOfRelations
    )
}