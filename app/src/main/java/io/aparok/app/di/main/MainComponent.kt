package io.aparok.app.di.main

import io.aparok.app.app.AndroidApplication
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.di.common.ComponentDependenciesKey
import io.aparok.app.di.feature.AllContentDependencies
import io.aparok.app.di.feature.AppPreferencesDependencies
import io.aparok.app.di.feature.BacklinkOrAddToObjectDependencies
import io.aparok.app.di.feature.CreateBookmarkSubComponent
import io.aparok.app.di.feature.CreateObjectFeatureDependencies
import io.aparok.app.di.feature.CreateObjectSubComponent
import io.aparok.app.di.feature.CreateObjectTypeDependencies
import io.aparok.app.di.feature.DateObjectDependencies
import io.aparok.app.di.feature.DebugSettingsSubComponent
import io.aparok.app.di.feature.EditTypePropertiesDependencies
import io.aparok.app.di.feature.EditorSubComponent
import io.aparok.app.di.feature.KeychainPhraseSubComponent
import io.aparok.app.di.feature.LinkToObjectDependencies
import io.aparok.app.di.feature.MainEntrySubComponent
import io.aparok.app.di.feature.MediaDependencies
import io.aparok.app.di.feature.MoveToDependencies
import io.aparok.app.di.feature.MySitesDependencies
import io.aparok.app.di.feature.ObjectSetSubComponent
import io.aparok.app.di.feature.ObjectTypeChangeSubComponent
import io.aparok.app.di.feature.ObjectTypeDependencies
import io.aparok.app.di.feature.PublishToWebDependencies
import io.aparok.app.di.feature.SpacePropertiesDependencies
import io.aparok.app.di.feature.SpaceTypesDependencies
import io.aparok.app.di.feature.SplashDependencies
import io.aparok.app.di.feature.auth.DeletedAccountDependencies
import io.aparok.app.di.feature.chats.ChatComponentDependencies
import io.aparok.app.di.feature.discussions.DiscussionComponentDependencies
import io.aparok.app.di.feature.chats.ChatReactionDependencies
import io.aparok.app.di.feature.chats.SelectChatIconDependencies
import io.aparok.app.di.feature.chats.SelectChatReactionDependencies
import io.aparok.app.di.feature.gallery.GalleryInstallationComponentDependencies
import io.aparok.app.di.feature.home.HomeScreenDependencies
import io.aparok.app.di.feature.multiplayer.ShareSpaceDependencies
import io.aparok.app.di.feature.notifications.NotificationDependencies
import io.aparok.app.di.feature.notifications.PushContentDependencies
import io.aparok.app.di.feature.objects.SelectObjectTypeDependencies
import io.aparok.app.di.feature.onboarding.OnboardingDependencies
import io.aparok.app.di.feature.oswidgets.CreateObjectWidgetConfigDependencies
import io.aparok.app.di.feature.oswidgets.DataViewWidgetConfigDependencies
import io.aparok.app.di.feature.oswidgets.ObjectShortcutWidgetConfigDependencies
import io.aparok.app.di.feature.onboarding.OnboardingStartDependencies
import io.aparok.app.di.feature.participant.ParticipantComponentDependencies
import io.aparok.app.di.feature.relations.RelationCreateFromLibraryDependencies
import io.aparok.app.di.feature.search.GlobalSearchDependencies
import io.aparok.app.di.feature.search.SearchV2Dependencies
import io.aparok.app.di.feature.settings.AboutAppDependencies
import io.aparok.app.di.feature.settings.AppearanceDependencies
import io.aparok.app.di.feature.settings.DebugDependencies
import io.aparok.app.di.feature.quickcapture.QuickCaptureDependencies
import io.aparok.app.di.feature.settings.ExperimentalFeaturesDependencies
import io.aparok.app.di.feature.settings.FilesStorageDependencies
import io.aparok.app.di.feature.settings.LogoutWarningSubComponent
import io.aparok.app.di.feature.settings.ProfileSubComponent
import io.aparok.app.di.feature.settings.SpacesStorageDependencies
import io.aparok.app.di.feature.sharing.SharingDependencies
import io.aparok.app.di.feature.spaces.CreateSpaceDependencies
import io.aparok.app.di.feature.spaces.ManageSectionsDependencies
import io.aparok.app.di.feature.spaces.SpaceListDependencies
import io.aparok.app.di.feature.spaces.SpaceSettingsDependencies
import io.aparok.app.di.feature.templates.TemplateBlankDependencies
import io.aparok.app.di.feature.templates.TemplateSelectDependencies
import io.aparok.app.di.feature.vault.VaultComponentDependencies
import io.aparok.app.di.feature.widgets.CreateChatObjectDependencies
import io.aparok.app.di.feature.widgets.SelectWidgetSourceDependencies
import io.aparok.app.di.feature.widgets.SelectWidgetTypeDependencies
import io.aparok.app.domain.chats.ChatPreviewContainer
import io.aparok.app.domain.chats.ChatsDetailsSubscriptionContainer
import io.aparok.app.domain.objects.CrossSpaceObjectTypesContainer
import io.aparok.app.ui.widgets.collection.CollectionDependencies
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ComponentDependenciesModule::class,
        ContextModule::class,
        AiModule::class,
        DataModule::class,
        EventModule::class,
        ConfigModule::class,
        WorkspaceModule::class,
        DeviceModule::class,
        UtilModule::class,
        EmojiModule::class,
        ClipboardModule::class,
        AnalyticsModule::class,
        LocalNetworkProviderModule::class,
        SubscriptionsModule::class,
        CrashReportingModule::class,
        TemplatesModule::class,
        NetworkModeModule::class,
        NotificationsModule::class,
        DispatcherModule::class,
        NavigationModule::class
    ]
)
interface MainComponent :
    AppearanceDependencies,
    HomeScreenDependencies,
    CollectionDependencies,
    RelationCreateFromLibraryDependencies,
    SplashDependencies,
    DeletedAccountDependencies,
    BacklinkOrAddToObjectDependencies,
    FilesStorageDependencies,
    OnboardingDependencies,
    OnboardingStartDependencies,
    AboutAppDependencies,
    TemplateBlankDependencies,
    TemplateSelectDependencies,
    CreateSpaceDependencies,
    SpaceListDependencies,
    SpaceSettingsDependencies,
    ManageSectionsDependencies,
    SelectObjectTypeDependencies,
    SpacesStorageDependencies,
    AppPreferencesDependencies,
    ShareSpaceDependencies,
    GalleryInstallationComponentDependencies,
    NotificationDependencies,
    GlobalSearchDependencies,
    SearchV2Dependencies,
    VaultComponentDependencies,
    AllContentDependencies,
    ChatComponentDependencies,
    DiscussionComponentDependencies,
    SelectWidgetSourceDependencies,
    SelectWidgetTypeDependencies,
    LinkToObjectDependencies,
    MoveToDependencies,
    DateObjectDependencies,
    ObjectTypeDependencies,
    SelectChatReactionDependencies,
    SelectChatIconDependencies,
    ChatReactionDependencies,
    ParticipantComponentDependencies,
    EditTypePropertiesDependencies,
    DebugDependencies,
    ExperimentalFeaturesDependencies,
    QuickCaptureDependencies,
    CreateObjectTypeDependencies,
    SpaceTypesDependencies,
    SpacePropertiesDependencies,
    PushContentDependencies,
    PublishToWebDependencies,
    MySitesDependencies,
    MediaDependencies,
    CreateChatObjectDependencies,
    SharingDependencies,
    CreateObjectFeatureDependencies,
    CreateObjectWidgetConfigDependencies,
    DataViewWidgetConfigDependencies,
    ObjectShortcutWidgetConfigDependencies
{

    fun inject(app: AndroidApplication)

    fun editorComponentBuilder(): EditorSubComponent.Builder
    fun createBookmarkBuilder(): CreateBookmarkSubComponent.Builder
    fun mainEntryComponentBuilder(): MainEntrySubComponent.Builder
    fun objectSetComponentBuilder(): ObjectSetSubComponent.Builder
    fun objectTypeChangeComponent(): ObjectTypeChangeSubComponent.Factory
    fun createObjectComponent(): CreateObjectSubComponent.Builder

    //region Settings

    fun profileComponent(): ProfileSubComponent.Builder
    fun debugSettingsBuilder(): DebugSettingsSubComponent.Builder
    fun keychainPhraseComponentBuilder(): KeychainPhraseSubComponent.Builder
    fun logoutWarningComponent(): LogoutWarningSubComponent.Builder

    //endregion

    fun chatPreviewContainer(): ChatPreviewContainer
    fun chatsDetailsSubscriptionContainer(): ChatsDetailsSubscriptionContainer
    fun crossSpaceObjectTypesContainer(): CrossSpaceObjectTypesContainer
}

