package io.aparok.app.ui.sets.modals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_ui.extensions.drawable
import io.aparok.app.core_ui.features.sets.PickFilterConditionAdapter
import io.aparok.app.core_ui.layout.DividerVerticalItemDecoration
import io.aparok.app.core_utils.ext.arg
import io.aparok.app.core_utils.ext.argInt
import io.aparok.app.core_utils.ext.hasSpan
import io.aparok.app.core_utils.ext.subscribe
import io.aparok.app.core_utils.ext.withParent
import io.aparok.app.core_utils.ui.BaseBottomSheetFragment
import io.aparok.app.databinding.FragmentSelectFilterConditionBinding
import io.aparok.app.di.common.componentManager
import io.aparok.app.di.feature.DefaultComponentParam
import io.aparok.app.presentation.sets.filter.PickFilterConditionViewModel
import io.aparok.app.presentation.sets.model.Viewer
import io.aparok.app.ui.sets.modals.filter.UpdateConditionActionReceiver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PickFilterConditionFragment :
    BaseBottomSheetFragment<FragmentSelectFilterConditionBinding>() {

    private val ctx: String get() = arg(CTX_KEY)
    private val space: String get() = arg(SPACE_ID_KEY)
    private val mode: Int get() = argInt(ARG_MODE)
    private val type: Viewer.Filter.Type get() = requireArguments().getParcelable(TYPE_KEY)!!
    private val index: Int get() = argInt(INDEX_KEY)

    @Inject
    lateinit var factory: PickFilterConditionViewModel.Factory
    private val vm: PickFilterConditionViewModel by viewModels { factory }

    val isDismissed = MutableSharedFlow<Boolean>(replay = 0)

    override fun onStart() {
        super.onStart()
        with(lifecycleScope) {
            jobs += subscribe(isDismissed) { isDismissed -> if (isDismissed) dismiss() }
            jobs += subscribe(vm.views) { screenState ->
                binding.recycler.adapter = PickFilterConditionAdapter(
                    picked = screenState.picked,
                    conditions = screenState.conditions,
                    click = this@PickFilterConditionFragment::click
                )
                binding.recycler.addItemDecoration(
                    DividerVerticalItemDecoration(
                        divider = requireContext().drawable(R.drawable.divider_relations),
                        isShowInLastItem = false
                    )
                )
            }
        }
        vm.onStart(type, index)
    }

    private fun click(condition: Viewer.Filter.Condition) {
        withParent<UpdateConditionActionReceiver> { update(condition) }
        lifecycleScope.launch { isDismissed.emit(true) }
    }

    override fun injectDependencies() {
        val param = DefaultComponentParam(
            ctx = ctx,
            space = SpaceId(space)
        )
        when (mode) {
            MODE_CREATE -> componentManager().pickFilterConditionComponentCreate.get(param)
                .inject(this)
            MODE_MODIFY -> componentManager().pickFilterConditionComponentModify.get(param)
                .inject(this)
            else -> throw RuntimeException("Wrong mode")
        }
    }

    override fun releaseDependencies() {
        when (mode) {
            MODE_CREATE -> componentManager().pickFilterConditionComponentCreate.release()
            MODE_MODIFY -> componentManager().pickFilterConditionComponentModify.release()
            else -> throw RuntimeException("Wrong mode")
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectFilterConditionBinding = FragmentSelectFilterConditionBinding.inflate(
        inflater, container, false
    )

    companion object {

        const val MODE_CREATE = 1
        const val MODE_MODIFY = 2

        private const val CTX_KEY = "arg.create-filter-relation.ctx"
        private const val SPACE_ID_KEY = "arg.create-filter-relation.space-id"
        private const val ARG_MODE = "arg.create-filter-relation.mode"
        private const val TYPE_KEY = "arg.create-filter-relation.type"
        private const val INDEX_KEY = "arg.create-filter-relation.index"

        fun new(
            ctx: Id,
            space: Id,
            mode: Int,
            type: Viewer.Filter.Type,
            index: Int
        ) = PickFilterConditionFragment().apply {
            arguments = bundleOf(
                CTX_KEY to ctx,
                SPACE_ID_KEY to space,
                ARG_MODE to mode,
                TYPE_KEY to type,
                INDEX_KEY to index
            )
        }
    }
}