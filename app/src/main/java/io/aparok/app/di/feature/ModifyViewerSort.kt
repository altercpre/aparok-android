package io.aparok.app.di.feature;

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.presentation.sets.sort.ModifyViewerSortViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.sets.modals.sort.ModifyViewerSortFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ModifyViewerSortModule::class])
@PerModal
interface ModifyViewerSortSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ModifyViewerSortModule): Builder
        fun build(): ModifyViewerSortSubComponent
    }

    fun inject(fragment: ModifyViewerSortFragment)
}

@Module
object ModifyViewerSortModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        analytics: Analytics,
        storeOfRelations: StoreOfRelations
    ): ModifyViewerSortViewModel.Factory = ModifyViewerSortViewModel.Factory(
        state = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        analytics = analytics,
        storeOfRelations = storeOfRelations
    )
}
