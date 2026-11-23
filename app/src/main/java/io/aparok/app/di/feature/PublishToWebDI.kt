package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.feature_properties.space.SpacePropertiesViewModel
import io.aparok.app.presentation.publishtoweb.PublishToWebViewModel
import io.aparok.app.ui.publishtoweb.PublishToWebFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [PublishToWebDependencies::class],
    modules = [
        PublishToWebModule::class,
        PublishToWebModule.Declarations::class
    ]
)
@PerDialog
interface PublishToWebComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: PublishToWebViewModel.Params,
            dependency: PublishToWebDependencies
        ): PublishToWebComponent
    }

    fun inject(fragment: PublishToWebFragment)
}

@Module
object PublishToWebModule {

    @Module
    interface Declarations {
        @PerDialog
        @Binds
        fun factory(factory: PublishToWebViewModel.Factory): ViewModelProvider.Factory
    }
}

interface PublishToWebDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun auth(): AuthRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun spaces(): SpaceViewSubscriptionContainer
    fun urlBuilder(): UrlBuilder
}
