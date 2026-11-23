package io.aparok.app.di.feature.quickcapture

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.ai.TypeSuggestionEngine
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import io.aparok.app.domain.library.StorelessSubscriptionContainer
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.multiplayer.ParticipantSubscriptionContainer
import io.aparok.app.domain.multiplayer.UserPermissionProvider
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.ui.quickcapture.QuickCaptureFragment
import dagger.Component
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope
import io.aparok.app.di.main.ConfigModule

@Component(
    dependencies = [QuickCaptureDependencies::class]
)
@PerScreen
interface QuickCaptureComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: QuickCaptureDependencies): QuickCaptureComponent
    }

    fun inject(fragment: QuickCaptureFragment)
}

interface QuickCaptureDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun userSettingsRepository(): UserSettingsRepository
    fun spaceManager(): SpaceManager
    fun userPermissionProvider(): UserPermissionProvider
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun provideStorelessSubscriptionContainer(): StorelessSubscriptionContainer
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun spaceSyncAndP2PStatusProvider(): SpaceSyncAndP2PStatusProvider
    fun typeSuggestionEngine(): TypeSuggestionEngine
    fun participantSubscriptionContainer(): ParticipantSubscriptionContainer
    fun authRepository(): AuthRepository
    @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE)
    fun applicationCoroutineScope(): CoroutineScope
    fun logger(): Logger
}
