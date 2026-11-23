package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.presentation.sets.SelectSortRelationViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.sets.modals.sort.SelectSortRelationFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [SelectSortRelationModule::class])
@PerModal
interface SelectSortRelationSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: SelectSortRelationModule): Builder
        fun build(): SelectSortRelationSubComponent
    }

    fun inject(fragment: SelectSortRelationFragment)
}

@Module
object SelectSortRelationModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideSelectSortRelationViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        storeOfRelations: StoreOfRelations,
        analytics: Analytics
    ): SelectSortRelationViewModel.Factory = SelectSortRelationViewModel.Factory(
        objectState = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        storeOfRelations = storeOfRelations,
        analytics = analytics
    )
}