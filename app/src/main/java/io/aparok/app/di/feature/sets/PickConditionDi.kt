package io.aparok.app.di.feature.sets

import io.aparok.app.core_utils.di.scope.PerDialog
import io.aparok.app.presentation.sets.filter.PickFilterConditionViewModel
import io.aparok.app.ui.sets.modals.PickFilterConditionFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [PickConditionModule::class])
@PerDialog
interface PickFilterConditionSubComponent {

    @Subcomponent.Builder
    interface Builder {

        fun module(module: PickConditionModule): Builder
        fun build(): PickFilterConditionSubComponent
    }

    fun inject(fragment: PickFilterConditionFragment)

}

@Module
object PickConditionModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideFactory(): PickFilterConditionViewModel.Factory =
        PickFilterConditionViewModel.Factory()
}