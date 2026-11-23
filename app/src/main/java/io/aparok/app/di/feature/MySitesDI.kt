package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.date.DateFormatter
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.publishtoweb.MySitesViewModel
import io.aparok.app.ui.publishtoweb.MySitesFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [MySitesDependencies::class],
    modules = [
        MySitesModule::class,
        MySitesModule.Declarations::class
    ]
)
@PerDialog
interface MySitesComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: MySitesViewModel.VmParams,
            dependency: MySitesDependencies
        ): MySitesComponent
    }

    fun inject(fragment: MySitesFragment)
}

@Module
object MySitesModule {

    @Module
    interface Declarations {
        @PerDialog
        @Binds
        fun factory(factory: MySitesViewModel.Factory): ViewModelProvider.Factory

        @PerDialog
        @Binds
        fun dateFormatter(formatter: DateFormatter.Basic): DateFormatter
    }
}

interface MySitesDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun auth(): AuthRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun spaceManager(): SpaceManager
}