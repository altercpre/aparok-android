package io.aparok.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_utils.ext.arg
import io.aparok.app.core_utils.ext.argString
import io.aparok.app.core_utils.ext.setupBottomSheetBehavior
import io.aparok.app.core_utils.ext.toast
import io.aparok.app.core_utils.ui.BaseBottomSheetComposeFragment
import io.aparok.app.core_utils.ui.proceed
import io.aparok.app.di.common.componentManager
import io.aparok.app.presentation.widgets.collection.CollectionViewModel
import io.aparok.app.presentation.widgets.collection.Subscription
import io.aparok.app.presentation.widgets.collection.SubscriptionMapper
import io.aparok.app.ui.base.navigation
import io.aparok.app.ui.dashboard.DeleteAlertFragment
import io.aparok.app.ui.settings.remote.RemoteFilesManageScreen
import javax.inject.Inject
import timber.log.Timber

class RemoteFilesManageFragment : BaseBottomSheetComposeFragment() {

    @Inject
    lateinit var factory: CollectionViewModel.Factory

    private val navigation get() = navigation()

    private val space get() = arg<Id>(SPACE_ID_KEY)

    private val subscription: Subscription by lazy {
        SubscriptionMapper().map(argString(SUBSCRIPTION_KEY))
    }

    private val vm by viewModels<CollectionViewModel> { factory }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme(typography = typography) {
                    RemoteFilesManageScreen(
                        vm = vm,
                        scope = lifecycleScope
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetBehavior(PADDING_TOP)
    }

    override fun onStart() {
        super.onStart()
        proceed(vm.commands) { execute(it) }
        proceed(vm.toasts) { toast(it) }
        vm.onStart(subscription)
    }

    private fun execute(command: CollectionViewModel.Command) {
        when (command) {
            is CollectionViewModel.Command.LaunchDocument -> launchDocument(
                target = command.target,
                space = command.space
            )
            is CollectionViewModel.Command.LaunchObjectSet -> launchObjectSet(
                target = command.target,
                space = command.space
            )
            is CollectionViewModel.Command.ConfirmRemoveFromBin -> confirmRemoveFromBin(command)
            is CollectionViewModel.Command.OpenCollection -> navigation.launchCollections(
                subscription = command.subscription,
                space = command.space
            )
            is CollectionViewModel.Command.OpenChat -> navigation.openChat(
                space = command.space,
                target = command.target
            )
            is CollectionViewModel.Command.ToDesktop -> navigation.exitToDesktop(space)
            is CollectionViewModel.Command.ToSearch -> {
                // Do nothing.
            }
            is CollectionViewModel.Command.Exit -> exit()
            is CollectionViewModel.Command.ToSpaceHome -> {
                // Do nothing.
            }
            is CollectionViewModel.Command.ExitToSpaceWidgets -> {
                // Do nothing
            }
            is CollectionViewModel.Command.OpenDateObject -> {
                // Do nothing
            }
            is CollectionViewModel.Command.OpenShareScreen -> {
                // Do nothing
            }
            is CollectionViewModel.Command.OpenParticipant -> {
                // Do nothing
            }
            is CollectionViewModel.Command.OpenTypeObject -> {
                // Do nothing
            }
            is CollectionViewModel.Command.OpenUrl -> {
                // Do nothing for remote files
            }
        }
    }

    private fun confirmRemoveFromBin(command: CollectionViewModel.Command.ConfirmRemoveFromBin) {
        val dialog = DeleteAlertFragment.new(command.count)
        dialog.onDeletionAccepted = { vm.onDeletionAccepted() }
        dialog.showChildFragment()
    }

    private fun exit() {
        navigation.exit(space)
    }

    private fun launchObjectSet(target: Id, space: Id) {
        navigation.launchObjectSet(
            target = target,
            space = space
        )
    }

    private fun launchDocument(target: Id, space: Id) {
        navigation.launchDocument(target = target, space = space)
    }

    override fun onStop() {
        vm.onStop()
        jobs.forEach { it.cancel() }
        jobs.clear()
        super.onStop()
    }

    override fun injectDependencies() {
        val vmParams = CollectionViewModel.VmParams(
            spaceId = SpaceId(space)
        )
        componentManager().collectionComponent.get(vmParams).inject(this)
    }

    override fun releaseDependencies() {
        componentManager().collectionComponent.release()
    }

    companion object {
        const val SUBSCRIPTION_KEY: String = "arg.space.files.subscription.key"
        const val SPACE_ID_KEY = "arg.space.files.space.key"
        fun args(subscription: Id, space: Id) = bundleOf(
            SUBSCRIPTION_KEY to subscription,
            SPACE_ID_KEY  to space
        )
    }
}

private const val PADDING_TOP = 74