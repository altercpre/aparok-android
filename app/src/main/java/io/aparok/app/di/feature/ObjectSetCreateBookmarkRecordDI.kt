package io.aparok.app.di.feature;

import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.core_utils.tools.UrlValidator
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.collections.AddObjectToCollection
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.objects.CreateBookmarkObject
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.sets.ObjectSetCreateBookmarkRecordViewModel
import io.aparok.app.presentation.sets.ObjectSetSession
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.sets.modals.SetObjectCreateBookmarkRecordFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ObjectSetCreateBookmarkRecordModule::class])
@PerDialog
interface ObjectSetCreateBookmarkRecordSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ObjectSetCreateBookmarkRecordModule): Builder
        fun build(): ObjectSetCreateBookmarkRecordSubComponent
    }

    fun inject(fragment: SetObjectCreateBookmarkRecordFragment)
}

@Module
object ObjectSetCreateBookmarkRecordModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideObjectSetRecordViewModelFactory(
        createBookmarkObject: CreateBookmarkObject,
        urlValidator: UrlValidator,
        spaceManager: SpaceManager,
        objectState: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        addObjectToCollection: AddObjectToCollection,
        session: ObjectSetSession,
        storeOfRelations: StoreOfRelations,
        dateProvider: DateProvider
    ) = ObjectSetCreateBookmarkRecordViewModel.Factory(
        createBookmarkObject = createBookmarkObject,
        urlValidator = urlValidator,
        spaceManager = spaceManager,
        objectState = objectState,
        dispatcher = dispatcher,
        addObjectToCollection = addObjectToCollection,
        session = session,
        storeOfRelations = storeOfRelations,
        dateProvider = dateProvider
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideCreateBookmarkObjectUseCase(
        repo: BlockRepository
    ): CreateBookmarkObject = CreateBookmarkObject(
        repo = repo
    )
}