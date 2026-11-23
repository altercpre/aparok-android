package io.aparok.app.di.feature

import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.presentation.objects.ObjectTypeChangeViewModel
import io.aparok.app.ui.objects.types.pickers.AppDefaultObjectTypeFragment
import io.aparok.app.ui.objects.types.pickers.CollectionAddObjectTypeFragment
import io.aparok.app.ui.objects.types.pickers.DataViewSelectSourceFragment
import io.aparok.app.ui.objects.types.pickers.EditorObjectTypeUpdateFragment
import io.aparok.app.ui.objects.types.pickers.EmptyDataViewSelectSourceFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [ObjectTypeChangeModule::class])
@PerScreen
interface ObjectTypeChangeSubComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: ObjectTypeChangeViewModel.VmParams
        ): ObjectTypeChangeSubComponent
    }

    fun inject(fragment: DataViewSelectSourceFragment)
    fun inject(fragment: EmptyDataViewSelectSourceFragment)
    fun inject(fragment: AppDefaultObjectTypeFragment)
    fun inject(fragment: CollectionAddObjectTypeFragment)
    fun inject(fragment: EditorObjectTypeUpdateFragment)
}

@Module
object ObjectTypeChangeModule {

}