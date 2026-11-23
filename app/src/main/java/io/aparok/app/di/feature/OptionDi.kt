package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.CreateRelationOption
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.relations.option.CreateOrEditOptionViewModel
import io.aparok.app.presentation.relations.option.CreateOrEditOptionViewModelFactory
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider
import io.aparok.app.presentation.relations.providers.ObjectValueProvider
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.relations.value.CreateOrEditOptionFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

//region OBJECT (layout BASIC, TASK etc.)
@PerModal
@Subcomponent(
    modules = [CreateOrEditOptionObjectModule::class]
)
interface CreateOrEditOptionObjectComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: CreateOrEditOptionViewModel.ViewModelParams): Builder
        fun build(): CreateOrEditOptionObjectComponent
    }

    fun inject(fragment: CreateOrEditOptionFragment)
}

@Module
object CreateOrEditOptionObjectModule {

    @JvmStatic
    @Provides
    @PerModal
    fun createRelationOption(
        repo: BlockRepository
    ): CreateRelationOption = CreateRelationOption(repo = repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        params: CreateOrEditOptionViewModel.ViewModelParams,
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: SetObjectDetails,
        dispatcher: Dispatcher<Payload>,
        spaceManager: SpaceManager,
        analytics: Analytics,
        createOption: CreateRelationOption,
        storeOfRelations: StoreOfRelations,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): CreateOrEditOptionViewModelFactory = CreateOrEditOptionViewModelFactory(
        params = params,
        values = values,
        createOption = createOption,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        spaceManager = spaceManager,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )
}
//endregion

//region SET or COLLECTION (layout SET, COLLECTION )
@PerModal
@Subcomponent(
    modules = [CreateOrEditOptionSetModule::class]
)
interface CreateOrEditOptionSetComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: CreateOrEditOptionViewModel.ViewModelParams): Builder
        fun build(): CreateOrEditOptionSetComponent
    }

    fun inject(fragment: CreateOrEditOptionFragment)
}

@Module
object CreateOrEditOptionSetModule {

    @JvmStatic
    @Provides
    @PerModal
    fun createRelationOption(
        repo: BlockRepository
    ): CreateRelationOption = CreateRelationOption(repo = repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        params: CreateOrEditOptionViewModel.ViewModelParams,
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: SetObjectDetails,
        dispatcher: Dispatcher<Payload>,
        spaceManager: SpaceManager,
        analytics: Analytics,
        createOption: CreateRelationOption,
        storeOfRelations: StoreOfRelations,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): CreateOrEditOptionViewModelFactory = CreateOrEditOptionViewModelFactory(
        params = params,
        values = values,
        createOption = createOption,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        spaceManager = spaceManager,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )
}
//endregion

//region DATA VIEW
@PerModal
@Subcomponent(
    modules = [CreateOrEditOptionDataViewModule::class]
)
interface CreateOrEditOptionDataViewComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: CreateOrEditOptionViewModel.ViewModelParams): Builder
        fun build(): CreateOrEditOptionDataViewComponent
    }

    fun inject(fragment: CreateOrEditOptionFragment)
}

@Module
object CreateOrEditOptionDataViewModule {

    @JvmStatic
    @Provides
    @PerModal
    fun createRelationOption(
        repo: BlockRepository
    ): CreateRelationOption = CreateRelationOption(repo = repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        params: CreateOrEditOptionViewModel.ViewModelParams,
        @Named(ObjectRelationProvider.DATA_VIEW_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: SetObjectDetails,
        dispatcher: Dispatcher<Payload>,
        spaceManager: SpaceManager,
        analytics: Analytics,
        createOption: CreateRelationOption,
        storeOfRelations: StoreOfRelations,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): CreateOrEditOptionViewModelFactory = CreateOrEditOptionViewModelFactory(
        params = params,
        values = values,
        createOption = createOption,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        spaceManager = spaceManager,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )
}
//endregion