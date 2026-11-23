package io.aparok.app.ui.sets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_utils.ext.argBoolean
import io.aparok.app.core_utils.ext.setVisible
import io.aparok.app.di.common.componentManager
import io.aparok.app.di.feature.DefaultComponentParam
import io.aparok.app.presentation.picker.IconPickerViewModel
import io.aparok.app.presentation.editor.picker.ObjectSetIconPickerViewModelFactory
import io.aparok.app.ui.editor.modals.IconPickerFragmentBase
import javax.inject.Inject

class ObjectSetIconPickerFragment : IconPickerFragmentBase<Id>() {

    @Inject
    lateinit var factory: ObjectSetIconPickerViewModelFactory
    override val vm by viewModels<IconPickerViewModel<Id>> { factory }

    override val target: Id
        get() = context

    private val showRemoveButton: Boolean
        get() = argBoolean(ARG_SHOW_REMOVE_BUTTON)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRemoveIcon.setVisible(showRemoveButton)
    }

    override fun injectDependencies() {
        componentManager()
            .objectSetIconPickerComponent
            .get(
                params = DefaultComponentParam(
                    ctx = context,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectSetIconPickerComponent.release()
    }
}

const val ARG_SHOW_REMOVE_BUTTON = "arg.show_remove_button"