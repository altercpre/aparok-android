package io.aparok.app.ui.editor.modals

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.di.common.componentManager
import io.aparok.app.di.feature.DefaultComponentParam
import io.aparok.app.presentation.editor.picker.ObjectIconPickerViewModelFactory
import io.aparok.app.presentation.picker.IconPickerViewModel
import javax.inject.Inject

open class ObjectIconPickerFragment : IconPickerFragmentBase<Id>() {

    @Inject
    lateinit var factory: ObjectIconPickerViewModelFactory
    override val vm by viewModels<IconPickerViewModel<Id>> { factory }

    override val target: Id
        get() = context

    override fun injectDependencies() {
        componentManager()
            .objectIconPickerComponent
            .get(
                params = DefaultComponentParam(
                    ctx = target,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectIconPickerComponent.release()
    }

    companion object {
        fun new(ctx: Id, space: Id) = ObjectIconPickerFragment().apply {
            arguments = bundleOf(
                ARG_CONTEXT_ID_KEY to ctx,
                ARG_SPACE_ID_KEY to space
            )
        }
    }
}