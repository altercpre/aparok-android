package io.aparok.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.CrashReporter
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.domain.auth.interactor.CheckAuthorizationStatus
import io.aparok.app.domain.auth.interactor.GetLastOpenedObject
import io.aparok.app.domain.auth.interactor.LaunchAccount
import io.aparok.app.domain.auth.interactor.LaunchWallet
import io.aparok.app.domain.deeplink.PendingIntentStore
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.page.CreateObjectByTypeAndTemplate
import io.aparok.app.domain.spaces.GetLastOpenedSpace
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.vault.SetVaultSearchHighlightSeen
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.auth.account.MigrationHelperDelegate
import javax.inject.Inject

/**
 * Created by Konstantin Ivanov
 * email :  ki@agileburo.com
 * on 2019-10-21.
 */
class SplashViewModelFactory @Inject constructor(
    private val checkAuthorizationStatus: CheckAuthorizationStatus,
    private val launchAccount: LaunchAccount,
    private val launchWallet: LaunchWallet,
    private val analytics: Analytics,
    private val getLastOpenedObject: GetLastOpenedObject,
    private val crashReporter: CrashReporter,
    private val localeProvider: LocaleProvider,
    private val spaceManager: SpaceManager,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val globalSubscriptionManager: GlobalSubscriptionManager,
    private val getLastOpenedSpace: GetLastOpenedSpace,
    private val createObjectByTypeAndTemplate: CreateObjectByTypeAndTemplate,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val migration: MigrationHelperDelegate,
    private val deepLinkResolver: DeepLinkResolver,
    private val pendingIntentStore: PendingIntentStore,
    private val searchObjects: SearchObjects,
    private val preferredSpaceIdHolder: PreferredSpaceIdHolder,
    private val setVaultSearchHighlightSeen: SetVaultSearchHighlightSeen
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SplashViewModel(
            checkAuthorizationStatus = checkAuthorizationStatus,
            launchAccount = launchAccount,
            launchWallet = launchWallet,
            analytics = analytics,
            getLastOpenedObject = getLastOpenedObject,
            crashReporter = crashReporter,
            localeProvider = localeProvider,
            spaceManager = spaceManager,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            globalSubscriptionManager = globalSubscriptionManager,
            getLastOpenedSpace = getLastOpenedSpace,
            createObjectByTypeAndTemplate = createObjectByTypeAndTemplate,
            spaceViews = spaceViews,
            migration = migration,
            deepLinkResolver = deepLinkResolver,
            pendingIntentStore = pendingIntentStore,
            searchObjects = searchObjects,
            preferredSpaceIdHolder = preferredSpaceIdHolder,
            setVaultSearchHighlightSeen = setVaultSearchHighlightSeen
        ) as T
}
