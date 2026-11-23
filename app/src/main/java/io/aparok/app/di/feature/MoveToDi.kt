package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.moving.MoveToViewModel
import io.aparok.app.presentation.moving.MoveToViewModelFactory
import io.aparok.app.ui.moving.MoveToFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [MoveToModule::class],
    dependencies = [MoveToDependencies::class]
)
@PerScreen
interface MoveToComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            vmParams: MoveToViewModel.VmParams,
            dependencies: MoveToDependencies
        ): MoveToComponent
    }

    fun inject(fragment: MoveToFragment)
}

interface MoveToDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun searchObjects(): SearchObjects
    fun analytics(): Analytics
    fun analyticSpaceHelperDelegate(): AnalyticSpaceHelperDelegate
    fun  fieldParser(): FieldParser
    fun storeOfObjectTypes(): StoreOfObjectTypes
}

@Module
object MoveToModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun provideMoveToViewModelFactory(
        vmParams: MoveToViewModel.VmParams,
        urlBuilder: UrlBuilder,
        searchObjects: SearchObjects,
        analytics: Analytics,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        storeOfObjectTypes: StoreOfObjectTypes
    ): MoveToViewModelFactory = MoveToViewModelFactory(
        vmParams = vmParams,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        analytics = analytics,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes
    )
}