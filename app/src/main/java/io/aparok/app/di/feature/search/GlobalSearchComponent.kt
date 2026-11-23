package io.aparok.app.di.feature.search

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.`object`.DuplicateObject
import io.aparok.app.domain.misc.DeepLinkResolver
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelationOptions
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.common.PayloadDelegator
import io.aparok.app.presentation.search.GlobalSearchViewModel
import io.aparok.app.ui.search.GlobalSearchFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [GlobalSearchDependencies::class],
    modules = [
        GlobalSearchModule::class,
        GlobalSearchModule.Declarations::class
    ]
)
@PerScreen
interface GlobalSearchComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: GlobalSearchViewModel.VmParams,
            dependencies: GlobalSearchDependencies
        ): GlobalSearchComponent
    }

    fun inject(fragment: GlobalSearchFragment)
    fun getViewModel(): GlobalSearchViewModel
}

@Module
object GlobalSearchModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateObject(
        repo: BlockRepository
    ): DuplicateObject = DuplicateObject(repo = repo)

    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: GlobalSearchViewModel.Factory): ViewModelProvider.Factory
    }
}

interface GlobalSearchDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun storeOfRelations(): StoreOfRelations
    fun analytics(): Analytics
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
    fun fieldParser(): FieldParser
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun storeOfRelationOptions(): StoreOfRelationOptions
    fun deepLinkResolver(): DeepLinkResolver
    fun spaceManager(): SpaceManager
    fun payloadDelegator(): PayloadDelegator
}
