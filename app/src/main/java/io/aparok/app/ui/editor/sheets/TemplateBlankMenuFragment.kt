package io.aparok.app.ui.editor.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.aparok.app.R
import io.aparok.app.core_ui.features.objects.ObjectActionAdapter
import io.aparok.app.core_ui.layout.SpacingItemDecoration
import io.aparok.app.core_utils.ext.withParent
import io.aparok.app.core_utils.ui.BaseBottomSheetFragment
import io.aparok.app.databinding.FragmentTemplateBlankMenuBinding
import io.aparok.app.presentation.objects.ObjectAction

class TemplateBlankMenuFragment : BaseBottomSheetFragment<FragmentTemplateBlankMenuBinding>() {

    private val actionAdapter by lazy {
        ObjectActionAdapter { _ ->
            dismiss()
            withParent<SetAsDefaultListener> { onSetAsDefaultClicked() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvActions.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = actionAdapter
            addItemDecoration(
                SpacingItemDecoration(
                    firstItemSpacingStart = resources.getDimension(R.dimen.dp_8).toInt(),
                    lastItemSpacingEnd = resources.getDimension(R.dimen.dp_8).toInt()
                )
            )
        }
        actionAdapter.submitList(listOf(ObjectAction.SET_AS_DEFAULT))
    }

    override fun injectDependencies() {}

    override fun releaseDependencies() {}

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTemplateBlankMenuBinding {
        return FragmentTemplateBlankMenuBinding.inflate(
            inflater, container, false
        )
    }
}

interface SetAsDefaultListener {
    fun onSetAsDefaultClicked()
}