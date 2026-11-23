package io.aparok.app.core_ui.widgets.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import io.aparok.app.core_ui.databinding.WidgetMainBottomToolbarBinding
import io.aparok.app.core_ui.common.DEFAULT_DISABLED_ALPHA
import io.aparok.app.core_ui.common.FULL_ALPHA
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.presentation.navigation.NavPanelState
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.reactive.clicks

class MainBottomToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    val binding = WidgetMainBottomToolbarBinding.inflate(
        LayoutInflater.from(context), this
    )

    init {
        isBaselineAligned = false
        orientation = HORIZONTAL
    }

    fun searchClicks() = binding.btnSearch.clicks()
    fun addDocClicks() = binding.btnAddDoc.clicks()
    fun shareClicks() = binding.btnShare.clicks()
    fun homeClicks() = binding.btnHome.clicks()

    fun setState(state: NavPanelState) {
        updateCreate(state)
        updateLeft(state)
    }

    private fun updateCreate(state: NavPanelState) {
        val enabled = when (state) {
            is NavPanelState.Default -> state.isCreateEnabled
            NavPanelState.Init -> false
        }
        with(binding) {
            val alpha = if (enabled) FULL_ALPHA else DEFAULT_DISABLED_ALPHA
            icAddDoc.alpha = alpha
            btnAddDoc.isEnabled = enabled
        }
    }

    private fun updateLeft(state: NavPanelState) {
        with(binding) {
            // Hide all left items by default
            btnShare.gone()
            btnHome.gone()

            // Reset share icon to default
            icShare.setImageResource(R.drawable.ic_nav_panel_add_member)
            icShare.alpha = FULL_ALPHA

            // Determine leftButtonState
            val leftState = when (state) {
                is NavPanelState.Default -> state.left
                NavPanelState.Init -> return
            }

            // Apply visibility & icon based on state
            when (leftState) {
                is NavPanelState.LeftButtonState.AddMembers -> {
                    btnShare.visible()
                    icShare.alpha = if (leftState.isActive) FULL_ALPHA else DEFAULT_DISABLED_ALPHA
                }

                is NavPanelState.LeftButtonState.ViewMembers -> {
                    // Use default icon and alpha
                    btnShare.visible()
                }

                NavPanelState.LeftButtonState.Home -> {
                    btnHome.visible()
                }

                NavPanelState.LeftButtonState.Hidden -> {
                    // All buttons remain hidden (already set to gone at the beginning)
                }
            }
        }
    }
}