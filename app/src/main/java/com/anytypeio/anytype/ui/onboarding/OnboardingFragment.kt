package com.anytypeio.anytype.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.anytypeio.anytype.R
import com.anytypeio.anytype.core_ui.views.BaseAlertDialog
import com.anytypeio.anytype.core_utils.ext.argOrNull
import com.anytypeio.anytype.core_utils.ext.toast
import com.anytypeio.anytype.di.common.componentManager
import com.anytypeio.anytype.ext.daggerViewModel
import com.anytypeio.anytype.presentation.onboarding.OnboardingStartViewModel
import com.anytypeio.anytype.presentation.onboarding.OnboardingViewModel
import com.anytypeio.anytype.ui.editor.EditorFragment
import com.anytypeio.anytype.ui.home.WidgetsScreenFragment
import com.anytypeio.anytype.ui.onboarding.screens.OnboardingProfileScreen
import com.anytypeio.anytype.ui.vault.VaultFragment
import javax.inject.Inject
import timber.log.Timber

class OnboardingFragment : Fragment() {

    private val deepLink: String? get() = argOrNull(ONBOARDING_DEEP_LINK_KEY)

    @Inject
    lateinit var factory: OnboardingViewModel.Factory

    private val onBoardingViewModel by viewModels<OnboardingViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        OnboardingScreen()
    }

    @Composable
    private fun OnboardingScreen() {
        MaterialTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.background_primary))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(insets = WindowInsets.systemBars)
                ) {
                    SingleScreenOnboarding()
                }
            }
            LaunchedEffect(Unit) {
                onBoardingViewModel.toasts.collect {
                    toast(it)
                }
            }
        }
    }

    @Composable
    private fun SingleScreenOnboarding() {
        val component = componentManager().onboardingStartComponent
        val vm = daggerViewModel { component.get().getViewModel() }

        OnboardingProfileScreen(
            isLoading = vm.isLoadingState.collectAsStateWithLifecycle().value,
            onCreateProfileClicked = vm::onCreateProfileClicked,
            onRestoreBackupClicked = {
                toast("Restore from backup will be available in Aparok Backup/Restore.")
            }
        )

        ErrorScreen(vm = vm)

        LaunchedEffect(Unit) {
            vm.navigation.collect { navigation ->
                when (navigation) {
                    is OnboardingStartViewModel.AuthNavigation.ProceedWithSignUp -> {
                        runCatching {
                            findNavController().navigate(
                                R.id.actionOpenVault,
                                VaultFragment.args(deepLink)
                            )
                            findNavController().navigate(
                                R.id.actionOpenSpaceFromVault,
                                WidgetsScreenFragment.args(
                                    space = navigation.spaceId,
                                    deeplink = null
                                )
                            )
                            if (navigation.startingObjectId != null) {
                                findNavController().navigate(
                                    R.id.objectNavigation,
                                    EditorFragment.args(
                                        ctx = navigation.startingObjectId,
                                        space = navigation.spaceId,
                                    )
                                )
                            }
                        }.onFailure {
                            Timber.e(it, "Error while navigating to vault from onboarding")
                        }
                    }
                    is OnboardingStartViewModel.AuthNavigation.ProceedWithSignIn -> {
                        // Sovereign offline mode: no remote sign in
                    }
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose { vm.cancelLoadingState() }
        }
        DisposableEffect(Unit) {
            onDispose { component.release() }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ErrorScreen(vm: OnboardingStartViewModel) {
        val errorState = vm.errorState.collectAsStateWithLifecycle().value

        val errorMessage = when (errorState) {
            OnboardingStartViewModel.ErrorState.Hidden -> null
            is OnboardingStartViewModel.ErrorState.Generic -> errorState.message
            is OnboardingStartViewModel.ErrorState.WalletSetupError -> stringResource(id = R.string.error_wallet_setup)
            OnboardingStartViewModel.ErrorState.NetworkError -> stringResource(id = R.string.onboarding_error_network_error)
            OnboardingStartViewModel.ErrorState.OfflineDevice -> stringResource(id = R.string.onboarding_error_offline_device)
            OnboardingStartViewModel.ErrorState.AccountCreatedButFailedToStartNode -> stringResource(id = R.string.onboarding_error_account_created_but_failed_to_start_node)
            OnboardingStartViewModel.ErrorState.AccountCreatedButFailedToSetName -> stringResource(id = R.string.onboarding_error_account_created_but_failed_to_set_name)
            OnboardingStartViewModel.ErrorState.FailedToStopRunningNode -> stringResource(id = R.string.onboarding_error_failed_to_stop_running_node)
            OnboardingStartViewModel.ErrorState.FailedToWriteConfig -> stringResource(id = R.string.onboarding_error_failed_to_write_config)
            OnboardingStartViewModel.ErrorState.FailedToCreateLocalRepo -> stringResource(id = R.string.onboarding_error_failed_to_create_local_repo)
            OnboardingStartViewModel.ErrorState.AccountCreationCanceled -> stringResource(id = R.string.onboarding_error_account_creation_canceled)
            OnboardingStartViewModel.ErrorState.ConfigFileNotFound -> stringResource(id = R.string.onboarding_error_config_file_not_found)
            OnboardingStartViewModel.ErrorState.ConfigFileInvalid -> stringResource(id = R.string.onboarding_error_config_file_invalid)
            OnboardingStartViewModel.ErrorState.ConfigFileNetworkIdMismatch -> stringResource(id = R.string.onboarding_error_config_file_network_id_mismatch)
        }

        errorMessage?.let { message ->
            BaseAlertDialog(
                dialogText = message,
                buttonText = stringResource(id = R.string.common_ok),
                onButtonClick = {
                    vm.onErrorDismissed()
                },
                onDismissRequest = {
                    vm.onErrorDismissed()
                }
            )
        }
    }

    private fun injectDependencies() {
        componentManager().onboardingComponent.get().inject(this)
    }

    private fun releaseDependencies() {
        with(componentManager()) {
            onboardingComponent.release()
            onboardingStartComponent.release()
        }
    }

    companion object {
        fun args(deepLink: String?) = bundleOf(
            ONBOARDING_DEEP_LINK_KEY to deepLink
        )
        private const val ONBOARDING_DEEP_LINK_KEY = "arg.onboarding.deep-link-key"
    }
}
