package io.aparok.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.domain.auth.interactor.GetAccount
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.workspace.InterceptFileLimitEvents
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.domain.workspace.SpacesUsageInfo
import io.aparok.app.presentation.membership.provider.MembershipProvider
import javax.inject.Inject

class SpacesStorageViewModelFactory @Inject constructor(
    private val vmParams: SpacesStorageViewModel.VmParams,
    private val analytics: Analytics,
    private val spaceManager: SpaceManager,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val spacesUsageInfo: SpacesUsageInfo,
    private val interceptFileLimitEvents: InterceptFileLimitEvents,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val getAccount: GetAccount,
    private val membershipProvider: MembershipProvider,
    private val userPermissionProvider: UserPermissionProvider,
    private val configStorage: ConfigStorage
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T = SpacesStorageViewModel(
        analytics = analytics,
        spaceManager = spaceManager,
        appCoroutineDispatchers = appCoroutineDispatchers,
        spacesUsageInfo = spacesUsageInfo,
        interceptFileLimitEvents = interceptFileLimitEvents,
        storelessSubscriptionContainer = storelessSubscriptionContainer,
        getAccount = getAccount,
        membershipProvider = membershipProvider,
        userPermissionProvider = userPermissionProvider,
        configStorage = configStorage,
        vmParams = vmParams
    ) as T
}