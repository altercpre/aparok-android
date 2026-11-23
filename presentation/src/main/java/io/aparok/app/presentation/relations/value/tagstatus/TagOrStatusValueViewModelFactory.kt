package io.aparok.app.presentation.relations.value.tagstatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.StoreOfRelationOptions
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.DeleteRelationOptions
import io.aparok.app.domain.relations.SetRelationOptionOrder
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.relations.providers.ObjectValueProvider
import io.aparok.app.presentation.util.Dispatcher
import javax.inject.Inject

class TagOrStatusValueViewModelFactory @Inject constructor(
    private val params: TagOrStatusValueViewModel.ViewModelParams,
    private val values: ObjectValueProvider,
    private val dispatcher: Dispatcher<Payload>,
    private val setObjectDetails: UpdateDetail,
    private val analytics: Analytics,
    private val deleteRelationOptions: DeleteRelationOptions,
    private val setRelationOptionOrder: SetRelationOptionOrder,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfRelationOptions: StoreOfRelationOptions
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = TagOrStatusValueViewModel(
        viewModelParams = params,
        values = values,
        dispatcher = dispatcher,
        setObjectDetails = setObjectDetails,
        analytics = analytics,
        deleteRelationOptions = deleteRelationOptions,
        setRelationOptionOrder = setRelationOptionOrder,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        storeOfRelationOptions = storeOfRelationOptions
    ) as T
}