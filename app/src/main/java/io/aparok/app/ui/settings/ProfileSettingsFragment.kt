package io.aparok.app.ui.settings

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.aparok.app.R
import io.aparok.app.analytics.base.EventsDictionary
import io.aparok.app.core_models.ui.AccountProfile
import io.aparok.app.core_ui.common.ComposeDialogView
import io.aparok.app.core_ui.extensions.throttledClick
import io.aparok.app.core_ui.features.profile.ProfileQrCodeScreen
import io.aparok.app.core_utils.ext.GetImageContract
import io.aparok.app.core_utils.ext.Mimetype
import io.aparok.app.core_utils.ext.cancel
import io.aparok.app.core_utils.ext.openNotificationSettings
import io.aparok.app.core_utils.ext.parseImagePath
import io.aparok.app.core_utils.ext.shareFile
import io.aparok.app.core_utils.ext.subscribe
import io.aparok.app.core_utils.ext.toast
import io.aparok.app.core_utils.tools.FeatureToggles
import io.aparok.app.core_utils.ui.BaseBottomSheetComposeFragment
import io.aparok.app.device.launchMediaPicker
import io.aparok.app.di.common.componentManager
import io.aparok.app.other.MediaPermissionHelper
import io.aparok.app.ui.profile.KeychainPhraseDialog
import io.aparok.app.ui_settings.account.NotificationSettingsScreen
import io.aparok.app.ui_settings.account.ProfileSettingsScreen
import io.aparok.app.ui_settings.account.ProfileSettingsViewModel
import javax.inject.Inject
import timber.log.Timber

class ProfileSettingsFragment : BaseBottomSheetComposeFragment() {

    @Inject
    lateinit var factory: ProfileSettingsViewModel.Factory

    @Inject
    lateinit var toggles: FeatureToggles

    private val vm by viewModels<ProfileSettingsViewModel> { factory }

    private val onKeychainPhraseClicked = {
        val bundle =
            bundleOf(KeychainPhraseDialog.ARG_SCREEN_TYPE to EventsDictionary.Type.screenSettings)
        safeNavigate(R.id.keychainDialog, bundle)
    }

    private val onLogoutClicked = {
        // Logout removed in sovereign offline mode
    }

