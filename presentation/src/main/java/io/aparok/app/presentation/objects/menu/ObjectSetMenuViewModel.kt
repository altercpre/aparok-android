package io.aparok.app.presentation.objects.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.restrictions.ObjectRestriction
import io.aparok.app.domain.base.fold
import io.aparok.app.domain.block.interactor.UpdateFields
import io.aparok.app.domain.collections.AddObjectToCollection
import io.aparok.app.domain.dashboard.interactor.SetObjectListIsFavorite
import io.aparok.app.domain.favorites.AddPersonalFavorite
import io.aparok.app.domain.favorites.RemovePersonalFavorite
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.GetSpaceInviteLink
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.DuplicateObject
import io.aparok.app.domain.`object`.GetObject
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.page.AddBackLinkToObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.relations.AddToFeaturedRelations
import io.aparok.app.domain.relations.DeleteRelationFromObject
import io.aparok.app.domain.relations.RemoveFromFeaturedRelations
import io.aparok.app.domain.widgets.CreateWidget
import io.aparok.app.domain.widgets.DeleteWidget
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.common.PayloadDelegator
import io.aparok.app.presentation.extension.getObject
import io.aparok.app.presentation.extension.sendAnalyticsResolveObjectConflict
import io.aparok.app.presentation.objects.ObjectAction
import io.aparok.app.presentation.objects.menu.ObjectMenuViewModelBase.Command.ShareDeeplinkToObject
import io.aparok.app.presentation.sets.dataViewState
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.util.downloader.DebugGoroutinesShareDownloader
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ObjectSetMenuViewModel(
    setObjectIsArchived: SetObjectListIsArchived,
    addBackLinkToObject: AddBackLinkToObject,
    duplicateObject: DuplicateObject,
    delegator: Delegator<Action>,
    urlBuilder: UrlBuilder,
    private val dispatcher: Dispatcher<Payload>,
    menuOptionsProvider: ObjectMenuOptionsProvider,
    createWidget: CreateWidget,
    spaceManager: SpaceManager,
    payloadDelegator: PayloadDelegator,
    private val objectState: StateFlow<ObjectState>,
    private val analytics: Analytics,
    private val addObjectToCollection: AddObjectToCollection,
    private val debugGoroutinesShareDownloader: DebugGoroutinesShareDownloader,
    private val deepLinkResolver: DeepLinkResolver,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    setObjectListIsFavorite: SetObjectListIsFavorite,
    fieldParser: FieldParser,
    spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
    getSpaceInviteLink: GetSpaceInviteLink,
    private val addToFeaturedRelations: AddToFeaturedRelations,
    private val removeFromFeaturedRelations: RemoveFromFeaturedRelations,
    private val userPermissionProvider: UserPermissionProvider,
    private val deleteRelationFromObject: DeleteRelationFromObject,
    private val updateFields: UpdateFields,
    private val setObjectDetails: SetObjectDetails,
    private val showObject: GetObject,
    private val deleteWidget: DeleteWidget,
    private val addPersonalFavorite: AddPersonalFavorite,
    private val removePersonalFavorite: RemovePersonalFavorite
) : ObjectMenuViewModelBase(
    setObjectIsArchived = setObjectIsArchived,
    addBackLinkToObject = addBackLinkToObject,
    duplicateObject = duplicateObject,
    delegator = delegator,
    urlBuilder = urlBuilder,
    dispatcher = dispatcher,
    analytics = analytics,
    menuOptionsProvider = menuOptionsProvider,
    addObjectToCollection = addObjectToCollection,
    debugGoroutinesShareDownloader = debugGoroutinesShareDownloader,
    createWidget = createWidget,
    spaceManager = spaceManager,
    analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
    payloadDelegator = payloadDelegator,
    setObjectListIsFavorite = setObjectListIsFavorite,
    fieldParser = fieldParser,
    deepLinkResolver = deepLinkResolver,
    spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
    getSpaceInviteLink = getSpaceInviteLink,
    showObject = showObject,
    deleteWidget = deleteWidget,
    addPersonalFavorite = addPersonalFavorite,
    removePersonalFavorite = removePersonalFavorite
) {

    init {
        Timber.i("ObjectSetMenuViewModel, init")
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val addBackLinkToObject: AddBackLinkToObject,
        private val duplicateObject: DuplicateObject,
        private val delegator: Delegator<Action>,
        private val urlBuilder: UrlBuilder,
        private val dispatcher: Dispatcher<Payload>,
        private val analytics: Analytics,
        private val objectState: StateFlow<ObjectState>,
        private val menuOptionsProvider: ObjectMenuOptionsProvider,
        private val addObjectToCollection: AddObjectToCollection,
        private val debugGoroutinesShareDownloader: DebugGoroutinesShareDownloader,
        private val createWidget: CreateWidget,
        private val spaceManager: SpaceManager,
        private val deepLinkResolver: DeepLinkResolver,
        private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        private val payloadDelegator: PayloadDelegator,
        private val setObjectListIsFavorite: SetObjectListIsFavorite,
        private val setObjectListIsArchived: SetObjectListIsArchived,
        private val fieldParser: FieldParser,
        private val spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        private val getSpaceInviteLink: GetSpaceInviteLink,
        private val addToFeaturedRelations: AddToFeaturedRelations,
        private val removeFromFeaturedRelations: RemoveFromFeaturedRelations,
        private val userPermissionProvider: UserPermissionProvider,
        private val deleteRelationFromObject: DeleteRelationFromObject,
        private val updateFields: UpdateFields,
        private val setObjectDetails: SetObjectDetails,
        private val showObject: GetObject,
        private val deleteWidget: DeleteWidget,
        private val addPersonalFavorite: AddPersonalFavorite,
        private val removePersonalFavorite: RemovePersonalFavorite
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ObjectSetMenuViewModel(
                setObjectIsArchived = setObjectListIsArchived,
                addBackLinkToObject = addBackLinkToObject,
                duplicateObject = duplicateObject,
                delegator = delegator,
                urlBuilder = urlBuilder,
                analytics = analytics,
                objectState = objectState,
                dispatcher = dispatcher,
                menuOptionsProvider = menuOptionsProvider,
                addObjectToCollection = addObjectToCollection,
                debugGoroutinesShareDownloader = debugGoroutinesShareDownloader,
                createWidget = createWidget,
                spaceManager = spaceManager,
                deepLinkResolver = deepLinkResolver,
                analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
                payloadDelegator = payloadDelegator,
                setObjectListIsFavorite = setObjectListIsFavorite,
                fieldParser = fieldParser,
                spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
                getSpaceInviteLink = getSpaceInviteLink,
                addToFeaturedRelations = addToFeaturedRelations,
                removeFromFeaturedRelations = removeFromFeaturedRelations,
                userPermissionProvider = userPermissionProvider,
                deleteRelationFromObject = deleteRelationFromObject,
                updateFields = updateFields,
                setObjectDetails = setObjectDetails,
                showObject = showObject,
                deleteWidget = deleteWidget,
                addPersonalFavorite = addPersonalFavorite,
                removePersonalFavorite = removePersonalFavorite
            ) as T
        }
    }

    override fun onIconClicked(ctx: Id, space: Id) {
        val dataViewState = objectState.value.dataViewState() ?: return
        viewModelScope.launch {
            if (dataViewState.objectRestrictions.contains(ObjectRestriction.DETAILS)) {
                _toasts.emit(NOT_ALLOWED)
            } else {
                commands.emit(Command.OpenSetIcons)
            }
        }
    }

    override fun onCoverClicked(ctx: Id, space: Id) {
        val dataViewState = objectState.value.dataViewState() ?: return
        viewModelScope.launch {
            if (dataViewState.objectRestrictions.contains(ObjectRestriction.DETAILS)) {
                _toasts.emit(NOT_ALLOWED)
            } else {
                commands.emit(Command.OpenSetCover)
            }
        }
    }

    override fun onDescriptionClicked(ctx: Id, space: Id) {
        val details = objectState.value.dataViewState()?.details ?: return
        viewModelScope.launch {
            if (userPermissionProvider.get(space = SpaceId(space))?.isOwnerOrEditor() != true) {
                _toasts.emit(NOT_ALLOWED)
                return@launch
            }
            val isDescriptionAlreadyInFeatured =
                details.getObject(ctx)?.featuredRelations?.contains(
                    Relations.DESCRIPTION
                ) == true
            if (isDescriptionAlreadyInFeatured) {
                removeFromFeaturedRelations.async(
                    params = RemoveFromFeaturedRelations.Params(
                        ctx = ctx,
                        relations = listOf(Relations.DESCRIPTION)
                    )
                ).fold(
                    onSuccess = { payload ->
                        dispatcher.send(payload)
                        Timber.d("Description was removed from featured relations")
                    },
                    onFailure = {
                        Timber.e(it, "Error while removing description from featured relations")
                        _toasts.emit(SOMETHING_WENT_WRONG_MSG)
                    }
                )
            } else {
                addToFeaturedRelations.async(
                    params = AddToFeaturedRelations.Params(
                        ctx = ctx,
                        relations = listOf(Relations.DESCRIPTION)
                    )
                ).fold(
                    onSuccess = { payload ->
                        dispatcher.send(payload)
                        Timber.d("Description was added to featured relations")
                    },
                    onFailure = {
                        Timber.e(it, "Error while adding description to featured relations")
                        _toasts.emit(SOMETHING_WENT_WRONG_MSG)
                    }
                )
            }
        }
    }

    override fun onRelationsClicked() {
        val dataViewState = objectState.value.dataViewState() ?: return
        viewModelScope.launch {
            if (dataViewState.objectRestrictions.contains(ObjectRestriction.RELATIONS)) {
                _toasts.emit(NOT_ALLOWED)
            } else {
                commands.emit(Command.OpenSetRelations)
            }
        }
    }

    override fun onTemplateNamePrefillToggleClicked(ctx: Id, space: Id) {
        // Not applicable for Sets/Collections - they cannot be templates
    }

    override fun buildActions(
        ctx: Id,
        isArchived: Boolean,
        isFavorite: Boolean,
        isTemplate: Boolean,
        isLocked: Boolean,
        isReadOnly: Boolean,
        isCurrentObjectPinned: Boolean,
        isInMyFavorites: Boolean,
        canToggleChannelPin: Boolean
    ): List<ObjectAction> = buildList {
        if (!isReadOnly) {
            if (isArchived) {
                add(ObjectAction.RESTORE)
            } else {
                add(ObjectAction.MOVE_TO_BIN)
            }
            if (isInMyFavorites) {
                add(ObjectAction.REMOVE_FROM_MY_FAVORITES)
            } else {
                add(ObjectAction.ADD_TO_MY_FAVORITES)
            }
            if (canToggleChannelPin) {
                if (isCurrentObjectPinned) {
                    add(ObjectAction.UNPIN)
                } else {
                    add(ObjectAction.PIN)
                }
            }
            val dataViewState = objectState.value.dataViewState()
            if (dataViewState != null && !dataViewState.objectRestrictions.contains(
                    ObjectRestriction.DUPLICATE
                )
            ) {
                add(ObjectAction.DUPLICATE)
            }
            add(ObjectAction.LINK_TO)
        }
        add(ObjectAction.COPY_LINK)
    }

    override fun onActionClicked(ctx: Id, space: Id, action: ObjectAction) {
        val state = objectState.value.dataViewState() ?: return
        when (action) {
            ObjectAction.MOVE_TO_BIN -> {
                proceedWithUpdatingArchivedStatus(ctx = ctx, isArchived = true)
            }
            ObjectAction.RESTORE -> {
                proceedWithUpdatingArchivedStatus(ctx = ctx, isArchived = false)
            }
            ObjectAction.ADD_TO_FAVOURITE -> {
                proceedWithAddingToFavorites(ctx)
            }
            ObjectAction.REMOVE_FROM_FAVOURITE -> {
                proceedWithRemovingFromFavorites(ctx)
            }
            ObjectAction.ADD_TO_MY_FAVORITES -> {
                proceedWithAddingToMyFavorites(ctx = ctx, space = SpaceId(space))
            }
            ObjectAction.REMOVE_FROM_MY_FAVORITES -> {
                proceedWithRemovingFromMyFavorites(ctx = ctx, space = SpaceId(space))
            }
            ObjectAction.LINK_TO -> {
                proceedWithLinkTo()
            }
            ObjectAction.DUPLICATE -> {
                proceedWithDuplication(
                    ctx = ctx,
                    space = space,
                    details = state.details.details
                )
            }
            ObjectAction.PIN -> {
                val wrapper = state.details.getObject(ctx)
                if (wrapper != null) proceedWithCreatingWidget(obj = wrapper)
            }
            ObjectAction.UNPIN -> {
                proceedWithRemovingWidget()
            }
            ObjectAction.COPY_LINK -> {
                viewModelScope.launch {
                    val link = proceedWithGeneratingObjectLink(
                        space = SpaceId(space),
                        ctx = ctx
                    )
                    commands.emit(ShareDeeplinkToObject(link))
                }
            }
            ObjectAction.MOVE_TO,
            ObjectAction.SEARCH_ON_PAGE,
            ObjectAction.UNDO_REDO,
            ObjectAction.LOCK,
            ObjectAction.UNLOCK,
            ObjectAction.DELETE,
            ObjectAction.USE_AS_TEMPLATE,
            ObjectAction.SET_AS_DEFAULT,
            ObjectAction.DELETE_FILES -> throw IllegalStateException("$action is unsupported")
            ObjectAction.DOWNLOAD_FILE -> {
                //do nothing
            }
        }
    }

    override fun onResetToDefaultLayout(
        ctx: Id,
        space: Id
    ) {
        showLayoutConflictScreen.value = false

        val state = objectState.value.dataViewState() ?: return

        val currentObject = state.details.getObject(ctx)
        val featuredRelations = currentObject?.featuredRelations ?: emptyList()

        viewModelScope.launch {
            val featuredWithoutConflict = featuredRelations.filter { key -> key == Relations.DESCRIPTION }
            val params = SetObjectDetails.Params(
                ctx = ctx,
                details = mapOf(Relations.FEATURED_RELATIONS to featuredWithoutConflict)
            )
            setObjectDetails.async(params).fold(
                onSuccess = {
                    dispatcher.send(it)
                },
                onFailure = {
                    Timber.e(it, "Error while resetting layout to default")
                }
            )
        }

        viewModelScope.launch {
            sendAnalyticsResolveObjectConflict(
                analytics = analytics,
                spaceParams = provideParams(space)
            )
        }
    }
}