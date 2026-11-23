package io.aparok.app.ui.objects.appearance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_ui.features.objects.appearance.ObjectAppearanceSettingAdapter
import io.aparok.app.core_utils.ext.argString
import io.aparok.app.core_utils.ext.drawable
import io.aparok.app.core_utils.ext.subscribe
import io.aparok.app.core_utils.ext.toast
import io.aparok.app.core_utils.ui.BaseBottomSheetFragment
import io.aparok.app.databinding.FragmentObjectAppearanceSettingBinding
import io.aparok.app.di.common.componentManager
import io.aparok.app.di.feature.DefaultComponentParam
import io.aparok.app.presentation.objects.appearance.ObjectAppearanceSettingViewModel
import io.aparok.app.ui.objects.appearance.choose.ObjectAppearanceChooseDescriptionFragment
import io.aparok.app.ui.objects.appearance.choose.ObjectAppearanceChooseIconFragment
import io.aparok.app.ui.objects.appearance.choose.ObjectAppearanceChoosePreviewLayoutFragment
import javax.inject.Inject

class ObjectAppearanceSettingFragment :
    BaseBottomSheetFragment<FragmentObjectAppearanceSettingBinding>() {

    @Inject
    lateinit var factory: ObjectAppearanceSettingViewModel.Factory
    private val vm by viewModels<ObjectAppearanceSettingViewModel> { factory }

    private val ctx: String get() = argString(CONTEXT_ID_KEY)
    private val space: String get() = argString(SPACE_KEY)
    private val block: String get() = argString(BLOCK_ID_KEY)
    private val adapterAppearance by lazy {
        ObjectAppearanceSettingAdapter(
            onItemClick = vm::onItemClicked,
            onSettingToggleChanged = { item, isChecked ->
                vm.onToggleClicked(
                    toggle = item,
                    blockId = block,
                    ctx = ctx,
                    isChecked = isChecked
                )
            },
            onCoverToggleChanged = { isChecked ->
                vm.updateCoverAppearance(
                    blockId = block,
                    ctx = ctx,
                    isCoverVisible = isChecked
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterAppearance
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(drawable(R.drawable.divider_relations))
                }
            )
        }
    }

    override fun onStart() {
        jobs += lifecycleScope.subscribe(vm.objectPreviewState) { observeState(it) }
        jobs += lifecycleScope.subscribe(vm.commands) { observeCommands(it) }
        super.onStart()
        vm.onStart(blockId = block)
    }

    private fun observeState(state: ObjectAppearanceSettingViewModel.State) {
        when (state) {
            is ObjectAppearanceSettingViewModel.State.Error -> {
                activity?.toast(state.msg)
            }
            is ObjectAppearanceSettingViewModel.State.Success -> {
                adapterAppearance.submitList(state.data)
            }
        }
    }

    private fun observeCommands(command: ObjectAppearanceSettingViewModel.Command) {
        val fr = when (command) {
            ObjectAppearanceSettingViewModel.Command.IconScreen -> {
                ObjectAppearanceChooseIconFragment.new(
                    block = block,
                    ctx = ctx,
                    space = space
                )
            }
            ObjectAppearanceSettingViewModel.Command.PreviewLayoutScreen -> {
                ObjectAppearanceChoosePreviewLayoutFragment.new(
                    block = block,
                    ctx = ctx,
                    space = space
                )
            }
            ObjectAppearanceSettingViewModel.Command.DescriptionScreen -> {
                ObjectAppearanceChooseDescriptionFragment.new(
                    block = block,
                    space = space,
                    ctx = ctx
                )
            }
        }
        fr.show(parentFragmentManager, null)
    }

    override fun injectDependencies() {
        componentManager()
            .objectAppearanceSettingComponent
            .get(
                params = DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectAppearanceSettingComponent.release()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentObjectAppearanceSettingBinding = FragmentObjectAppearanceSettingBinding.inflate(
        inflater, container, false
    )

    companion object {
        fun new(ctx: Id, space: Id, block: Id) = ObjectAppearanceSettingFragment().apply {
            arguments = bundleOf(
                CONTEXT_ID_KEY to ctx,
                SPACE_KEY to space,
                BLOCK_ID_KEY to block
            )
        }
        const val CONTEXT_ID_KEY = "arg.object-appearance-setting.ctx"
        const val SPACE_KEY = "arg.object-appearance-setting.space"
        const val BLOCK_ID_KEY = "arg.object-appearance-setting.block"
    }
}