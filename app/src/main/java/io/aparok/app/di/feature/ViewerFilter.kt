package io.aparok.app.di.feature;

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.presentation.sets.ObjectSetDatabase
import io.aparok.app.presentation.sets.filter.ViewerFilterViewModel
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.sets.ViewerFilterFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ViewerFilterModule::class])
@ViewerFilterByScope
interface ViewerFilterSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ViewerFilterModule): Builder
        fun build(): ViewerFilterSubComponent
    }

    fun inject(fragment: ViewerFilterFragment)
}

@Module
object ViewerFilterModule {

    @JvmStatic
    @Provides
    @ViewerFilterByScope
    fun provideViewerFilterViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        urlBuilder: UrlBuilder,
        analytics: Analytics,
        storeOfRelations: StoreOfRelations,
        db: ObjectSetDatabase,
        fieldParser: FieldParser,
        storeOfObjectTypes: StoreOfObjectTypes
    ): ViewerFilterViewModel.Factory = ViewerFilterViewModel.Factory(
        state = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        urlBuilder = urlBuilder,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        objectSetDatabase = db,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes
    )
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewerFilterByScope