package io.aparok.app.feature_properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.dataview.SetDataViewProperties
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.SetObjectTypeRecommendedFields
import io.aparok.app.domain.relations.CreateRelation
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.feature_properties.add.EditTypePropertiesVmParams
import io.aparok.app.presentation.util.Dispatcher
import javax.inject.Inject

class EditTypePropertiesViewModelFactory @Inject constructor(
    private val vmParams: EditTypePropertiesVmParams,
    private val storeOfRelations: StoreOfRelations,
    private val stringResourceProvider: StringResourceProvider,
    private val createRelation: CreateRelation,
    private val setObjectDetails: SetObjectDetails,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val setObjectTypeRecommendedFields: SetObjectTypeRecommendedFields,
    private val setDataViewProperties: SetDataViewProperties,
    private val dispatcher: Dispatcher<Payload>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        EditTypePropertiesViewModel(
            vmParams = vmParams,
            storeOfRelations = storeOfRelations,
            stringResourceProvider = stringResourceProvider,
            createRelation = createRelation,
            setObjectDetails = setObjectDetails,
            storeOfObjectTypes = storeOfObjectTypes,
            setObjectTypeRecommendedFields = setObjectTypeRecommendedFields,
            setDataViewProperties = setDataViewProperties,
            dispatcher = dispatcher
        ) as T
}