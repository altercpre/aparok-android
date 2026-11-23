package io.aparok.app.presentation.relations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.primitives.SetObjectTypeRecommendedFields
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.relations.AddToFeaturedRelations
import io.aparok.app.domain.relations.DeleteRelationFromObject
import io.aparok.app.domain.relations.RemoveFromFeaturedRelations
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.objects.LockedStateProvider
import io.aparok.app.presentation.relations.RelationListViewModel.VmParams
import io.aparok.app.presentation.relations.providers.ObjectRelationListProvider
import io.aparok.app.presentation.util.Dispatcher

class ObjectRelationListViewModelFactory(
    private val vmParams: VmParams,
    private val objectRelationListProvider: ObjectRelationListProvider,
    private val lockedStateProvider: LockedStateProvider,
    private val urlBuilder: UrlBuilder,
    private val dispatcher: Dispatcher<Payload>,
    private val updateDetail: UpdateDetail,
    private val addToFeaturedRelations: AddToFeaturedRelations,
    private val removeFromFeaturedRelations: RemoveFromFeaturedRelations,
    private val deleteRelationFromObject: DeleteRelationFromObject,
    private val analytics: Analytics,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val addRelationToObject: AddRelationToObject,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val fieldParser: FieldParser,
    private val userPermissionProvider: UserPermissionProvider,
    private val setObjectTypeRecommendedFields: SetObjectTypeRecommendedFields
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RelationListViewModel(
            vmParams = vmParams,
            objectRelationListProvider = objectRelationListProvider,
            lockedStateProvider = lockedStateProvider,
            urlBuilder = urlBuilder,
            dispatcher = dispatcher,
            updateDetail = updateDetail,
            addToFeaturedRelations = addToFeaturedRelations,
            removeFromFeaturedRelations = removeFromFeaturedRelations,
            deleteRelationFromObject = deleteRelationFromObject,
            analytics = analytics,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            addRelationToObject = addRelationToObject,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            fieldParser = fieldParser,
            userPermissionProvider = userPermissionProvider,
            setObjectTypeRecommendedFields = setObjectTypeRecommendedFields
        ) as T
    }
}