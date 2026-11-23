package io.aparok.app.di.feature

import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.presentation.editor.bookmark.CreateBookmarkViewModel
import io.aparok.app.ui.editor.modals.CreateBookmarkFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [CreateBookmarkModule::class])
@PerScreen
interface CreateBookmarkSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun createBookmarkModule(module: CreateBookmarkModule): Builder
        fun build(): CreateBookmarkSubComponent
    }

    fun inject(fragment: CreateBookmarkFragment)
}

@Module
class CreateBookmarkModule {

    @Provides
    @PerScreen
    fun provideCreateBookmarkViewModelFactory(): CreateBookmarkViewModel.Factory {
        return CreateBookmarkViewModel.Factory()
    }
}