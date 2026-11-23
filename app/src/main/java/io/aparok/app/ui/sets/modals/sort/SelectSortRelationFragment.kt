package io.aparok.app.ui.sets.modals.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_ui.features.sets.SearchRelationAdapter
import io.aparok.app.core_ui.reactive.textChanges
import io.aparok.app.core_utils.ext.arg
import io.aparok.app.core_utils.ext.drawable
import io.aparok.app.core_utils.ext.invisible
import io.aparok.app.core_utils.ext.subscribe
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.core_utils.ui.BaseBottomSheetTextInputFragment
import io.aparok.app.databinding.FragmentSelectSortOrFilterRelationBinding
import io.aparok.app.di.common.componentManager
import io.aparok.app.di.feature.DefaultComponentParam
import io.aparok.app.presentation.sets.SelectSortRelationViewModel
import javax.inject.Inject

class SelectSortRelationFragment : BaseBottomSheetTextInputFragment<FragmentSelectSortOrFilterRelationBinding>() {

    private val ctx: String get() = arg(CTX_KEY)
    private val space: String get() = arg(SPACE_ID_KEY)
    private val viewer: String get() = arg(VIEWER_ID_KEY)

    private val searchRelationAdapter by lazy {
        SearchRelationAdapter { relation ->
            vm.onRelationClicked(ctx = ctx, viewerId = viewer, relation = relation)
        }
    }

    lateinit var searchRelationInput: EditText
    lateinit var clearSearchText: View

    @Inject
    lateinit var factory: SelectSortRelationViewModel.Factory

    private val vm: SelectSortRelationViewModel by viewModels { factory }

    override val textInput: EditText get() = binding.searchBar.root.findViewById(R.id.filterInputField)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchRelationInput = binding.searchBar.root.findViewById(R.id.filterInputField)
        searchRelationInput.apply {
            hint = getString(R.string.choose_relation_to_sort)
        }
        clearSearchText = binding.searchBar.root.findViewById(R.id.clearSearchText)
        clearSearchText.setOnClickListener {
            searchRelationInput.setText("")
            clearSearchText.invisible()

        }
        binding.searchRelationRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchRelationAdapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(drawable(R.drawable.divider_sort_or_filter_relation))
                }
            )
        }
        with(lifecycleScope) {
            subscribe(searchRelationInput.textChanges()) {
                vm.onSearchQueryChanged(it.toString())
                if (it.isEmpty()) {
                    clearSearchText.invisible()
                } else {
                    clearSearchText.visible()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vm.onStart(viewer)
    }

    override fun onStop() {
        super.onStop()
        vm.onStop()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(lifecycleScope) {
            subscribe(vm.views) { searchRelationAdapter.update(it) }
            subscribe(vm.isDismissed) { isDismissed -> if (isDismissed) dismiss() }
        }
    }

    override fun injectDependencies() {
        componentManager()
            .selectSortRelationComponent
            .get(
                params = DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().selectSortRelationComponent.release()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectSortOrFilterRelationBinding =
        FragmentSelectSortOrFilterRelationBinding.inflate(
            inflater, container, false
        )

    companion object {

        fun new(ctx: Id, space: Id, viewerId: Id): SelectSortRelationFragment = SelectSortRelationFragment().apply {
            arguments = bundleOf(CTX_KEY to ctx, SPACE_ID_KEY to space, VIEWER_ID_KEY to viewerId)
        }

        const val CTX_KEY = "arg.select-sort-relation.ctx"
        const val SPACE_ID_KEY = "arg.select-sort-relation.space-id"
        const val VIEWER_ID_KEY = "arg.select-sort-relation.viewer"
    }
}
