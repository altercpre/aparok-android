package io.aparok.app.feature_allcontent.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.domain.all_content.RestoreAllContentState
import io.aparok.app.domain.all_content.UpdateAllContentState
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.workspace.RemoveObjectsFromWorkspace
import io.aparok.app.feature_allcontent.presentation.AllContentViewModel.VmParams
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import javax.inject.Inject

class AllContentViewModelFactory @Inject constructor(
    private val vmParams: VmParams,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val urlBuilder: UrlBuilder,
    private val analytics: Analytics,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val updateAllContentState: UpdateAllContentState,
    private val restoreAllContentState: RestoreAllContentState,
    private val searchObjects: SearchObjects,
    private val localeProvider: LocaleProvider,
    private val createObject: CreateObject,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val removeObjectsFromWorkspace: RemoveObjectsFromWorkspace,
    private val userPermissionProvider: UserPermissionProvider,
    private val fieldParser: FieldParser,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AllContentViewModel(
            vmParams = vmParams,
            storeOfObjectTypes = storeOfObjectTypes,
            urlBuilder = urlBuilder,
            analytics = analytics,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            restoreAllContentState = restoreAllContentState,
            updateAllContentState = updateAllContentState,
            searchObjects = searchObjects,
            localeProvider = localeProvider,
            createObject = createObject,
            setObjectListIsArchived = setObjectListIsArchived,
            removeObjectsFromWorkspace = removeObjectsFromWorkspace,
            userPermissionProvider = userPermissionProvider,
            fieldParser = fieldParser,
            spaceViews = spaceViews,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider
        ) as T
}