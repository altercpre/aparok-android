package io.aparok.app.di.feature.relations

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.dataview.interactor.AddRelationToDataView
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.relations.GetRelations
import io.aparok.app.domain.workspace.AddObjectToWorkspace
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.relations.RelationAddToDataViewViewModel
import io.aparok.app.presentation.relations.RelationAddToObjectViewModel
import io.aparok.app.presentation.relations.RelationAddViewModelBase
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider.Companion.DATA_VIEW_PROVIDER_TYPE
import io.aparok.app.presentation.relations.providers.ObjectRelationProvider.Companion.INTRINSIC_PROVIDER_TYPE
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.relations.RelationAddToDataViewFragment
import io.aparok.app.ui.relations.RelationAddToObjectBlockFragment
import io.aparok.app.ui.relations.RelationAddToObjectFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [RelationAddToObjectModule::class])
@PerDialog
interface RelationAddToObjectSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withVmParams(vmParams: RelationAddViewModelBase.VmParams) : Builder
        fun module(module: RelationAddToObjectModule): Builder
        fun build(): RelationAddToObjectSubComponent
    }

    fun inject(fragment: RelationAddToObjectFragment)
    fun inject(fragment: RelationAddToObjectBlockFragment)
}

@Module
object RelationAddToObjectModule {
    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        vmParams: RelationAddViewModelBase.VmParams,
        addRelationToObject: AddRelationToObject,
        storeOfRelations: StoreOfRelations,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        @Named(INTRINSIC_PROVIDER_TYPE) relationsProvider: ObjectRelationProvider,
        getRelations: GetRelations,
        appCoroutineDispatchers: AppCoroutineDispatchers,
        addObjectToWorkspace: AddObjectToWorkspace,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): RelationAddToObjectViewModel.Factory = RelationAddToObjectViewModel.Factory(
        vmParams = vmParams,
        storeOfRelations = storeOfRelations,
        addRelationToObject = addRelationToObject,
        dispatcher = dispatcher,
        analytics = analytics,
        relationsProvider = relationsProvider,
        getRelations = getRelations,
        appCoroutineDispatchers = appCoroutineDispatchers,
        addObjectToWorkspace = addObjectToWorkspace,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun getRelations(repo: BlockRepository) : GetRelations = GetRelations(repo)

    @JvmStatic
    @Provides
    @PerDialog
    fun addObjectToWorkspace(
        repo: BlockRepository,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ) : AddObjectToWorkspace = AddObjectToWorkspace(
        repo = repo,
        dispatchers = appCoroutineDispatchers
    )
}

@Subcomponent(modules = [RelationAddToDataViewModule::class])
@PerDialog
interface RelationAddToDataViewSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withVmParams(vmParams: RelationAddViewModelBase.VmParams) : Builder
        fun module(module: RelationAddToDataViewModule): Builder
        fun build(): RelationAddToDataViewSubComponent
    }

    fun inject(fragment: RelationAddToDataViewFragment)
}

@Module
object RelationAddToDataViewModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        vmParams: RelationAddViewModelBase.VmParams,
        addRelationToDataView: AddRelationToDataView,
        dispatcher: Dispatcher<Payload>,
        state: MutableStateFlow<ObjectState>,
        updateDataViewViewer: UpdateDataViewViewer,
        analytics: Analytics,
        @Named(DATA_VIEW_PROVIDER_TYPE) relationsProvider: ObjectRelationProvider,
        appCoroutineDispatchers: AppCoroutineDispatchers,
        getRelations: GetRelations,
        addObjectToWorkspace: AddObjectToWorkspace,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations
    ): RelationAddToDataViewViewModel.Factory = RelationAddToDataViewViewModel.Factory(
        addRelationToDataView = addRelationToDataView,
        dispatcher = dispatcher,
        state = state,
        updateDataViewViewer = updateDataViewViewer,
        analytics = analytics,
        relationsProvider = relationsProvider,
        appCoroutineDispatchers = appCoroutineDispatchers,
        getRelations = getRelations,
        addObjectToWorkspace = addObjectToWorkspace,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        vmParams = vmParams
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideAddRelationToDataViewUseCase(
        repo: BlockRepository
    ): AddRelationToDataView = AddRelationToDataView(repo)

    @JvmStatic
    @Provides
    @PerDialog
    fun getRelations(repo: BlockRepository) : GetRelations = GetRelations(repo)


    @JvmStatic
    @Provides
    @PerDialog
    fun addObjectToWorkspace(
        repo: BlockRepository,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ) : AddObjectToWorkspace = AddObjectToWorkspace(
        repo = repo,
        dispatchers = appCoroutineDispatchers
    )
}