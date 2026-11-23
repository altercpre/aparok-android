package io.aparok.app.presentation.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.domain.block.interactor.CreateBlock
import io.aparok.app.domain.block.interactor.UpdateText
import io.aparok.app.domain.collections.AddObjectToCollection
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.collections.RemoveObjectFromCollection
import io.aparok.app.domain.cover.SetDocCoverImage
import io.aparok.app.domain.dataview.SetDataViewProperties
import io.aparok.app.domain.dataview.interactor.SetDataViewObjectOrder
import io.aparok.app.domain.search.BoardGroupSubscriptionContainer
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.search.BoardRecordsSubscriptionContainer
import io.aparok.app.domain.discussions.AddDiscussion
import io.aparok.app.domain.dataview.interactor.CreateDataViewObject
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.`object`.ConvertObjectToCollection
import io.aparok.app.domain.`object`.DuplicateObjects
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.objects.SetObjectListIsArchived
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.page.CloseObject
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.search.DataViewSubscriptionContainer
import io.aparok.app.domain.sets.OpenObjectSet
import io.aparok.app.domain.sets.SetQueryToObjectSet
import io.aparok.app.domain.templates.CreateTemplate
import io.aparok.app.domain.unsplash.DownloadUnsplashImage
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.emojifier.data.EmojiProvider
import io.aparok.app.emojifier.suggest.EmojiSuggester
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.navigation.backstack.BackHistoryDelegate
import io.aparok.app.presentation.sets.state.ObjectStateReducer
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.presentation.sets.subscription.DataViewSubscription
import io.aparok.app.presentation.sets.viewer.ViewerDelegate
import io.aparok.app.presentation.templates.ObjectTypeTemplatesContainer
import io.aparok.app.presentation.util.Dispatcher

class ObjectSetViewModelFactory(
    private val params: ObjectSetViewModel.Params,
    private val permissions: UserPermissionProvider,
    private val openObjectSet: OpenObjectSet,
    private val closeObject: CloseObject,
    private val setObjectDetails: UpdateDetail,
    private val createDataViewObject: CreateDataViewObject,
    private val downloadUnsplashImage: DownloadUnsplashImage,
    private val setDocCoverImage: SetDocCoverImage,
    private val updateText: UpdateText,
    private val createBlock: CreateBlock,
    private val interceptEvents: InterceptEvents,
    private val dispatcher: Dispatcher<Payload>,
    private val delegator: Delegator<Action>,
    private val coverImageHashProvider: CoverImageHashProvider,
    private val urlBuilder: UrlBuilder,
    private val session: ObjectSetSession,
    private val analytics: Analytics,
    private val createObject: CreateObject,
    private val dataViewSubscriptionContainer: DataViewSubscriptionContainer,
    private val setQueryToObjectSet: SetQueryToObjectSet,
    private val database: ObjectSetDatabase,
    private val paginator: ObjectSetPaginator,
    private val storeOfRelations: StoreOfRelations,
    private val objectStateReducer: ObjectStateReducer,
    private val dataViewSubscription: DataViewSubscription,
    private val objectStore: ObjectStore,
    private val addObjectToCollection: AddObjectToCollection,
    private val objectToCollection: ConvertObjectToCollection,
    private val removeObjectFromCollection: RemoveObjectFromCollection,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val duplicateObjects: DuplicateObjects,
    private val templatesContainer: ObjectTypeTemplatesContainer,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val createTemplate: CreateTemplate,
    private val viewerDelegate: ViewerDelegate,
    private val spaceManager: SpaceManager,
    private val dateProvider: DateProvider,
    private val spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val fieldParser: FieldParser,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val deepLinkResolver: DeepLinkResolver,
    private val setDataViewProperties: SetDataViewProperties,
    private val setDataViewObjectOrder: SetDataViewObjectOrder,
    private val boardGroupSubscriptionContainer: BoardGroupSubscriptionContainer,
    private val boardRecordsSubscriptionContainer: BoardRecordsSubscriptionContainer,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val emojiProvider: EmojiProvider,
    private val emojiSuggester: EmojiSuggester,
    private val stringResourceProvider: StringResourceProvider,
    private val getDefaultObjectType: GetDefaultObjectType,
    private val addDiscussion: AddDiscussion,
    private val userSettingsRepository: UserSettingsRepository,
    private val backHistoryDelegate: BackHistoryDelegate,
    private val exitToVaultDelegate: ExitToVaultDelegate
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ObjectSetViewModel(
            vmParams = params,
            permissions = permissions,
            openObjectSet = openObjectSet,
            closeObject = closeObject,
            setObjectDetails = setObjectDetails,
            createDataViewObject = createDataViewObject,
            setDocCoverImage = setDocCoverImage,
            downloadUnsplashImage = downloadUnsplashImage,
            updateText = updateText,
            interceptEvents = interceptEvents,
            dispatcher = dispatcher,
            delegator = delegator,
            coverImageHashProvider = coverImageHashProvider,
            urlBuilder = urlBuilder,
            session = session,
            analytics = analytics,
            createObject = createObject,
            dataViewSubscriptionContainer = dataViewSubscriptionContainer,
            setQueryToObjectSet = setQueryToObjectSet,
            database = database,
            paginator = paginator,
            storeOfRelations = storeOfRelations,
            stateReducer = objectStateReducer,
            dataViewSubscription = dataViewSubscription,
            objectStore = objectStore,
            addObjectToCollection = addObjectToCollection,
            objectToCollection = objectToCollection,
            storeOfObjectTypes = storeOfObjectTypes,
            duplicateObjects = duplicateObjects,
            templatesContainer = templatesContainer,
            setObjectListIsArchived = setObjectListIsArchived,
            viewerDelegate = viewerDelegate,
            spaceManager = spaceManager,
            createTemplate = createTemplate,
            dateProvider = dateProvider,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
            fieldParser = fieldParser,
            spaceViews = spaceViews,
            deepLinkResolver = deepLinkResolver,
            removeObjectFromCollection = removeObjectFromCollection,
            setDataViewProperties = setDataViewProperties,
            setDataViewObjectOrder = setDataViewObjectOrder,
            boardGroupSubscriptionContainer = boardGroupSubscriptionContainer,
            boardRecordsSubscriptionContainer = boardRecordsSubscriptionContainer,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            emojiProvider = emojiProvider,
            emojiSuggester = emojiSuggester,
            createBlock = createBlock,
            stringResourceProvider = stringResourceProvider,
            getDefaultObjectType = getDefaultObjectType,
            addDiscussion = addDiscussion,
            userSettingsRepository = userSettingsRepository,
            backHistoryDelegate = backHistoryDelegate,
            exitToVaultDelegate = exitToVaultDelegate
        ) as T
    }
}