@Module
abstract class ComponentDependenciesModule {

    @Binds
    @IntoMap
    @ComponentDependenciesKey(AppearanceDependencies::class)
    abstract fun provideAppearanceDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(HomeScreenDependencies::class)
    abstract fun provideHomeScreenDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CollectionDependencies::class)
    abstract fun provideHomeWidgetDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(RelationCreateFromLibraryDependencies::class)
    abstract fun provideRelationCreateFromLibraryDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SplashDependencies::class)
    abstract fun provideSplashDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DeletedAccountDependencies::class)
    abstract fun provideDeletedAccountDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(BacklinkOrAddToObjectDependencies::class)
    abstract fun provideBackLinkDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(FilesStorageDependencies::class)
    abstract fun provideFilesStorageDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(OnboardingDependencies::class)
    abstract fun provideOnboardingDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(OnboardingStartDependencies::class)
    abstract fun provideOnboardingStartDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(AboutAppDependencies::class)
    abstract fun provideAboutAppDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(TemplateBlankDependencies::class)
    abstract fun provideTemplateBlankDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(TemplateSelectDependencies::class)
    abstract fun provideTemplateSelectDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpaceListDependencies::class)
    abstract fun provideSpaceListDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateSpaceDependencies::class)
    abstract fun provideCreateSpaceDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpaceSettingsDependencies::class)
    abstract fun provideSpaceSettingsDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ManageSectionsDependencies::class)
    abstract fun provideManageSectionsDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectObjectTypeDependencies::class)
    abstract fun provideCreateObjectOfTypeDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpacesStorageDependencies::class)
    abstract fun provideSpacesStorageDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(AppPreferencesDependencies::class)
    abstract fun providePreferencesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ShareSpaceDependencies::class)
    abstract fun provideShareSpaceDependencies(component: MainComponent): ComponentDependencies


    @Binds
    @IntoMap
    @ComponentDependenciesKey(GalleryInstallationComponentDependencies::class)
    abstract fun provideGalleryInstallationDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(NotificationDependencies::class)
    abstract fun provideNotificationDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(GlobalSearchDependencies::class)
    abstract fun provideGlobalSearchDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SearchV2Dependencies::class)
    abstract fun provideSearchV2Dependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ChatComponentDependencies::class)
    abstract fun provideChatComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DiscussionComponentDependencies::class)
    abstract fun provideDiscussionComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(VaultComponentDependencies::class)
    abstract fun provideVaultComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(AllContentDependencies::class)
    abstract fun provideAllContentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectWidgetSourceDependencies::class)
    abstract fun provideSelectWidgetSourceDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectWidgetTypeDependencies::class)
    abstract fun provideSelectWidgetTypeDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(LinkToObjectDependencies::class)
    abstract fun provideLinkToObjectDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(MoveToDependencies::class)
    abstract fun provideMoveToDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DateObjectDependencies::class)
    abstract fun provideDateObjectDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ObjectTypeDependencies::class)
    abstract fun provideObjectTypeDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectChatReactionDependencies::class)
    abstract fun provideChatReactionPickerDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectChatIconDependencies::class)
    abstract fun provideSelectChatIconDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ChatReactionDependencies::class)
    abstract fun provideChatReactionDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ParticipantComponentDependencies::class)
    abstract fun provideParticipantComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DebugDependencies::class)
    abstract fun provideDebugDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ExperimentalFeaturesDependencies::class)
    abstract fun provideExperimentalFeaturesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(QuickCaptureDependencies::class)
    abstract fun provideQuickCaptureDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(EditTypePropertiesDependencies::class)
    abstract fun provideEditTypePropertiesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateObjectTypeDependencies::class)
    abstract fun provideCreateObjectTypeDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpaceTypesDependencies::class)
    abstract fun provideSpaceTypesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpacePropertiesDependencies::class)
    abstract fun provideSpacePropertiesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(PushContentDependencies::class)
    abstract fun providePushContentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(PublishToWebDependencies::class)
    abstract fun providePublishToWebDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(MySitesDependencies::class)
    abstract fun mySitesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(MediaDependencies::class)
    abstract fun mediaDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateChatObjectDependencies::class)
    abstract fun createChatObjectDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SharingDependencies::class)
    abstract fun sharingDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateObjectFeatureDependencies::class)
    abstract fun provideCreateObjectFeatureDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateObjectWidgetConfigDependencies::class)
    abstract fun provideCreateObjectWidgetConfigDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ObjectShortcutWidgetConfigDependencies::class)
    abstract fun provideObjectShortcutWidgetConfigDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DataViewWidgetConfigDependencies::class)
    abstract fun provideDataViewWidgetConfigDependencies(component: MainComponent): ComponentDependencies
}
