package io.aparok.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.reactive.clicks
import io.aparok.app.core_utils.ui.BaseBottomSheetFragment
import io.aparok.app.databinding.FragmentClearCacheBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ClearCacheAlertFragment : BaseBottomSheetFragment<FragmentClearCacheBinding>() {

    var onClearAccepted: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSecondarySheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel
            .clicks()
            .onEach { dismiss() }
            .launchIn(lifecycleScope)

        binding.btnClear
            .clicks()
            .onEach {
                onClearAccepted()
                dismiss()
            }
            .launchIn(lifecycleScope)
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentClearCacheBinding = FragmentClearCacheBinding.inflate(
        inflater, container, false
    )
    companion object {
        fun new(): ClearCacheAlertFragment = ClearCacheAlertFragment()
    }
}