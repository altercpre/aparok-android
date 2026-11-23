package io.aparok.app.di.feature.widgets

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.debugging.Logger
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.widgets.GetSuggestedWidgetTypes
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.search.ObjectSearchViewModel
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.widgets.SelectWidgetSourceViewModel
import io.aparok.app.presentation.widgets.WidgetDispatchEvent
import io.aparok.app.ui.widgets.SelectWidgetSourceFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [SelectWidgetSourceModule::class],
    dependencies = [SelectWidgetSourceDependencies::class]
)
@PerModal
interface SelectWidgetSourceComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            vmParams: ObjectSearchViewModel.VmParams,
            dependencies: SelectWidgetSourceDependencies
        ): SelectWidgetSourceComponent
    }

    fun inject(fragment: SelectWidgetSourceFragment)
}

interface SelectWidgetSourceDependencies : ComponentDependencies {
    fun dispatcherWidgets(): Dispatcher<WidgetDispatchEvent>
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun searchObjects(): SearchObjects
    fun fieldParser(): FieldParser
    fun logger(): Logger
    fun spaceViews(): SpaceViewSubscriptionContainer
}

@Module
object SelectWidgetSourceModule {

    @JvmStatic
    @PerModal
    @Provides
    fun factory(
        vmParams: ObjectSearchViewModel.VmParams,
        urlBuilder: UrlBuilder,
        analytics: Analytics,
        searchObjects: SearchObjects,
        dispatcher: Dispatcher<WidgetDispatchEvent>,
        analyticsHelper: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        storeOfObjectTypes: StoreOfObjectTypes,
        getSuggestedWidgetTypes: GetSuggestedWidgetTypes,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
    ): SelectWidgetSourceViewModel.Factory = SelectWidgetSourceViewModel.Factory(
        vmParams = vmParams,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        analytics = analytics,
        dispatcher = dispatcher,
        analyticSpaceHelperDelegate = analyticsHelper,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes,
        getSuggestedWidgetTypes = getSuggestedWidgetTypes,
        spaceViews = spaceViewSubscriptionContainer
    )
}