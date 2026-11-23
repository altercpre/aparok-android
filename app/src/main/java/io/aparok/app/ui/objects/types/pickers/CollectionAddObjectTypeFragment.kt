package io.aparok.app.ui.objects.types.pickers

import androidx.core.os.bundleOf
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_utils.ext.withParentSafe
import io.aparok.app.di.common.componentManager
import io.aparok.app.presentation.objects.ObjectTypeChangeViewModel
import io.aparok.app.ui.objects.BaseObjectTypeChangeFragment

/**
 * Selecting an object type to create when adding a new object to a collection.
 * Includes list types (Set, Collection) and bookmarks.
 * Excludes media types (Image, File, Video, Audio) and templates.
 */
class CollectionAddObjectTypeFragment : BaseObjectTypeChangeFragment() {

    override fun onItemClicked(item: ObjectWrapper.Type) {
        withParentSafe<CollectionObjectTypeSelectionListener> {
            onSelectObjectTypeForCollection(objType = item)
        }
    }

    override fun resolveTitle(): String = getString(R.string.change_type)

    override fun injectDependencies() {
        val params = ObjectTypeChangeViewModel.VmParams(
            spaceId = SpaceId(space),
            screen = ObjectTypeChangeViewModel.Screen.CREATE_OBJECT_FOR_COLLECTION,
            excludeTypes = excludeTypes
        )
        componentManager().objectTypeChangeComponent.get(params).inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectTypeChangeComponent.release()
    }

    companion object {
        fun newInstance(space: Id) = CollectionAddObjectTypeFragment().apply {
            arguments = bundleOf(
                ARG_SPACE to space
            )
        }
    }
}
