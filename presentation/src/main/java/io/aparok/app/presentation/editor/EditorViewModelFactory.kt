package io.aparok.app.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.domain.ai.TypeSuggestionEngine
import io.aparok.app.domain.auth.interactor.ClearLastOpenedObject
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.interactor.RemoveLinkMark
import io.aparok.app.domain.block.interactor.SetObjectType
import io.aparok.app.domain.block.interactor.UpdateLinkMarks
import io.aparok.app.domain.block.interactor.sets.CreateObjectSet
import io.aparok.app.domain.block.interactor.sets.GetObjectTypes
import io.aparok.app.domain.cover.SetDocCoverImage
import io.aparok.app.domain.chats.GetChatMessages
import io.aparok.app.domain.discussions.AddDiscussion
import io.aparok.app.domain.event.interactor.InterceptEvents
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.icon.SetDocumentImageIcon
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.networkmode.GetNetworkMode
import io.aparok.app.domain.`object`.ConvertObjectToCollection
import io.aparok.app.domain.`object`.ConvertObjectToSet
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.page.CloseObject
import io.aparok.app.domain.page.CreateBlockLinkWithObject
import io.aparok.app.domain.page.CreateObject
import io.aparok.app.domain.page.CreateObjectAsMentionOrLink
import io.aparok.app.domain.page.OpenPage
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.sets.FindObjectSetForType
import io.aparok.app.domain.templates.ApplyTemplate
import io.aparok.app.domain.unsplash.DownloadUnsplashImage
import io.aparok.app.domain.workspace.InterceptFileLimitEvents
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.Action
import io.aparok.app.presentation.common.Delegator
import io.aparok.app.presentation.common.StateReducer
import io.aparok.app.presentation.editor.editor.Orchestrator
import io.aparok.app.presentation.editor.editor.table.EditorTableDelegate
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import io.aparok.app.presentation.navigation.backstack.BackHistoryDelegate
import io.aparok.app.presentation.vault.ExitToVaultDelegate
import io.aparok.app.presentation.templates.ObjectTypeTemplatesContainer
import io.aparok.app.presentation.util.CopyFileToCacheDirectory
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.util.UrlHelper
import javax.inject.Inject

open class EditorViewModelFactory @Inject constructor(
    private val params: EditorViewModel.Params,
    private val permissions: UserPermissionProvider,
    private val openPage: OpenPage,
    private val closeObject: CloseObject,
    private val createObjectSet: CreateObjectSet,
    private val createBlockLinkWithObject: CreateBlockLinkWithObject,
    private val createObjectAsMentionOrLink: CreateObjectAsMentionOrLink,
    private val interceptEvents: InterceptEvents,
    private val updateLinkMarks: UpdateLinkMarks,
    private val removeLinkMark: RemoveLinkMark,
    private val documentEventReducer: StateReducer<List<Block>, Event>,
    private val urlBuilder: UrlBuilder,
    private val renderer: DefaultBlockViewRenderer,
    private val orchestrator: Orchestrator,
    private val analytics: Analytics,
    private val dispatcher: Dispatcher<Payload>,
    private val delegator: Delegator<Action>,
    private val updateDetail: UpdateDetail,
    private val searchObjects: SearchObjects,
    private val getDefaultObjectType: GetDefaultObjectType,
    private val findObjectSetForType: FindObjectSetForType,
    private val copyFileToCacheDirectory: CopyFileToCacheDirectory,
    private val downloadUnsplashImage: DownloadUnsplashImage,
    private val setDocCoverImage: SetDocCoverImage,
    private val setDocImageIcon: SetDocumentImageIcon,
    private val createObject: CreateObject,
    private val objectToSet: ConvertObjectToSet,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val featureToggles: FeatureToggles,
    private val tableDelegate: EditorTableDelegate,
    private val spaceManager: SpaceManager,
    private val getObjectTypes: GetObjectTypes,
    private val objectToCollection: ConvertObjectToCollection,
    private val interceptFileLimitEvents: InterceptFileLimitEvents,
    private val addRelationToObject: AddRelationToObject,
    private val applyTemplate: ApplyTemplate,
    private val setObjectType: SetObjectType,
    private val templatesContainer: ObjectTypeTemplatesContainer,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val dispatchers: AppCoroutineDispatchers,
    private val getNetworkMode: GetNetworkMode,
    private val clearLastOpenedObject: ClearLastOpenedObject,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val syncStatusProvider: SpaceSyncAndP2PStatusProvider,
    private val fieldParser : FieldParser,
    private val dateProvider: DateProvider,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val urlHelper: UrlHelper,
    private val addDiscussion: AddDiscussion,
    private val getChatMessages: GetChatMessages,
    private val backHistoryDelegate: BackHistoryDelegate,
    private val exitToVaultDelegate: ExitToVaultDelegate,
    private val typeSuggestionEngine: TypeSuggestionEngine = TypeSuggestionEngine.NoOp
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditorViewModel(
            vmParams = params,
            permissions = permissions,
            openPage = openPage,
            closePage = closeObject,
            createBlockLinkWithObject = createBlockLinkWithObject,
            createObjectAsMentionOrLink = createObjectAsMentionOrLink,
            interceptEvents = interceptEvents,
            updateLinkMarks = updateLinkMarks,
            removeLinkMark = removeLinkMark,
            reducer = documentEventReducer,
            urlBuilder = urlBuilder,
            renderer = renderer,
            orchestrator = orchestrator,
            analytics = analytics,
            dispatcher = dispatcher,
            delegator = delegator,
            updateDetail = updateDetail,
            searchObjects = searchObjects,
            getDefaultObjectType = getDefaultObjectType,
            findObjectSetForType = findObjectSetForType,
            createObjectSet = createObjectSet,
            copyFileToCache = copyFileToCacheDirectory,
            downloadUnsplashImage = downloadUnsplashImage,
            setDocCoverImage = setDocCoverImage,
            setDocImageIcon = setDocImageIcon,
            createObject = createObject,
            objectToSet = objectToSet,
            objectToCollection = objectToCollection,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            featureToggles = featureToggles,
            tableDelegate = tableDelegate,
            spaceManager = spaceManager,
            getObjectTypes = getObjectTypes,
            interceptFileLimitEvents = interceptFileLimitEvents,
            addRelationToObject = addRelationToObject,
            applyTemplate = applyTemplate,
            setObjectType = setObjectType,
            templatesContainer = templatesContainer,
            dispatchers = dispatchers,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            spaceSyncAndP2PStatusProvider = syncStatusProvider,
            getNetworkMode = getNetworkMode,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            clearLastOpenedObject = clearLastOpenedObject,
            fieldParser = fieldParser,
            dateProvider = dateProvider,
            spaceViews = spaceViews,
            urlHelper = urlHelper,
            addDiscussion = addDiscussion,
            getChatMessages = getChatMessages,
            backHistoryDelegate = backHistoryDelegate,
            exitToVaultDelegate = exitToVaultDelegate,
            typeSuggestionEngine = typeSuggestionEngine
        ) as T
    }
}
