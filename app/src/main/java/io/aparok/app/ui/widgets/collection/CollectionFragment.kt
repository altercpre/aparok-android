package io.aparok.app.ui.widgets.collection

import android.os.Build
import io.aparok.app.core_utils.intents.ActivityCustomTabsHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.aparok.app.BuildConfig
import io.aparok.app.R
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_utils.ext.arg
import io.aparok.app.core_utils.ext.argString
import io.aparok.app.core_utils.ext.toast
import io.aparok.app.core_utils.insets.EDGE_TO_EDGE_MIN_SDK
import io.aparok.app.core_utils.ui.BaseComposeFragment
import io.aparok.app.core_utils.ui.proceed
import io.aparok.app.di.common.componentManager
import io.aparok.app.presentation.widgets.collection.CollectionViewModel
import io.aparok.app.presentation.widgets.collection.CollectionViewModel.Command
import io.aparok.app.presentation.widgets.collection.Subscription
import io.aparok.app.presentation.widgets.collection.SubscriptionMapper
import io.aparok.app.ui.base.navigation
import io.aparok.app.ui.dashboard.DeleteAlertFragment
import io.aparok.app.ui.home.WidgetsScreenFragment
import io.aparok.app.ui.multiplayer.ShareSpaceFragment
import javax.inject.Inject
import timber.log.Timber

class CollectionFragment : BaseComposeFragment() {

    @Inject
    lateinit var factory: CollectionViewModel.Factory

    private val navigation get() = navigation()

    private val space get() = arg<Id>(SPACE_ID_KEY)

    private val subscription: Subscription by lazy {
        SubscriptionMapper().map(
            argString(
                SUBSCRIPTION_KEY
            )
        )
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
                DefaultTheme {
                    CollectionScreen(vm = vm)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        proceed(vm.commands) { execute(it) }
        vm.onStart(subscription)
    }

    private fun execute(command: Command) {
        when (command) {
            is Command.LaunchDocument -> launchDocument(
                target = command.target,
                space = command.space
            )
            is Command.LaunchObjectSet -> launchObjectSet(
                target = command.target,
                space = command.space
            )
            is Command.Exit -> exit()
            is Command.ConfirmRemoveFromBin -> confirmRemoveFromBin(command)
            is Command.OpenCollection -> navigation.launchCollections(
                subscription = command.subscription,
                space = space
            )
            is Command.OpenChat -> navigation.openChat(
                target = command.target,
                space = command.space
            )
            is Command.ToDesktop -> navigation.exitToDesktop(space = space)
            is Command.ToSearch -> navigation.openGlobalSearch(
                space = command.space
            )
            is Command.ToSpaceHome -> {
                navigation().exitToSpaceHome()
            }
            is Command.ExitToSpaceWidgets -> {
                runCatching {
                    findNavController().navigate(
                        R.id.actionExitToSpaceWidgets,
                        WidgetsScreenFragment.args(space = space)
                    )
                }.onFailure {
                    Timber.e(it, "Error while opening space switcher from full-screen widget")
                }
            }
            is Command.OpenDateObject -> {
                runCatching {
                    navigation().openDateObject(
                        objectId = command.target,
                        space = command.space
                    )
                }.onFailure { e ->
                    Timber.e(e, "Error while opening date object from Collection screen")
                }
            }
            is Command.OpenTypeObject -> {
                runCatching {
                    navigation().openObjectType(
                        objectId = command.target,
                        space = command.space
                    )
                }.onFailure {
                    Timber.e(it, "Error while opening object type from expanded widget screen")
                }
            }
            is Command.OpenShareScreen -> {
                runCatching {
                    findNavController().navigate(
                        R.id.shareSpaceScreen,
                        args = ShareSpaceFragment.args(command.space)
                    )
                }.onFailure {
                    Timber.e(it, "Error while opening share screen")
                }
            }

            is Command.OpenParticipant -> {
                runCatching {
                    navigation().openParticipantObject(
                        objectId = command.target,
                        space = command.space
                    )
                }.onFailure { e ->
                    Timber.e(e, "Error while opening participant object from Collection screen")
                }
            }

            is Command.OpenUrl -> {
                try {
                    ActivityCustomTabsHelper.openUrl(
                        activity = requireActivity(),
                        url = command.url
                    )
                } catch (e: Throwable) {
                    Timber.e(e, "Error opening bookmark URL: ${command.url}")
                    toast("Failed to open URL")
                }
            }
        }
    }

    private fun confirmRemoveFromBin(command: Command.ConfirmRemoveFromBin) {
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
        navigation.launchDocument(
            target = target,
            space = space
        )
    }

    override fun onStop() {
        vm.onStop()
        super.onStop()
    }

    override fun injectDependencies() {
        val vmParams = CollectionViewModel.VmParams(spaceId = SpaceId(space))
        componentManager().collectionComponent.get(params = vmParams).inject(this)
    }

    override fun releaseDependencies() {
        componentManager().collectionComponent.release()
    }

    override fun onApplyWindowRootInsets(view: View) {
        if (BuildConfig.USE_EDGE_TO_EDGE && Build.VERSION.SDK_INT >= EDGE_TO_EDGE_MIN_SDK) {
            // Do nothing.
        } else {
            super.onApplyWindowRootInsets(view)
        }
    }

    companion object {
        const val SUBSCRIPTION_KEY: String = "arg.collection.subscription"
        const val SPACE_ID_KEY = "arg.collection.space-id"
        fun args(subscription: Id, space: Id) = bundleOf(
            SUBSCRIPTION_KEY to subscription,
            SPACE_ID_KEY  to space
        )
    }
}