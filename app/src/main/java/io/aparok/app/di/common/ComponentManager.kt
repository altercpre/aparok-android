package io.aparok.app.di.common

import io.aparok.app.BuildConfig
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.di.feature.CreateBookmarkModule
import io.aparok.app.di.feature.CreateObjectModule
import io.aparok.app.di.feature.DaggerAllContentComponent
import io.aparok.app.di.feature.DaggerAppPreferencesComponent
import io.aparok.app.di.feature.DaggerBacklinkOrAddToObjectComponent
import io.aparok.app.di.feature.DaggerCreateObjectFeatureComponent
import io.aparok.app.di.feature.DaggerCreateObjectTypeComponent
import io.aparok.app.di.feature.DaggerDateObjectComponent
import io.aparok.app.di.feature.DaggerEditTypePropertiesComponent
import io.aparok.app.di.feature.DaggerLinkToObjectComponent
import io.aparok.app.di.feature.DaggerMediaComponent
import io.aparok.app.di.feature.DaggerMoveToComponent
import io.aparok.app.di.feature.DaggerMySitesComponent
import io.aparok.app.di.feature.DaggerObjectTypeComponent
import io.aparok.app.di.feature.DaggerPublishToWebComponent
import io.aparok.app.di.feature.DaggerSpacePropertiesComponent
import io.aparok.app.di.feature.DaggerSpaceTypesComponent
import io.aparok.app.di.feature.DaggerSplashComponent
import io.aparok.app.di.feature.DebugSettingsModule
import io.aparok.app.di.feature.DefaultComponentParam
import io.aparok.app.di.feature.EditorSessionModule
import io.aparok.app.di.feature.EditorUseCaseModule
import io.aparok.app.di.feature.KeychainPhraseModule
import io.aparok.app.di.feature.LinkToObjectOrWebModule
import io.aparok.app.di.feature.MainEntryModule
import io.aparok.app.di.feature.ModifyViewerSortModule
import io.aparok.app.di.feature.ObjectAppearanceIconModule
import io.aparok.app.di.feature.ObjectAppearancePreviewLayoutModule
import io.aparok.app.di.feature.ObjectAppearanceSettingModule
import io.aparok.app.di.feature.ObjectIconPickerBaseModule
import io.aparok.app.di.feature.ObjectIconPickerModule
import io.aparok.app.di.feature.ObjectMenuModule
import io.aparok.app.di.feature.ObjectMenuModuleBase
import io.aparok.app.di.feature.ObjectRelationListModule
import io.aparok.app.di.feature.ObjectSetCreateBookmarkRecordModule
import io.aparok.app.di.feature.ObjectSetIconPickerModule
import io.aparok.app.di.feature.ObjectSetMenuModule
import io.aparok.app.di.feature.ObjectSetModule
import io.aparok.app.di.feature.ObjectSetRecordModule
import io.aparok.app.di.feature.ObjectSetSettingsModule
import io.aparok.app.di.feature.RelationDataViewDateValueModule
import io.aparok.app.di.feature.RelationDataViewTextValueModule
import io.aparok.app.di.feature.RelationDateValueModule
import io.aparok.app.di.feature.RelationTextValueModule
import io.aparok.app.di.feature.SelectCoverObjectModule
import io.aparok.app.di.feature.SelectCoverObjectSetModule
import io.aparok.app.di.feature.SelectSortRelationModule
import io.aparok.app.di.feature.TextBlockIconPickerModule
import io.aparok.app.di.feature.ViewerFilterModule
import io.aparok.app.di.feature.ViewerSortModule
import io.aparok.app.di.feature.auth.DaggerDeletedAccountComponent
import io.aparok.app.di.feature.chats.DaggerChatComponent
import io.aparok.app.di.feature.discussions.DaggerDiscussionComponent
import io.aparok.app.di.feature.chats.DaggerChatReactionComponent
import io.aparok.app.di.feature.chats.DaggerSelectChatIconComponent
import io.aparok.app.di.feature.chats.DaggerSelectChatReactionComponent
import io.aparok.app.di.feature.cover.UnsplashModule
import io.aparok.app.di.feature.gallery.DaggerGalleryInstallationComponent
import io.aparok.app.di.feature.home.DaggerHomeScreenComponent
import io.aparok.app.di.feature.multiplayer.DaggerShareSpaceComponent
import io.aparok.app.di.feature.notifications.DaggerNotificationComponent
import io.aparok.app.di.feature.notifications.DaggerPushContentComponent
import io.aparok.app.di.feature.objects.DaggerSelectObjectTypeComponent
import io.aparok.app.di.feature.onboarding.DaggerOnboardingComponent
import io.aparok.app.di.feature.onboarding.DaggerOnboardingStartComponent
import io.aparok.app.di.feature.participant.DaggerParticipantComponent
import io.aparok.app.di.feature.relations.DaggerRelationCreateFromLibraryComponent
import io.aparok.app.di.feature.relations.LimitObjectTypeModule
import io.aparok.app.di.feature.relations.RelationAddToDataViewModule
import io.aparok.app.di.feature.relations.RelationAddToObjectModule
import io.aparok.app.di.feature.relations.RelationCreateFromScratchForDataViewModule
import io.aparok.app.di.feature.relations.RelationCreateFromScratchForObjectBlockModule
import io.aparok.app.di.feature.relations.RelationCreateFromScratchForObjectModule
import io.aparok.app.di.feature.search.DaggerGlobalSearchComponent
import io.aparok.app.di.feature.search.DaggerSearchV2Component
import io.aparok.app.feature_search.presentation.SearchViewModel
import io.aparok.app.di.feature.sets.CreateFilterModule
import io.aparok.app.di.feature.sets.ModifyFilterModule
import io.aparok.app.di.feature.sets.PickConditionModule
import io.aparok.app.di.feature.sets.SelectFilterRelationModule
import io.aparok.app.di.feature.quickcapture.DaggerQuickCaptureComponent
import io.aparok.app.di.feature.settings.DaggerAboutAppComponent
import io.aparok.app.di.feature.settings.DaggerAppearanceComponent
import io.aparok.app.di.feature.settings.DaggerDebugComponent
import io.aparok.app.di.feature.settings.DaggerExperimentalFeaturesComponent
import io.aparok.app.di.feature.settings.DaggerFilesStorageComponent
import io.aparok.app.di.feature.settings.DaggerSpacesStorageComponent
import io.aparok.app.di.feature.settings.LogoutWarningModule
import io.aparok.app.di.feature.settings.ProfileModule
import io.aparok.app.di.feature.sharing.DaggerSharingComponent
import io.aparok.app.di.feature.spaces.DaggerCreateSpaceComponent
import io.aparok.app.di.feature.spaces.DaggerManageSectionsComponent
import io.aparok.app.di.feature.spaces.DaggerSpaceListComponent
import io.aparok.app.di.feature.spaces.DaggerSpaceSettingsComponent
import io.aparok.app.di.feature.templates.DaggerTemplateBlankComponent
import io.aparok.app.di.feature.templates.DaggerTemplateSelectComponent
import io.aparok.app.di.feature.vault.DaggerVaultComponent
import io.aparok.app.di.feature.widgets.DaggerCreateChatObjectComponent
import io.aparok.app.di.feature.oswidgets.DaggerCreateObjectWidgetConfigComponent
import io.aparok.app.di.feature.oswidgets.DaggerDataViewWidgetConfigComponent
import io.aparok.app.di.feature.oswidgets.DaggerObjectShortcutWidgetConfigComponent
import io.aparok.app.di.feature.widgets.DaggerSelectWidgetSourceComponent
import io.aparok.app.di.feature.widgets.DaggerSelectWidgetTypeComponent
import io.aparok.app.di.main.MainComponent
import io.aparok.app.feature_allcontent.presentation.AllContentViewModel
import io.aparok.app.feature_chats.presentation.ChatReactionViewModel
import io.aparok.app.feature_chats.presentation.ChatViewModel
import io.aparok.app.feature_discussions.presentation.DiscussionViewModel
import io.aparok.app.feature_chats.presentation.SelectChatReactionViewModel
import io.aparok.app.feature_create_object.presentation.NewCreateObjectViewModel
import io.aparok.app.feature_date.viewmodel.DateObjectVmParams
import io.aparok.app.feature_object_type.ui.ObjectTypeVmParams
import io.aparok.app.feature_object_type.viewmodel.CreateTypeVmParams
import io.aparok.app.feature_properties.add.EditTypePropertiesVmParams
import io.aparok.app.feature_properties.space.SpacePropertiesViewModel
import io.aparok.app.gallery_experience.viewmodel.GalleryInstallationViewModel
import io.aparok.app.presentation.editor.EditorViewModel
import io.aparok.app.presentation.history.VersionHistoryViewModel
import io.aparok.app.presentation.home.HomeScreenVmParams
import io.aparok.app.presentation.linking.LinkToObjectOrWebViewModel
import io.aparok.app.presentation.moving.MoveToViewModel
import io.aparok.app.presentation.objects.ObjectTypeChangeViewModel
import io.aparok.app.presentation.objects.SelectObjectTypeViewModel
import io.aparok.app.presentation.profile.ParticipantViewModel
import io.aparok.app.presentation.multiplayer.ShareSpaceViewModel
import io.aparok.app.presentation.publishtoweb.MySitesViewModel
import io.aparok.app.presentation.publishtoweb.PublishToWebViewModel
import io.aparok.app.presentation.relations.RelationAddViewModelBase
import io.aparok.app.presentation.relations.RelationListViewModel
import io.aparok.app.presentation.relations.option.CreateOrEditOptionViewModel
import io.aparok.app.presentation.relations.value.`object`.ObjectValueViewModel
import io.aparok.app.presentation.relations.value.tagstatus.TagOrStatusValueViewModel
import io.aparok.app.presentation.search.GlobalSearchViewModel
import io.aparok.app.presentation.search.ObjectSearchViewModel
import io.aparok.app.presentation.sets.ObjectSetViewModel
import io.aparok.app.presentation.settings.SpacesStorageViewModel
import io.aparok.app.presentation.spaces.CreateSpaceViewModel
import io.aparok.app.presentation.spaces.SpaceSettingsViewModel
import io.aparok.app.presentation.types.SpaceTypesViewModel
import io.aparok.app.presentation.widgets.CreateChatObjectViewModel
import io.aparok.app.presentation.widgets.collection.CollectionViewModel
import io.aparok.app.ui.widgets.collection.DaggerCollectionComponent
import timber.log.Timber

