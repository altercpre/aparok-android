package io.aparok.app.ui.auth.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import io.aparok.app.R
import io.aparok.app.core_ui.foundation.Warning
import io.aparok.app.core_utils.ui.BaseBottomSheetComposeFragment
import io.aparok.app.ui.settings.typography

class DeleteAccountWarning : BaseBottomSheetComposeFragment() {

    var onDeletionAccepted: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme(typography = typography) {
                    Warning(
                        actionButtonText = stringResource(R.string.delete),
                        cancelButtonText = stringResource(R.string.cancel),
                        title = stringResource(R.string.are_you_sure_to_delete_account),
                        subtitle = stringResource(R.string.deleted_account_warning_msg),
                        onNegativeClick = { dismiss() },
                        onPositiveClick = { onDeletionAccepted() },
                        isInProgress = false
                    )
                }
            }
        }
    }


    override fun injectDependencies() {}
    override fun releaseDependencies() {}
}