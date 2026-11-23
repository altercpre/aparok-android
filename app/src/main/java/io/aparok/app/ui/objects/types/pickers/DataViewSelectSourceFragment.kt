package io.aparok.app.ui.objects.types.pickers

import androidx.core.os.bundleOf
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_utils.ext.withParent
import io.aparok.app.di.common.componentManager
import io.aparok.app.presentation.objects.ObjectTypeChangeViewModel
import io.aparok.app.ui.objects.BaseObjectTypeChangeFragment


class DataViewSelectSourceFragment : BaseObjectTypeChangeFragment() {

    override fun onItemClicked(item: ObjectWrapper.Type) {
        withParent<OnDataViewSelectSourceAction> {
            onProceedWithSelectSource(id = item.id)
        }
    }

    override fun resolveTitle(): String = getString(R.string.select_query)

    override fun injectDependencies() {
        val params = ObjectTypeChangeViewModel.VmParams(
            spaceId = SpaceId(space),
            screen = ObjectTypeChangeViewModel.Screen.DATA_VIEW_SOURCE,
            selectedTypes = selectedTypes
        )
        componentManager().objectTypeChangeComponent.get(params).inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectTypeChangeComponent.release()
    }

    companion object {
        fun newInstance(space: Id, selectedTypes: List<Id>) = DataViewSelectSourceFragment().apply {
            arguments = bundleOf(
                ARG_SPACE to space,
                ARG_SELECTED_TYPES to selectedTypes
            )
        }
    }
}
