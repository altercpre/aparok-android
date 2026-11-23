package io.aparok.app.ui.objects.types.pickers

import android.os.Bundle
import androidx.core.os.bundleOf
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_utils.ext.withParent
import io.aparok.app.di.common.componentManager
import io.aparok.app.presentation.objects.ObjectTypeChangeViewModel
import io.aparok.app.ui.objects.BaseObjectTypeChangeFragment

/**
 * Selecting a default object type for application
 */
class AppDefaultObjectTypeFragment : BaseObjectTypeChangeFragment() {

    override fun onItemClicked(item: ObjectWrapper.Type) {
        withParent<ObjectTypeSelectionListener> {
            onSelectObjectType(objType = item)
        }
    }

    override fun resolveTitle(): String = getString(R.string.default_type_screen_title)

    override fun injectDependencies() {
        val params = ObjectTypeChangeViewModel.VmParams(
            spaceId = SpaceId(space),
            screen = ObjectTypeChangeViewModel.Screen.DEFAULT_OBJECT_TYPE
        )
        componentManager().objectTypeChangeComponent.get(params).inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectTypeChangeComponent.release()
    }

    companion object {
        fun args(space: Id) : Bundle = bundleOf(
            ARG_SPACE to space
        )
    }
}