    private lateinit var permissionHelper: MediaPermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionHelper = MediaPermissionHelper(
            fragment = this,
            onPermissionDenied = { toast(R.string.permission_read_denied) },
            onPermissionSuccess = { _, _ -> openGallery() }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeDialogView(
            context = requireContext(),
            dialog = requireDialog()
        ).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme(typography = typography) {
                    var showNotificationSettingsModal by remember { mutableStateOf(false) }
                    val notificationsDisabled = vm.notificationsDisabled.collectAsStateWithLifecycle().value
                    val accountProfile = vm.profileData.collectAsStateWithLifecycle().value
                    ProfileSettingsScreen(
                        onKeychainPhraseClicked = onKeychainPhraseClicked,
                        onLogoutClicked = onLogoutClicked,
                        isLogoutInProgress = vm.isLoggingOut.collectAsState().value,
                        onNameChange = { vm.onNameChange(it) },
                        onProfileIconClick = { proceedWithIconClick() },
                        account = accountProfile,
                        onAppearanceClicked = throttledClick(
                            onClick = {
                                findNavController().navigate(R.id.appearanceScreen)
                            }
                        ),
                        onDataManagementClicked = throttledClick(
                            onClick = {
                                findNavController().navigate(R.id.filesStorageScreen)
                            }
                        ),
                        onAboutClicked = throttledClick(
                            onClick = {
                                findNavController().navigate(R.id.aboutAppScreen)
                            }
                        ),
                        onSpacesClicked = throttledClick(
                            onClick = {
                                runCatching {
                                    findNavController().navigate(R.id.spaceListScreen)
                                }
                            }
                        ),
                        clearProfileImage = { vm.onClearProfileImage() },
                        onDebugClicked = {},
                        onExperimentalFeaturesClicked = {},
                        isDebugEnabled = false,
                        showExperimentalFeatures = false,
                        onMiscSectionClicked = vm::onMiscSectionClicked,
                        notificationsDisabled = notificationsDisabled,
                        onOpenNotificationSettings = { showNotificationSettingsModal = true },
                        onMySitesClicked = {},
                        onIdentityClicked = {},
                        onShowQrCodeClicked = { vm.onShowQrCodeClicked() }
                    )
                    if (showNotificationSettingsModal) {
                        NotificationSettingsScreen(
                            isDisabled = notificationsDisabled,
                            onDismiss = {
                                showNotificationSettingsModal = false
                            },
                            onOpenSettings = {
                                showNotificationSettingsModal = false
                                requireContext().openNotificationSettings()
                            }
                        )
                    }
                    val profileQrCodeState =
                        vm.profileQrCodeState.collectAsStateWithLifecycle().value
                    ProfileQrCodeScreen(
                        state = profileQrCodeState,
                        onShare = { link -> shareProfileLink(link) },
                        onCopyLink = { link -> copyProfileLink(link) },
                        onScanQrCode = { /* QR scanning disabled in sovereign offline mode */ },
                        onDismiss = { vm.onHideQrCodeScreen() }
                    )
                }
            }
        }
    }

    private fun shareProfileLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }
        startActivity(Intent.createChooser(intent, null))
    }

    private fun copyProfileLink(link: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Aparok Profile Link", link)
        clipboard.setPrimaryClip(clip)
        toast(R.string.profile_qr_link_copied)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skipCollapsed()
        expand()
    }

    override fun onStart() {
        super.onStart()
        subscribe(vm.toasts) {
            toast(it)
        }
        jobs += lifecycleScope.subscribe(vm.debugSyncReportUri) { uri ->
            if (uri != null) {
                shareFile(uri)
            }
        }
        jobs += lifecycleScope.subscribe(vm.commands) { command ->
            when (command) {
                is ProfileSettingsViewModel.Command.ShareLink -> {
                    shareProfileLink(command.link)
                }

                is ProfileSettingsViewModel.Command.ProcessScannedQrCode -> {
                    // Dismiss the bottom sheet and let the activity handle the deep link
                    dismiss()
                    handleScannedQrCode(command.qrCode)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        jobs.cancel()
    }

    override fun onResume() {
        super.onResume()
        vm.refreshPermissionState()
    }

    private fun proceedWithIconClick() {
        launchMediaPicker(
            pickMedia = pickMedia,
            permissionHelper = permissionHelper,
            mediaType = PickVisualMedia.ImageOnly,
            fallbackMimeType = Mimetype.MIME_IMAGE_ALL
        )
    }

    private fun openGallery() {
        getContent.launch(SELECT_IMAGE_CODE)
    }

    val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            val path = uri.parseImagePath(requireContext())
            if (path != null) {
                vm.onPickedImageFromDevice(path = path)
            } else {
                toast("Error while parsing path for media file")
            }
        } else {
            Timber.i("No media selected")
        }
    }

    private val getContent = registerForActivityResult(GetImageContract()) { uri: Uri? ->
        if (uri != null) {
            val path = uri.parseImagePath(requireContext())
            if (path != null) {
                vm.onPickedImageFromDevice(path = path)
            } else {
                toast("Error while parsing path for cover image")
            }
        } else {
            Timber.e("Error while upload cover image, URI is null")
        }
    }

    override fun injectDependencies() {
        componentManager().profileComponent.get().inject(this)
    }

    override fun releaseDependencies() {
        componentManager().profileComponent.release()
    }

    companion object {
        const val SPACE_ID_KEY = "arg.profile-settings.space-id"
    }
}

private const val SELECT_IMAGE_CODE = 1
