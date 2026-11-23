package io.aparok.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.domain.history.GetVersions
import io.aparok.app.domain.history.SetVersion
import io.aparok.app.domain.history.ShowVersion
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import io.aparok.app.presentation.history.VersionHistoryViewModel.VmParams
import io.aparok.app.presentation.sets.state.ObjectStateReducer
import javax.inject.Inject

class VersionHistoryVMFactory @Inject constructor(
    private val vmParams: VmParams,
    private val getVersions: GetVersions,
    private val objectSearch: SearchObjects,
    private val dateProvider: DateProvider,
    private val analytics: Analytics,
    private val urlBuilder: UrlBuilder,
    private val showVersion: ShowVersion,
    private val setVersion: SetVersion,
    private val renderer: DefaultBlockViewRenderer,
    private val setStateReducer: ObjectStateReducer,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VersionHistoryViewModel(
            vmParams = vmParams,
            getVersions = getVersions,
            objectSearch = objectSearch,
            dateProvider = dateProvider,
            analytics = analytics,
            urlBuilder = urlBuilder,
            showVersion = showVersion,
            renderer = renderer,
            setVersion = setVersion,
            setStateReducer = setStateReducer,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes
        ) as T
    }
}