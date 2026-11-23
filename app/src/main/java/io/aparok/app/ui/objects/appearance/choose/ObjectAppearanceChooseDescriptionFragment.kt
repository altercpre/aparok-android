package io.aparok.app.ui.objects.appearance.choose

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.di.common.componentManager
import io.aparok.app.di.feature.DefaultComponentParam
import io.aparok.app.presentation.objects.appearance.choose.ObjectAppearanceChooseDescriptionViewModel
import io.aparok.app.presentation.objects.appearance.choose.ObjectAppearanceChooseSettingsView
import javax.inject.Inject

class ObjectAppearanceChooseDescriptionFragment :
    ObjectAppearanceChooseFragmentBase<ObjectAppearanceChooseSettingsView.Description, ObjectAppearanceChooseDescriptionViewModel>() {

    @Inject
    lateinit var factory: ObjectAppearanceChooseDescriptionViewModel.Factory
    override val vm by viewModels<ObjectAppearanceChooseDescriptionViewModel> { factory }
    override val title: Int = R.string.description

    override fun injectDependencies() {
        componentManager()
            .objectAppearanceChooseDescriptionComponent
            .get(params = DefaultComponentParam(ctx = ctx, space = SpaceId(space)))
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectAppearanceChooseDescriptionComponent.release()
    }

    companion object {
        fun new(
            ctx: Id,
            space: Id,
            block: Id
        ) = ObjectAppearanceChooseDescriptionFragment().apply {
            arguments = bundleOf(
                CONTEXT_ID_KEY to ctx,
                SPACE_KEY to space,
                BLOCK_ID_KEY to block
            )
        }
    }
}