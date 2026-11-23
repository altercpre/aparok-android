package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.core_utils.tools.UrlValidator
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.linking.LinkToObjectOrWebViewModel
import io.aparok.app.presentation.linking.LinkToObjectOrWebViewModelFactory
import io.aparok.app.ui.linking.LinkToObjectOrWebPagesFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(
    modules = [LinkToObjectOrWebModule::class]
)
@PerModal
interface LinkToObjectOrWebSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: LinkToObjectOrWebViewModel.VmParams): Builder
        fun module(module: LinkToObjectOrWebModule): Builder
        fun build(): LinkToObjectOrWebSubComponent
    }

    fun inject(fragment: LinkToObjectOrWebPagesFragment)
}

@Module
object LinkToObjectOrWebModule {

    @JvmStatic
    @PerModal
    @Provides
    fun provideLinkToObjectViewModelFactory(
        vmParams: LinkToObjectOrWebViewModel.VmParams,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        searchObjects: SearchObjects,
        analytics: Analytics,
        stores: Editor.Storage,
        urlValidator: UrlValidator,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer
    ): LinkToObjectOrWebViewModelFactory = LinkToObjectOrWebViewModelFactory(
        vmParams = vmParams,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        searchObjects = searchObjects,
        analytics = analytics,
        stores = stores,
        urlValidator = urlValidator,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )
}