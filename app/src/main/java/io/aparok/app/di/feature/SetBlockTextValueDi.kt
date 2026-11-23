package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.domain.block.interactor.UpdateText
import io.aparok.app.domain.clipboard.Copy
import io.aparok.app.domain.clipboard.Paste
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.objects.block.SetBlockTextValueViewModel
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.editor.modals.SetBlockTextValueFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [SetBlockTextValueModule::class])
@PerDialog
interface SetBlockTextValueSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(model: SetBlockTextValueModule): Builder
        fun build(): SetBlockTextValueSubComponent
    }

    fun inject(fragment: SetBlockTextValueFragment)
}

@Module
object SetBlockTextValueModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        storage: Editor.Storage,
        dispatcher: Dispatcher<Payload>,
        paste: Paste,
        copy: Copy,
        updateText: UpdateText,
        analytics: Analytics
    ): SetBlockTextValueViewModel.Factory =
        SetBlockTextValueViewModel.Factory(
            storage = storage,
            dispatcher = dispatcher,
            paste = paste,
            copy = copy,
            updateText = updateText,
            analytics = analytics
        )
}