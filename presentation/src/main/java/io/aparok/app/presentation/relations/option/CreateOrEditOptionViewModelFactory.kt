package io.aparok.app.presentation.relations.option

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.CreateRelationOption
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.relations.providers.ObjectValueProvider
import io.aparok.app.presentation.util.Dispatcher
import javax.inject.Inject

class CreateOrEditOptionViewModelFactory  @Inject constructor(
    private val params: CreateOrEditOptionViewModel.ViewModelParams,
    private val values: ObjectValueProvider,
    private val createOption: CreateRelationOption,
    private val setObjectDetails: SetObjectDetails,
    private val dispatcher: Dispatcher<Payload>,
    private val spaceManager: SpaceManager,
    private val analytics: Analytics,
    private val storeOfRelations: StoreOfRelations,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = CreateOrEditOptionViewModel(
        viewModelParams = params,
        values = values,
        createOption = createOption,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        spaceManager = spaceManager,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    ) as T
}