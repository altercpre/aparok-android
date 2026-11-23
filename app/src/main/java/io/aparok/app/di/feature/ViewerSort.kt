package io.aparok.app.di.feature;

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.presentation.sets.sort.ViewerSortViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.sets.modals.sort.ViewerSortFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ViewerSortModule::class])
@PerModal
interface ViewerSortSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ViewerSortModule): Builder
        fun build(): ViewerSortSubComponent
    }

    fun inject(fragment: ViewerSortFragment)
}

@Module
object ViewerSortModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        updateDataViewViewer: UpdateDataViewViewer,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        storeOfRelations: StoreOfRelations
    ): ViewerSortViewModel.Factory = ViewerSortViewModel.Factory(
        state = state,
        updateDataViewViewer = updateDataViewViewer,
        dispatcher = dispatcher,
        analytics = analytics,
        storeOfRelations = storeOfRelations
    )
}
