package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.`object`.UpdateDetail
import io.aparok.app.domain.objects.StoreOfRelationOptions
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.DeleteRelationOptions
import io.aparok.app.domain.relations.SetRelationOptionOrder
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider
import io.aparok.app.presentation.relations.providers.ObjectValueProvider
import io.aparok.app.presentation.relations.value.tagstatus.TagOrStatusValueViewModel
import io.aparok.app.presentation.relations.value.tagstatus.TagOrStatusValueViewModelFactory
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.relations.value.TagOrStatusValueFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

//region OBJECT (layout BASIC, TASK etc.)
@PerModal
@Subcomponent(
    modules = [TagOrStatusValueObjectModule::class]
)
interface TagOrStatusValueObjectComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: TagOrStatusValueViewModel.ViewModelParams): Builder
        fun build(): TagOrStatusValueObjectComponent
    }

    fun inject(fragment: TagOrStatusValueFragment)
}

@Module
object TagOrStatusValueObjectModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideDeleteRelationOptions(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationOptions = DeleteRelationOptions(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetRelationOptionOrder(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetRelationOptionOrder = SetRelationOptionOrder(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        params: TagOrStatusValueViewModel.ViewModelParams,
        deleteRelationOptions: DeleteRelationOptions,
        setRelationOptionOrder: SetRelationOptionOrder,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        storeOfRelationOptions: StoreOfRelationOptions
    ): TagOrStatusValueViewModelFactory = TagOrStatusValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        deleteRelationOptions = deleteRelationOptions,
        setRelationOptionOrder = setRelationOptionOrder,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        storeOfRelationOptions = storeOfRelationOptions
    )
}
//endregion

//region SET or COLLECTION (layout SET, COLLECTION )
@PerModal
@Subcomponent(
    modules = [TagOrStatusValueSetModule::class]
)
interface TagOrStatusValueSetComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: TagOrStatusValueViewModel.ViewModelParams): Builder
        fun build(): TagOrStatusValueSetComponent
    }

    fun inject(fragment: TagOrStatusValueFragment)
}

@Module
object TagOrStatusValueSetModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideDeleteRelationOptions(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationOptions = DeleteRelationOptions(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetRelationOptionOrder(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetRelationOptionOrder = SetRelationOptionOrder(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        params: TagOrStatusValueViewModel.ViewModelParams,
        deleteRelationOptions: DeleteRelationOptions,
        setRelationOptionOrder: SetRelationOptionOrder,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        storeOfRelationOptions: StoreOfRelationOptions
    ): TagOrStatusValueViewModelFactory = TagOrStatusValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        deleteRelationOptions = deleteRelationOptions,
        setRelationOptionOrder = setRelationOptionOrder,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        storeOfRelationOptions = storeOfRelationOptions
    )
}
//endregion

//region DATA VIEW
@PerModal
@Subcomponent(
    modules = [TagOrStatusValueDataViewModule::class]
)
interface TagOrStatusValueDataViewComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: TagOrStatusValueViewModel.ViewModelParams): Builder
        fun build(): TagOrStatusValueDataViewComponent
    }

    fun inject(fragment: TagOrStatusValueFragment)
}

@Module
object TagOrStatusValueDataViewModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideDeleteRelationOptions(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationOptions = DeleteRelationOptions(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetRelationOptionOrder(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetRelationOptionOrder = SetRelationOptionOrder(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.DATA_VIEW_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        params: TagOrStatusValueViewModel.ViewModelParams,
        deleteRelationOptions: DeleteRelationOptions,
        setRelationOptionOrder: SetRelationOptionOrder,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        storeOfRelationOptions: StoreOfRelationOptions
    ): TagOrStatusValueViewModelFactory = TagOrStatusValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        deleteRelationOptions = deleteRelationOptions,
        setRelationOptionOrder = setRelationOptionOrder,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        storeOfRelationOptions = storeOfRelationOptions
    )
}
//endregion