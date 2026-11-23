package io.aparok.app.feature_object_type.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.block.interactor.UpdateText
import io.aparok.app.domain.dataview.SetDataViewProperties
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.DuplicateObjects
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.DeleteObjects
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.primitives.GetObjectTypeConflictingFields
import io.aparok.app.domain.primitives.SetObjectTypeRecommendedFields
import io.aparok.app.domain.relations.AddToFeaturedRelations
import io.aparok.app.domain.relations.RemoveFromFeaturedRelations
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.templates.CreateTemplate
import io.aparok.app.domain.widgets.CreateWidget
import io.aparok.app.domain.widgets.DeleteWidget
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.feature_object_type.ui.ObjectTypeVmParams
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.navigation.backstack.BackHistoryDelegate
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.util.Dispatcher
import javax.inject.Inject

class ObjectTypeVMFactory @Inject constructor(
    private val vmParams: ObjectTypeVmParams,
    private val analytics: Analytics,
    private val urlBuilder: UrlBuilder,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val userPermissionProvider: UserPermissionProvider,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider,
    private val fieldParser: FieldParser,
    private val coverImageHashProvider: CoverImageHashProvider,
    private val deleteObjects: DeleteObjects,
    private val setObjectDetails: SetObjectDetails,
    private val stringResourceProvider: StringResourceProvider,
    private val createTemplate: CreateTemplate,
    private val duplicateObjects: DuplicateObjects,
    private val getObjectTypeConflictingFields: GetObjectTypeConflictingFields,
    private val objectTypeSetRecommendedFields: SetObjectTypeRecommendedFields,
    private val setDataViewProperties: SetDataViewProperties,
    private val dispatcher: Dispatcher<Payload>,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val createWidget: CreateWidget,
    private val deleteWidget: DeleteWidget,
    private val spaceManager: SpaceManager,
    private val getObject: GetObject,
    private val addToFeaturedRelations: AddToFeaturedRelations,
    private val removeFromFeaturedRelations: RemoveFromFeaturedRelations,
    private val updateText: UpdateText,
    private val searchObjects: SearchObjects,
    private val backHistoryDelegate: BackHistoryDelegate,
    private val exitToVaultDelegate: ExitToVaultDelegate
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ObjectTypeViewModel(
            vmParams = vmParams,
            analytics = analytics,
            urlBuilder = urlBuilder,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            userPermissionProvider = userPermissionProvider,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
            fieldParser = fieldParser,
            coverImageHashProvider = coverImageHashProvider,
            deleteObjects = deleteObjects,
            setObjectDetails = setObjectDetails,
            stringResourceProvider = stringResourceProvider,
            createTemplate = createTemplate,
            duplicateObjects = duplicateObjects,
            getObjectTypeConflictingFields = getObjectTypeConflictingFields,
            objectTypeSetRecommendedFields = objectTypeSetRecommendedFields,
            setDataViewProperties = setDataViewProperties,
            dispatcher = dispatcher,
            setObjectListIsArchived = setObjectListIsArchived,
            createWidget = createWidget,
            deleteWidget = deleteWidget,
            spaceManager = spaceManager,
            getObject = getObject,
            addToFeaturedRelations = addToFeaturedRelations,
            removeFromFeaturedRelations = removeFromFeaturedRelations,
            updateText = updateText,
            searchObjects = searchObjects,
            backHistoryDelegate = backHistoryDelegate,
            exitToVaultDelegate = exitToVaultDelegate
        ) as T
}