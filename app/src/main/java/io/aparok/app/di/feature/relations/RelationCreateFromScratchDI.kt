package io.aparok.app.di.feature.relations

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_utils.di.scope.CreateFromScratch
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.dataview.interactor.AddRelationToDataView
import io.aparok.app.domain.dataview.interactor.UpdateDataViewViewer
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.AddRelationToObject
import io.aparok.app.domain.relations.CreateRelation
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.relations.RelationCreateFromScratchForDataViewViewModel
import io.aparok.app.presentation.relations.RelationCreateFromScratchForObjectBlockViewModel
import io.aparok.app.presentation.relations.RelationCreateFromScratchForObjectViewModel
import io.aparok.app.presentation.relations.model.CreateFromScratchState
import io.aparok.app.presentation.relations.model.StateHolder
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.relations.RelationCreateFromScratchForDataViewFragment
import io.aparok.app.ui.relations.RelationCreateFromScratchForObjectBlockFragment
import io.aparok.app.ui.relations.RelationCreateFromScratchForObjectFragment
import io.aparok.app.ui.relations.RelationCreateFromScratchFormatPickerFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [RelationCreateFromScratchForObjectModule::class])
@CreateFromScratch
interface RelationCreateFromScratchForObjectSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: RelationCreateFromScratchForObjectModule): Builder
        fun build(): RelationCreateFromScratchForObjectSubComponent
    }

    fun inject(fragment: RelationCreateFromScratchForObjectFragment)

    fun relationFormatPickerComponent(): RelationFormatPickerSubcomponent.Builder
    fun limitObjectTypeComponent(): LimitObjectTypeSubComponent.Builder
}

@Module
object RelationCreateFromScratchForObjectModule {

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideViewModelFactory(
        addRelationToObject: AddRelationToObject,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        createFromScratchState: StateHolder<CreateFromScratchState>,
        createRelation: CreateRelation,
        spaceManager: SpaceManager,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations
    ) = RelationCreateFromScratchForObjectViewModel.Factory(
        addRelationToObject = addRelationToObject,
        createRelation = createRelation,
        dispatcher = dispatcher,
        analytics = analytics,
        createFromScratchState = createFromScratchState,
        spaceManager = spaceManager,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideState(): StateHolder<CreateFromScratchState> = StateHolder(
        initial = CreateFromScratchState(
            format = RelationFormat.OBJECT,
            limitObjectTypes = emptyList()
        )
    )
}

@Subcomponent(modules = [RelationCreateFromScratchForDataViewModule::class])
@CreateFromScratch
interface RelationCreateFromScratchForDataViewSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: RelationCreateFromScratchForDataViewModule): Builder
        fun build(): RelationCreateFromScratchForDataViewSubComponent
    }

    fun inject(fragment: RelationCreateFromScratchForDataViewFragment)

    fun relationFormatPickerComponent(): RelationFormatPickerSubcomponent.Builder
    fun limitObjectTypeComponent(): LimitObjectTypeSubComponent.Builder
}

@Module
object RelationCreateFromScratchForDataViewModule {

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        updateDataViewViewer: UpdateDataViewViewer,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        createFromScratchState: StateHolder<CreateFromScratchState>,
        createRelation: CreateRelation,
        addRelationToDataView: AddRelationToDataView,
        spaceManager: SpaceManager,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations
    ) = RelationCreateFromScratchForDataViewViewModel.Factory(
        addRelationToDataView = addRelationToDataView,
        dispatcher = dispatcher,
        objectState = state,
        updateDataViewViewer = updateDataViewViewer,
        analytics = analytics,
        createFromScratchState = createFromScratchState,
        createRelation = createRelation,
        spaceManager = spaceManager,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideState(): StateHolder<CreateFromScratchState> = StateHolder(
        initial = CreateFromScratchState(
            format = RelationFormat.OBJECT,
            limitObjectTypes = emptyList()
        )
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun addRelationToDataView(repo: BlockRepository) = AddRelationToDataView(repo)
}

@Subcomponent(modules = [RelationCreateFromScratchForObjectBlockModule::class])
@CreateFromScratch
interface RelationCreateFromScratchForObjectBlockSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: RelationCreateFromScratchForObjectBlockModule): Builder
        fun build(): RelationCreateFromScratchForObjectBlockSubComponent
    }

    fun inject(fragment: RelationCreateFromScratchForObjectBlockFragment)

    fun relationFormatPickerComponent(): RelationFormatPickerSubcomponent.Builder
    fun limitObjectTypeComponent(): LimitObjectTypeSubComponent.Builder
}

@Module
object RelationCreateFromScratchForObjectBlockModule {

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideViewModelFactory(
        addRelationToObject: AddRelationToObject,
        createRelation: CreateRelation,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        createFromScratchState: StateHolder<CreateFromScratchState>,
        spaceManager: SpaceManager,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations
    ) = RelationCreateFromScratchForObjectBlockViewModel.Factory(
        addRelationToObject = addRelationToObject,
        createRelation = createRelation,
        dispatcher = dispatcher,
        analytics = analytics,
        createFromScratchState = createFromScratchState,
        spaceManager = spaceManager,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideState(): StateHolder<CreateFromScratchState> = StateHolder(
        initial = CreateFromScratchState(
            format = RelationFormat.OBJECT,
            limitObjectTypes = emptyList()
        )
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )
}

@Subcomponent
@PerDialog
interface RelationFormatPickerSubcomponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): RelationFormatPickerSubcomponent
    }

    fun inject(fragment: RelationCreateFromScratchFormatPickerFragment)
}

