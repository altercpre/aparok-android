package io.aparok.app.ui.sets.modals.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_ui.extensions.drawable
import io.aparok.app.core_ui.features.sets.ViewerSortAdapter
import io.aparok.app.core_ui.layout.DividerVerticalItemDecoration
import io.aparok.app.core_ui.reactive.clicks
import io.aparok.app.core_utils.ext.arg
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.invisible
import io.aparok.app.core_utils.ext.subscribe
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.core_utils.ui.BaseBottomSheetFragment
import io.aparok.app.databinding.FragmentViewerSortBinding
import io.aparok.app.di.common.componentManager
import io.aparok.app.di.feature.DefaultComponentParam
import io.aparok.app.presentation.sets.sort.ViewerSortViewModel
import io.aparok.app.presentation.sets.sort.ViewerSortViewModel.ScreenState
import javax.inject.Inject

open class ViewerSortFragment : BaseBottomSheetFragment<FragmentViewerSortBinding>() {

    private val ctx: String get() = arg(CTX_KEY)
    private val space: String get() = arg(SPACE_ID_KEY)
    private val viewer: String get() = arg(VIEWER_ID_KEY)

    @Inject
    lateinit var factory: ViewerSortViewModel.Factory

    private val vm: ViewerSortViewModel by viewModels { factory }

    private val viewerSortAdapter by lazy {
        ViewerSortAdapter(
            onViewerSortClicked = { view ->
                if (view.mode == ScreenState.READ) navigateToChangeSort(
                    sortId = view.sortId,
                    relation = view.relation
                )
            },
            onRemoveViewerSortClicked = {
                vm.onRemoveViewerSortClicked(
                    ctx = ctx, view = it, viewerId = viewer
                )
            }
        )
    }

    private lateinit var dividerItem: RecyclerView.ItemDecoration
    private lateinit var dividerItemEdit: RecyclerView.ItemDecoration

    private fun navigateToSelectSort() {
        val fr = SelectSortRelationFragment.new(ctx = ctx, space = space, viewerId = viewer)
        fr.show(parentFragmentManager, null)
    }

    private fun navigateToChangeSort(sortId: Id, relation: Key) {
        val fr = ModifyViewerSortFragment.new(
            ctx = ctx,
            space = space,
            viewer = viewer,
            sortId = sortId,
            relation = relation
        )
        fr.show(parentFragmentManager, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dividerItem = DividerVerticalItemDecoration(
            divider = requireContext().drawable(R.drawable.decoration_viewer_sort),
            isShowInLastItem = false
        )
        dividerItemEdit = DividerVerticalItemDecoration(
            divider = requireContext().drawable(R.drawable.decoration_viewer_sort_edit),
            isShowInLastItem = false
        )
        binding.viewerSortRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewerSortAdapter
        }
        with(lifecycleScope) {
            subscribe(binding.btnAdd.clicks()) { navigateToSelectSort() }
            subscribe(binding.btnEditSortOrDone.clicks()) {
                if (binding.btnEditSortOrDone.text == getString(R.string.edit))
                    vm.onEditClicked()
                else if (binding.btnEditSortOrDone.text == getString(R.string.done))
                    vm.onDoneClicked()
            }
        }
    }

    override fun onStart() {
        with(lifecycleScope) {
            jobs += subscribe(vm.screenState) { render(it) }
            jobs += subscribe(vm.views) { viewerSortAdapter.update(it) }
            jobs += subscribe(vm.isDismissed) { isDismissed -> if (isDismissed) dismiss() }
        }
        super.onStart()
        vm.onStart(viewerId = viewer)
    }

    override fun onStop() {
        super.onStop()
        vm.onStop()
    }

    private fun render(state: ScreenState) {
        when (state) {
            ScreenState.READ -> {
                with(binding) {
                    btnEditSortOrDone.setText(R.string.edit)
                    btnAdd.visible()
                    viewerSortRecycler.apply {
                        removeItemDecoration(dividerItemEdit)
                        addItemDecoration(dividerItem)
                    }
                    txtEmptyState.gone()
                }
            }
            ScreenState.EDIT -> {
                with(binding) {
                    btnEditSortOrDone.setText(R.string.done)
                    btnAdd.invisible()
                    viewerSortRecycler.apply {
                        removeItemDecoration(dividerItem)
                        addItemDecoration(dividerItemEdit)
                    }
                    txtEmptyState.gone()
                }
            }
            ScreenState.EMPTY -> {
                with(binding) {
                    txtEmptyState.visible()
                    btnEditSortOrDone.text = ""
                    btnAdd.visible()
                }
            }
        }
    }

    override fun injectDependencies() {
        componentManager()
            .viewerSortComponent
            .get(
                params = DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().viewerSortComponent.release()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentViewerSortBinding = FragmentViewerSortBinding.inflate(
        inflater, container, false
    )

    companion object {
        fun new(ctx: Id, space: Id, viewer: Id): ViewerSortFragment = ViewerSortFragment().apply {
            arguments = bundleOf(
                CTX_KEY to ctx,
                SPACE_ID_KEY to space,
                VIEWER_ID_KEY to viewer
            )
        }

        const val CTX_KEY = "arg.viewer-sort.ctx"
        const val SPACE_ID_KEY = "arg.viewer-sort.space"
        const val VIEWER_ID_KEY = "arg.viewer-sort.viewer"
    }
}