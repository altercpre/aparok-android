package io.aparok.app.presentation.moving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate

class MoveToViewModelFactory(
    private val vmParams: MoveToViewModel.VmParams,
    private val urlBuilder: UrlBuilder,
    private val searchObjects: SearchObjects,
    private val analytics: Analytics,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val fieldParser: FieldParser,
    private val storeOfObjectTypes: StoreOfObjectTypes
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoveToViewModel(
            vmParams = vmParams,
            urlBuilder = urlBuilder,
            searchObjects = searchObjects,
            analytics = analytics,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes
        ) as T
    }
}