package io.aparok.app.di.feature.widgets

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.widgets.UpdateWidget
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.presentation.widgets.SelectWidgetTypeViewModel
import io.aparok.app.presentation.widgets.WidgetDispatchEvent
import io.aparok.app.ui.widgets.SelectWidgetTypeFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [SelectWidgetTypeModule::class],
    dependencies = [SelectWidgetTypeDependencies::class]
)
@PerModal
interface SelectWidgetTypeComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: SelectWidgetTypeDependencies): SelectWidgetTypeComponent
    }

    fun inject(fragment: SelectWidgetTypeFragment)
}

interface SelectWidgetTypeDependencies : ComponentDependencies {
    fun dispatcherWidgets(): Dispatcher<WidgetDispatchEvent>
    fun dispatcherPayload(): Dispatcher<Payload>
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
}

@Module
object SelectWidgetTypeModule {

    @JvmStatic
    @PerModal
    @Provides
    fun factory(
        payloadDispatcher: Dispatcher<Payload>,
        widgetDispatcher: Dispatcher<WidgetDispatchEvent>,
        updateWidget: UpdateWidget,
        appCoroutineDispatchers: AppCoroutineDispatchers,
        analytics: Analytics
    ): SelectWidgetTypeViewModel.Factory = SelectWidgetTypeViewModel.Factory(
        payloadDispatcher = payloadDispatcher,
        widgetDispatcher = widgetDispatcher,
        updateWidget = updateWidget,
        appCoroutineDispatchers = appCoroutineDispatchers,
        analytics = analytics
    )
}