package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.block.interactor.sets.GetObjectTypes
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.linking.LinkToObjectViewModelFactory
import io.aparok.app.presentation.search.ObjectSearchViewModel
import io.aparok.app.ui.linking.LinkToObjectFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [LinkToObjectModule::class],
    dependencies = [LinkToObjectDependencies::class]
)
@PerScreen
interface LinkToObjectComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            params: ObjectSearchViewModel.VmParams,
            dependencies: LinkToObjectDependencies
        ) : LinkToObjectComponent
    }

    fun inject(fragment: LinkToObjectFragment)
}

interface LinkToObjectDependencies: ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun getObjectTypes(): GetObjectTypes
    fun searchObjects(): SearchObjects
    fun analytics(): Analytics
    fun analyticSpaceHelperDelegate(): AnalyticSpaceHelperDelegate
    fun dateProvider(): DateProvider
    fun fieldParser(): FieldParser
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun spaceViews(): SpaceViewSubscriptionContainer
}

@Module
object LinkToObjectModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLinkToObjectViewModelFactory(
        vmParams: ObjectSearchViewModel.VmParams,
        urlBuilder: UrlBuilder,
        searchObjects: SearchObjects,
        analytics: Analytics,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        storeOfObjectTypes: StoreOfObjectTypes,
        spaceViews: SpaceViewSubscriptionContainer
    ): LinkToObjectViewModelFactory = LinkToObjectViewModelFactory(
        vmParams = vmParams,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        analytics = analytics,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes,
        spaceViews = spaceViews
    )
}
