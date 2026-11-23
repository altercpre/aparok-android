package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.interactor.sets.GetObjectTypes
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.linking.BackLinkOrAddToObjectViewModelFactory
import io.aparok.app.presentation.search.ObjectSearchViewModel
import io.aparok.app.ui.linking.BacklinkOrAddToObjectFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides


@PerScreen
@Component(
    dependencies = [BacklinkOrAddToObjectDependencies::class],
    modules = [
        BackLinkToObjectModule::class,
        BackLinkToObjectModule.Declarations::class
    ]
)
interface BacklinkOrAddToObjectComponent {

    @Component.Builder
    interface Builder {

        fun withDependencies(dependency: BacklinkOrAddToObjectDependencies): Builder

        @BindsInstance
        fun withParams(params: ObjectSearchViewModel.VmParams): Builder

        fun build(): BacklinkOrAddToObjectComponent
    }

    fun inject(fragment: BacklinkOrAddToObjectFragment)
}

@Module
object BackLinkToObjectModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetObjectTypesUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetObjectTypes = GetObjectTypes(repository, dispatchers)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: BackLinkOrAddToObjectViewModelFactory): ViewModelProvider.Factory
    }
}

interface BacklinkOrAddToObjectDependencies : ComponentDependencies {
    fun authRepository(): AuthRepository
    fun blockRepository(): BlockRepository
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun searchObjects(): SearchObjects
    fun dateProvider(): DateProvider
    fun fieldParser(): FieldParser
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun spaceViews(): SpaceViewSubscriptionContainer
}
