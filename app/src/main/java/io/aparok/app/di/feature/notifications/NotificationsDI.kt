package io.aparok.app.di.feature.notifications

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.notifications.NotificationsProvider
import io.aparok.app.presentation.notifications.NotificationsViewModelFactory
import io.aparok.app.ui.notifications.NotificationsFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [NotificationDependencies::class],
    modules = [
        NotificationsModule::class,
        NotificationsModule.Declarations::class
    ]
)
@PerDialog
interface NotificationComponent {
    @Component.Factory
    interface Factory {
        fun create(dependency: NotificationDependencies): NotificationComponent
    }

    fun inject(fragment: NotificationsFragment)
}

@Module
object NotificationsModule {
    @Module
    interface Declarations {
        @PerDialog
        @Binds
        fun factory(factory: NotificationsViewModelFactory): ViewModelProvider.Factory
    }
}

interface NotificationDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun analytics(): Analytics
    fun notificationsProvider(): NotificationsProvider
    fun spaceManager(): SpaceManager
    fun userSettingsRepository(): UserSettingsRepository
    fun provideDispatchers(): AppCoroutineDispatchers
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
}