package io.aparok.app.di.feature.relations

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_utils.di.scope.CreateFromScratch
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.DefaultStoreOfRelations
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.relations.CreateRelation
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.relations.RelationCreateFromLibraryViewModel
import io.aparok.app.presentation.relations.model.CreateFromScratchState
import io.aparok.app.presentation.relations.model.StateHolder
import io.aparok.app.ui.relations.RelationCreateFromLibraryFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [RelationCreateFromLibraryDependencies::class],
    modules = [
        RelationCreateFromLibraryModule::class,
        RelationCreateFromLibraryModule.Declarations::class
    ]
)
@PerScreen
@CreateFromScratch
interface RelationCreateFromLibraryComponent {

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: RelationCreateFromLibraryDependencies
        ): RelationCreateFromLibraryComponent
    }

    fun relationFormatPickerComponent(): RelationFormatPickerSubcomponent.Builder
    fun limitObjectTypeComponent(): LimitObjectTypeSubComponent.Builder

    fun inject(fragment: RelationCreateFromLibraryFragment)
}

@Module
object RelationCreateFromLibraryModule {

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
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    fun provideSearchObjects(
        repo: BlockRepository
    ) = SearchObjects(
        repo = repo
    )

    @JvmStatic
    @Provides
    fun provideStoreOfRelations(): StoreOfRelations = DefaultStoreOfRelations()

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: RelationCreateFromLibraryViewModel.Factory): ViewModelProvider.Factory
    }

}

interface RelationCreateFromLibraryDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun urlBuilder(): UrlBuilder
    fun spaceManager(): SpaceManager
}