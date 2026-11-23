package io.aparok.app.ui.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import io.aparok.app.R
import io.aparok.app.core_ui.extensions.throttledClick
import io.aparok.app.core_ui.foundation.AlertConfig
import io.aparok.app.core_ui.foundation.BUTTON_SECONDARY
import io.aparok.app.core_ui.foundation.GenericAlert
import io.aparok.app.core_utils.ui.BaseBottomSheetComposeFragment
import io.aparok.app.ui.settings.typography

class AlertImportExperienceUnsupported : BaseBottomSheetComposeFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme(typography = typography) {
                    GenericAlert(
                        onFirstButtonClicked = throttledClick(
                            onClick = { dismiss() }
                        ),
                        config = AlertConfig.WithOneButton(
                            title = stringResource(id = R.string.alert_gallery_unsupported_title),
                            description = stringResource(R.string.alert_gallery_unsupported_description),
                            firstButtonText = stringResource(R.string.button_close),
                            firstButtonType = BUTTON_SECONDARY,
                            icon = R.drawable.ic_popup_alert_56
                        )
                    )
                }
            }
        }
    }
}