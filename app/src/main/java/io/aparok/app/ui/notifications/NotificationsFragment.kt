package io.aparok.app.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import io.aparok.app.R
import io.aparok.app.analytics.base.EventsDictionary
import io.aparok.app.core_ui.notifications.NotificationsScreen
import io.aparok.app.core_utils.ext.subscribe
import io.aparok.app.core_utils.ui.BaseBottomSheetComposeFragment
import io.aparok.app.di.common.componentManager
import io.aparok.app.presentation.notifications.NotificationsViewModel
import io.aparok.app.presentation.notifications.NotificationsViewModelFactory
import io.aparok.app.ui.multiplayer.ShareSpaceFragment
import io.aparok.app.ui.settings.typography
import javax.inject.Inject
import timber.log.Timber

class NotificationsFragment : BaseBottomSheetComposeFragment() {

    @Inject
    lateinit var factory: NotificationsViewModelFactory

    private val vm by viewModels<NotificationsViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MaterialTheme(
                typography = typography
            ) {
                NotificationsScreen(
                    state = vm.state.collectAsStateWithLifecycle().value,
                    onActionButtonClick = vm::onNavigateToSpaceClicked,
                    onErrorButtonClick = vm::onErrorButtonClick,
                    onNotificationAction = vm::onNotificationAction
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        jobs += subscribe(vm.command) { command ->
            Timber.d("GalleryInstallationFragment command: $command")
            when (command) {
                is NotificationsViewModel.Command.Dismiss -> {
                    dismiss()
                }
                is NotificationsViewModel.Command.ViewSpaceJoinRequest -> {
                    // Join requests removed in Aparok sovereign offline build
                }
                is NotificationsViewModel.Command.ViewSpaceLeaveRequest -> {
                    runCatching {
                        findNavController().navigate(
                            R.id.shareSpaceScreen,
                            ShareSpaceFragment.args(space = command.space)
                        )
                    }.onFailure {
                        Timber.e(it, "Error while navigation")
                    }
                }
                is NotificationsViewModel.Command.NavigateToSpace -> {
                    //TODO: implement navigation
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    override fun injectDependencies() {
        componentManager().notificationsComponent.get().inject(this)
    }

    override fun releaseDependencies() {
        componentManager().notificationsComponent.release()
    }
}