class ComponentManager(
    private val main: MainComponent,
    private val provider: HasComponentDependencies
) {

    val mainEntryComponent = Component {
        main.mainEntryComponentBuilder().module(MainEntryModule).build()
    }

    val debugSettingsComponent = Component {
        main
            .debugSettingsBuilder()
            .module(DebugSettingsModule())
            .build()
    }

    val debugComponent = Component {
        DaggerDebugComponent
            .factory()
            .create(findComponentDependencies())
    }

    val experimentalFeaturesComponent = Component {
        DaggerExperimentalFeaturesComponent
            .factory()
            .create(findComponentDependencies())
    }

    val quickCaptureComponent = Component {
        DaggerQuickCaptureComponent
            .factory()
            .create(findComponentDependencies())
    }

    val splashLoginComponent = Component {
       DaggerSplashComponent
           .factory()
           .create(findComponentDependencies())
    }

    val keychainPhraseComponent = Component {
        main
            .keychainPhraseComponentBuilder()
            .keychainPhraseModule(KeychainPhraseModule)
            .build()
    }

    val homeScreenComponent = ComponentWithParams { vmParams: HomeScreenVmParams ->
        DaggerHomeScreenComponent
            .factory()
            .create(
                vmParams = vmParams, dependencies = findComponentDependencies()
            )
    }

    /**
     * Single-slot cache for the widget overlay. Each fragment open fires get(),
     * each fragment destroy fires release() which clears the slot so the next
     * open builds a fresh DaggerHomeScreenComponent.
     *
     * IMPORTANT: the overlay must NOT be opened while [WidgetsScreenFragment]
     * is alive for the same space. Both fragments use HomeScreenViewModel,
     * and HomeScreenViewModel.onCleared() calls closeObject against the shared
     * widget object. Running both simultaneously causes the first cleared VM
     * to close the widget object the other VM is still subscribed to.
     *
     * In practice the overlay is only triggered from Chat/Editor/ObjectSet
     * destinations where the root widgets screen is not in the back stack,
     * so this invariant is preserved structurally — but any future
     * "show overlay from the widgets screen itself" path must be prevented.
     */
    val widgetOverlayComponent = ComponentWithParams { vmParams: HomeScreenVmParams ->
        DaggerHomeScreenComponent
            .factory()
            .create(
                vmParams = vmParams, dependencies = findComponentDependencies()
            )
    }

    val collectionComponent = ComponentWithParams { vmParams: CollectionViewModel.VmParams ->
        DaggerCollectionComponent
            .factory()
            .create(
                dependencies = findComponentDependencies(),
                vmParams = vmParams
            )
    }

    val selectWidgetSourceSubcomponent =
        ComponentWithParams { params: ObjectSearchViewModel.VmParams ->
            DaggerSelectWidgetSourceComponent.factory()
                .create(params, findComponentDependencies())
        }

    val selectWidgetTypeSubcomponent = Component {
        DaggerSelectWidgetTypeComponent.factory().create(
            findComponentDependencies()
        )
    }

    val createObjectComponent = Component {
        main
            .createObjectComponent()
            .module(CreateObjectModule)
            .build()
    }

    val templateSelectComponent = Component {
        DaggerTemplateSelectComponent.factory().create(findComponentDependencies())
    }

    val editorComponent = ComponentMapWithParam { param: DefaultComponentParam ->
        main
            .editorComponentBuilder()
            .session(EditorSessionModule)
            .usecase(EditorUseCaseModule)
            .withParams(
                EditorViewModel.Params(
                    ctx = param.ctx,
                    space = param.space
                )
            )
            .build()
    }

    val objectIconPickerComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectIconPickerComponent()
            .base(ObjectIconPickerBaseModule)
            .module(ObjectIconPickerModule)
            .build()
    }

    val textBlockIconPickerComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .textBlockIconPickerComponent()
            .module(TextBlockIconPickerModule)
            .build()
    }

    val objectSetIconPickerComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetIconPickerComponent()
            .base(ObjectIconPickerBaseModule)
            .module(ObjectSetIconPickerModule)
            .build()
    }

    val objectAppearanceSettingComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectAppearanceSettingComponent()
            .module(ObjectAppearanceSettingModule)
            .build()
    }

    val objectAppearanceIconComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectAppearanceIconComponent()
            .module(ObjectAppearanceIconModule)
            .build()
    }

    val objectAppearancePreviewLayoutComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectAppearancePreviewLayoutComponent()
            .module(ObjectAppearancePreviewLayoutModule)
            .build()
    }

    val objectAppearanceChooseDescriptionComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectAppearanceChooseDescription()
            .build()
    }

    val setTextBlockValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .setBlockTextValueComponent()
            .build()
    }

    val createBookmarkSubComponent = Component {
        main
            .createBookmarkBuilder()
            .createBookmarkModule(CreateBookmarkModule())
            .build()
    }


    val linkToObjectComponent = ComponentWithParams { param: ObjectSearchViewModel.VmParams ->
        DaggerLinkToObjectComponent
            .factory()
            .create(
                params = param,
                dependencies = findComponentDependencies()
            )
    }

    val linkToObjectOrWebComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .linkToObjectOrWebBuilder()
            .withParams(
                params = LinkToObjectOrWebViewModel.VmParams(
                    space = param.space
                )
            )
            .module(LinkToObjectOrWebModule)
            .build()
    }

    val moveToComponent = ComponentWithParams { params: MoveToViewModel.VmParams ->
        DaggerMoveToComponent.factory()
            .create(params, findComponentDependencies())
    }

    val globalSearchComponent = ComponentWithParams { params: GlobalSearchViewModel.VmParams ->
        DaggerGlobalSearchComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val searchV2Component = ComponentWithParams { params: SearchViewModel.VmParams ->
        DaggerSearchV2Component
            .factory()
            .create(params, findComponentDependencies())
    }

    val allContentComponent = ComponentWithParams { params: AllContentViewModel.VmParams ->
        DaggerAllContentComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val participantScreenComponent = ComponentWithParams { params: ParticipantViewModel.VmParams ->
        DaggerParticipantComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val dateObjectComponent = ComponentWithParams { params: DateObjectVmParams  ->
        DaggerDateObjectComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val createObjectFeatureComponent = ComponentMapWithParam { params: NewCreateObjectViewModel.VmParams ->
        DaggerCreateObjectFeatureComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val objectSetComponent = ComponentMapWithParam { param: DefaultComponentParam ->
        main.objectSetComponentBuilder()
            .module(ObjectSetModule)
            .withParams(
                ObjectSetViewModel.Params(
                    ctx = param.ctx,
                    space = param.space
                )
            )
            .build()
    }

    val objectRelationListComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectRelationListComponent()
            .withVmParams(
                RelationListViewModel.VmParams(
                    objectId = param.ctx,
                    spaceId = param.space
                )
            )
            .module(ObjectRelationListModule)
            .build()
    }

    val objectSetRelationListComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectRelationListComponent()
            .withVmParams(
                RelationListViewModel.VmParams(
                    objectId = param.ctx,
                    spaceId = param.space
                )
            )
            .module(ObjectRelationListModule)
            .build()
    }

    val setOrCollectionRelationTextValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationTextValueComponent()
            .module(RelationTextValueModule)
            .build()
    }

    val dataViewRelationTextValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationDataViewTextValueComponent()
            .module(RelationDataViewTextValueModule)
            .build()
    }

    val relationTextValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .relationTextValueComponent()
            .module(RelationTextValueModule)
            .build()
    }

    val dataViewRelationDateValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .dataViewRelationDateValueComponent()
            .module(RelationDataViewDateValueModule)
            .build()
    }

    val setOrCollectionRelationDateValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationDateValueComponent()
            .module(RelationDateValueModule)
            .build()
    }

    val objectRelationDateValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .editRelationDateComponent()
            .module(RelationDateValueModule)
            .build()
    }

    val viewerFilterComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .viewerFilterBySubComponent()
            .module(ViewerFilterModule)
            .build()
    }

    val objectSetRecordComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetRecordComponent()
            .module(ObjectSetRecordModule)
            .build()
    }

    val objectSetCreateBookmarkRecordComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetCreateBookmarkRecordComponent()
            .module(ObjectSetCreateBookmarkRecordModule)
            .build()
    }

    val objectsSetSettingsComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetSettingsComponent()
            .module(ObjectSetSettingsModule)
            .build()
    }

    val selectSortRelationComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .selectSortRelationComponent()
            .module(SelectSortRelationModule)
            .build()
    }

    val selectFilterRelationComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .selectFilterRelationComponent()
            .module(SelectFilterRelationModule)
            .build()
    }

    val createFilterComponent = DependentComponentMap { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .createFilterComponent()
            .module(CreateFilterModule)
            .build()
    }

    val pickFilterConditionComponentCreate = ComponentWithParams { param: DefaultComponentParam ->
        createFilterComponent
            .get(key = param.ctx, param = param)
            .createPickConditionComponent()
            .module(PickConditionModule)
            .build()
    }

    val pickFilterConditionComponentModify = ComponentWithParams { param: DefaultComponentParam ->
        modifyFilterComponent
            .get(key = param.ctx, param = param)
            .createPickConditionComponent()
            .module(PickConditionModule)
            .build()
    }

    val modifyFilterComponent = DependentComponentMap { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .modifyFilterComponent()
            .module(ModifyFilterModule)
            .build()
    }

    val viewerSortComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .viewerSortComponent()
            .module(ViewerSortModule)
            .build()
    }

    val modifyViewerSortComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .modifyViewerSortComponent()
            .module(ModifyViewerSortModule)
            .build()
    }

    val objectCoverComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectCoverComponent()
            .module(SelectCoverObjectModule)
            .build()
    }

    val objectUnsplashComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectUnsplashComponent()
            .module(UnsplashModule)
            .build()
    }

    val objectSetUnsplashComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectUnsplashComponent()
            .module(UnsplashModule)
            .build()
    }

    val objectSetCoverComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetCoverComponent()
            .module(SelectCoverObjectSetModule)
            .build()
    }

    val objectMenuComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectMenuComponent()
            .base(ObjectMenuModuleBase)
            .module(ObjectMenuModule)
            .build()
    }

    val objectSetMenuComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetMenuComponent()
            .base(ObjectMenuModuleBase)
            .module(ObjectSetMenuModule)
            .build()
    }

    val relationAddToObjectComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .relationAddToObjectComponent()
            .withVmParams(
                vmParams = RelationAddViewModelBase.VmParams(
                    objectId = param.ctx,
                    space = param.space
                )
            )
            .module(RelationAddToObjectModule)
            .build()
    }

    val relationAddToObjectSetComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationAddToObjectComponent()
            .withVmParams(
                vmParams = RelationAddViewModelBase.VmParams(
                    objectId = param.ctx,
                    space = param.space
                )
            )
            .module(RelationAddToObjectModule)
            .build()
    }

    val relationAddToDataViewComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationAddToDataViewComponent()
            .withVmParams(
                vmParams = RelationAddViewModelBase.VmParams(
                    objectId = param.ctx,
                    space = param.space
                )
            )
            .module(RelationAddToDataViewModule)
            .build()
    }

    val relationCreateFromScratchForObjectComponent = DependentComponentMap { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .relationCreateFromScratchForObjectComponent()
            .module(RelationCreateFromScratchForObjectModule)
            .build()
    }

    val relationCreateFromScratchForObjectSetComponent = DependentComponentMap { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationCreateFromScratchForObjectComponent()
            .module(RelationCreateFromScratchForObjectModule)
            .build()
    }

    val relationCreateFromScratchForObjectBlockComponent = DependentComponentMap { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .relationCreateFromScratchForObjectBlockComponent()
            .module(RelationCreateFromScratchForObjectBlockModule)
            .build()
    }

    val relationCreateFromScratchForDataViewComponent = DependentComponentMap { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationCreateFromScratchForDataViewComponent()
            .module(RelationCreateFromScratchForDataViewModule)
            .build()
    }

    val relationFormatPickerObjectComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectComponent
            .get(key = param.ctx, param = param)
            .relationFormatPickerComponent()
            .build()
    }

    val relationFormatPickerLibraryComponent = ComponentWithParams { ctx: Id ->
        relationCreationFromLibraryComponent
            .get()
            .relationFormatPickerComponent()
            .build()
    }

    val relationFormatPickerBlockComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectBlockComponent
            .get(key = param.ctx, param = param)
            .relationFormatPickerComponent()
            .build()
    }

    val relationFormatPickerDataViewComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForDataViewComponent
            .get(key = param.ctx, param = param)
            .relationFormatPickerComponent()
            .build()
    }

    val relationFormatPickerSetOrCollectionComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectSetComponent
            .get(key = param.ctx, param = param)
            .relationFormatPickerComponent()
            .build()
    }

    val limitObjectTypeObjectComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectComponent
            .get(key = param.ctx, param = param)
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val limitObjectTypeBlockComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectBlockComponent
            .get(key = param.ctx, param = param)
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val limitObjectTypeDataViewComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForDataViewComponent
            .get(key = param.ctx, param = param)
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val limitObjectTypeSetOrCollectionComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectSetComponent
            .get(key = param.ctx, param = param)
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val limitObjectTypeLibraryComponent = ComponentWithParams { ctx: Id ->
        relationCreationFromLibraryComponent.get()
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val objectTypeChangeComponent = ComponentWithParams { vmParams: ObjectTypeChangeViewModel.VmParams ->
        main
            .objectTypeChangeComponent()
            .create(vmParams)
    }

    val templateBlankComponent = Component {
        DaggerTemplateBlankComponent.factory().create(findComponentDependencies())
    }

    // Settings

    val aboutAppComponent = Component {
        DaggerAboutAppComponent.factory().create(findComponentDependencies())
    }

    val profileComponent = Component {
        main.profileComponent().module(ProfileModule).build()
    }

    val logoutWarningComponent = Component {
        main.logoutWarningComponent().module(LogoutWarningModule).build()
    }

    val filesStorageComponent = Component {
        DaggerFilesStorageComponent.builder()
            .withDependencies(findComponentDependencies())
            .build()
    }

    val spacesStorageComponent = ComponentWithParams { vmParams: SpacesStorageViewModel.VmParams ->
        DaggerSpacesStorageComponent
            .factory()
            .create(
                vmParams = vmParams,
                dependency = findComponentDependencies()
            )
    }

    val appearanceComponent = Component {
        DaggerAppearanceComponent
            .factory()
            .create(findComponentDependencies())
    }

    val backLinkOrAddToObjectComponent = ComponentWithParams { vmParams: ObjectSearchViewModel.VmParams ->
        DaggerBacklinkOrAddToObjectComponent.builder()
            .withParams(vmParams)
            .withDependencies(findComponentDependencies())
            .build()
    }

    val relationCreationFromLibraryComponent = Component {
        DaggerRelationCreateFromLibraryComponent
            .factory()
            .create(findComponentDependencies())
    }

    val deletedAccountComponent = Component {
        DaggerDeletedAccountComponent
            .factory()
            .create(findComponentDependencies())
    }

    val onboardingComponent = Component {
        DaggerOnboardingComponent
            .factory()
            .create(findComponentDependencies())
    }

    val onboardingStartComponent = Component {
        DaggerOnboardingStartComponent
            .factory()
            .create(findComponentDependencies())
    }

    val spaceListComponent = Component {
        DaggerSpaceListComponent
            .factory()
            .create(findComponentDependencies())
    }

    val createSpaceComponent = ComponentWithParams { vmParams: CreateSpaceViewModel.VmParams ->
        DaggerCreateSpaceComponent
            .factory()
            .create(vmParams, findComponentDependencies())
    }

    val spaceSettingsComponent = ComponentWithParams { vmParams: SpaceSettingsViewModel.VmParams ->
        DaggerSpaceSettingsComponent
            .factory()
            .create(
                vmParams = vmParams,
                dependencies = findComponentDependencies()
            )
    }

    val manageSectionsComponent = Component {
        DaggerManageSectionsComponent
            .factory()
            .create(findComponentDependencies())
    }

    val selectObjectTypeComponent = ComponentWithParams { params: SelectObjectTypeViewModel.Params ->
        DaggerSelectObjectTypeComponent
            .factory()
            .create(
                params = params,
                dependencies = findComponentDependencies()
            )
    }

    val appPreferencesComponent = Component {
        DaggerAppPreferencesComponent
            .factory()
            .create(findComponentDependencies())
    }

    val sharingComponent = Component {
        DaggerSharingComponent
            .factory()
            .create(findComponentDependencies())
    }

    val notificationsComponent = Component {
        DaggerNotificationComponent
            .factory()
            .create(findComponentDependencies())
    }

    val tagStatusObjectComponent = ComponentWithParams { params: TagOrStatusValueViewModel.ViewModelParams ->
        editorComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .tagStatusObjectComponent()
            .params(params)
            .build()
    }

    val tagStatusSetComponent = ComponentWithParams { params: TagOrStatusValueViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .tagStatusSetComponent()
            .params(params)
            .build()
    }

    val tagStatusDataViewComponent = ComponentWithParams { params: TagOrStatusValueViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .tagStatusDataViewComponent()
            .params(params)
            .build()
    }

    val optionObjectComponent = ComponentWithParams { params: CreateOrEditOptionViewModel.ViewModelParams ->
        editorComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .optionObjectComponent()
            .params(params)
            .build()
    }

    val optionSetComponent = ComponentWithParams { params: CreateOrEditOptionViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .optionSetComponent()
            .params(params)
            .build()
    }

    val optionDataViewComponent = ComponentWithParams { params: CreateOrEditOptionViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .optionDataViewComponent()
            .params(params)
            .build()
    }

    val objectValueObjectComponent = ComponentWithParams { params: ObjectValueViewModel.ViewModelParams ->
        editorComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .objectValueComponent()
            .params(params)
            .build()
    }

    val objectValueSetComponent = ComponentWithParams { params: ObjectValueViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .objectValueSetComponent()
            .params(params)
            .build()
    }

    val objectValueDataViewComponent = ComponentWithParams { params: ObjectValueViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .objectValueDataViewComponent()
            .params(params)
            .build()
    }

    val shareSpaceComponent = ComponentWithParams { space: SpaceId ->
        DaggerShareSpaceComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params = ShareSpaceViewModel.VmParams(space))
            .build()
    }


    val galleryInstallationsComponent =
        ComponentWithParams { params: GalleryInstallationViewModel.ViewModelParams ->
            DaggerGalleryInstallationComponent.builder()
                .withDependencies(findComponentDependencies())
                .withParams(params)
                .build()
        }

    val versionHistoryComponent =
        ComponentWithParams { vmParams: VersionHistoryViewModel.VmParams ->
            editorComponent.get(
                key = vmParams.objectId,
                param = DefaultComponentParam(
                    ctx = vmParams.objectId,
                    space = vmParams.spaceId
                )
            ).versionHistoryComponent()
                .vmParams(vmParams)
                .build()
        }

    val chatComponent = ComponentMapWithParam { params: ChatViewModel.Params.Default ->
        DaggerChatComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params)
            .build()
    }

    val discussionComponent = ComponentMapWithParam { params: DiscussionViewModel.Params ->
        DaggerDiscussionComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params)
            .build()
    }

    val selectChatReactionComponent = ComponentMapWithParam { params: SelectChatReactionViewModel.Params ->
        DaggerSelectChatReactionComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params)
            .build()
    }

    val chatReactionComponent = ComponentMapWithParam { params: ChatReactionViewModel.Params ->
        DaggerChatReactionComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params)
            .build()
    }

    val selectChatIconComponent = Component {
        DaggerSelectChatIconComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .build()
    }

    val vaultComponent = Component {
        DaggerVaultComponent
            .factory()
            .create(findComponentDependencies())
    }

    val objectTypeComponent = ComponentWithParams { params: ObjectTypeVmParams ->
        DaggerObjectTypeComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val editTypePropertiesComponent = ComponentWithParams { params: EditTypePropertiesVmParams ->
        DaggerEditTypePropertiesComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val createObjectTypeComponent = ComponentWithParams { params: CreateTypeVmParams ->
        DaggerCreateObjectTypeComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val spaceTypesComponent = ComponentWithParams { params: SpaceTypesViewModel.VmParams ->
        DaggerSpaceTypesComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val spacePropertiesComponent = ComponentWithParams { params: SpacePropertiesViewModel.VmParams ->
        DaggerSpacePropertiesComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val pushContentComponent = Component {
        DaggerPushContentComponent
            .factory()
            .create(findComponentDependencies())
    }

    val publishToWebComponent = ComponentWithParams { params: PublishToWebViewModel.Params ->
        DaggerPublishToWebComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val mySitesComponent = ComponentWithParams { params: MySitesViewModel.VmParams ->
        DaggerMySitesComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val mediaComponent = Component {
        DaggerMediaComponent
            .factory()
            .create(findComponentDependencies())
    }

    val createChatObjectComponent = ComponentWithParams { vmParams: CreateChatObjectViewModel.VmParams ->
        DaggerCreateChatObjectComponent
            .factory()
            .create(vmParams, findComponentDependencies())
    }

    val createObjectWidgetConfigComponent = Component {
        DaggerCreateObjectWidgetConfigComponent
            .factory()
            .create(findComponentDependencies())
    }

    val objectShortcutWidgetConfigComponent = Component {
        DaggerObjectShortcutWidgetConfigComponent
            .factory()
            .create(findComponentDependencies())
    }

    val dataViewWidgetConfigComponent = Component {
        DaggerDataViewWidgetConfigComponent
            .factory()
            .create(findComponentDependencies())
    }

    class Component<T>(private val builder: () -> T) {

        private var instance: T? = null

        fun get() = instance ?: builder().also { instance = it }

        fun new() = builder().also { instance = it }

        fun release() {
            instance = null
        }

        fun isInitialized() = instance != null

        override fun toString(): String {
            return if (BuildConfig.DEBUG) {
                instance?.toString().orEmpty()
            } else {
                super.toString()
            }
        }
    }

    class ComponentMap<T>(private val builder: () -> T) {

        private val map = mutableMapOf<String, T>()

        fun get(id: String) = map[id] ?: builder().also { map[id] = it }

        fun new(id: String) = builder().also { map[id] = it }

        fun release(id: String) {
            map.remove(id)
        }

        fun isInitialized() = map.isNotEmpty()

        override fun toString(): String {
            return if (BuildConfig.DEBUG) {
                map.toString()
            } else {
                super.toString()
            }
        }
    }

    class ComponentMapWithParam<out T, in PARAMETER>(private val builder: (PARAMETER) -> T) {

        private val map = mutableMapOf<String, T>()

        fun get(key: String, param: PARAMETER) = map[key] ?: builder(param).also { map[key] = it }

        fun new(id: String, param: PARAMETER) = builder(param).also { map[id] = it }

        fun release(id: String) {
            map.remove(id)
        }

        fun isInitialized() = map.isNotEmpty()

        override fun toString(): String {
            return if (BuildConfig.DEBUG) {
                map.toString()
            } else {
                super.toString()
            }
        }
    }

    class DependentComponentMap<out T, in PARAMETER>(private val builder: (PARAMETER) -> T) {

        private val map = mutableMapOf<String, T>()

        fun get(key: String, param: PARAMETER) = map[key] ?: builder(param).also { map[key] = it }

        fun new(key: String, param: PARAMETER) = builder(param).also { map[key] = it }

        fun release(id: Id) {
            map.remove(id)
        }

        fun isInitialized() = map.isNotEmpty()

        override fun toString(): String {
            return if (BuildConfig.DEBUG) {
                map.toString()
            } else {
                super.toString()
            }
        }
    }

    class ComponentWithParams<out T, in PARAMETER>(private val builder: (PARAMETER) -> T) {

        private var instance: T? = null

        fun get(params: PARAMETER) = instance ?: builder(params).also { instance = it }

        fun new(params: PARAMETER) = builder(params).also { instance = it }

        fun release() {
            instance = null
        }
    }

    private inline fun <reified T : ComponentDependencies> findComponentDependencies(): T {
        return provider.dependencies[T::class.java] as T
    }

    /**
     * Can be used for debugging and tracing unreleased components.
     */
    fun logUnreleasedComponents() {
        if (BuildConfig.DEBUG) {
            javaClass.declaredFields.forEach { field ->
                val component = field.get(this)
                if (component != null) {
                    when (component) {
                        is Component<*> -> {
                            if (component.isInitialized()) {
                                Timber.d("Unreleased component: $component")
                            }
                        }
                        is DependentComponentMap<*, *> -> {
                            if (component.isInitialized()) {
                                Timber.d("Unreleased component: $component")
                            }
                        }
                        is ComponentMap<*> -> {
                            if (component.isInitialized()) {
                                Timber.d("Unreleased component: $component")
                            }
                        }
                    }
                }
            }
        }
    }
}