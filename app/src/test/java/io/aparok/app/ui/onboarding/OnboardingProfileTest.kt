package io.aparok.app.ui.onboarding

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import io.aparok.app.CrashReporter
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Account
import io.aparok.app.core_models.AccountSetup
import io.aparok.app.core_models.AccountStatus
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Config
import io.aparok.app.core_models.NetworkMode
import io.aparok.app.core_models.NetworkModeConfig
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.interactor.CreateAccount
import io.aparok.app.domain.auth.interactor.SetupWallet
import io.aparok.app.domain.auth.model.Wallet
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.misc.LocaleProvider
import io.aparok.app.domain.`object`.ImportGetStartedUseCase
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.domain.spaces.SetSpaceDetails
import io.aparok.app.domain.subscriptions.GlobalSubscriptionManager
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.persistence.networkmode.DefaultNetworkModeProvider
import io.aparok.app.presentation.onboarding.OnboardingStartViewModel
import io.aparok.app.presentation.spaces.SpaceGradientProvider
import io.aparok.app.ui.onboarding.screens.OnboardingProfileValidator
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config as RoboConfig

@RunWith(RobolectricTestRunner::class)
@RoboConfig(sdk = [Build.VERSION_CODES.P])
class OnboardingProfileTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `production OnboardingProfileValidator trims names and rejects blank or over-limit input`() {
        assertNull(OnboardingProfileValidator.normalizeForSubmit(""))
        assertNull(OnboardingProfileValidator.normalizeForSubmit("   \t\n  "))
        assertEquals("Alice Sovereign", OnboardingProfileValidator.normalizeForSubmit("  Alice Sovereign  "))

        val maxAllowed = "A".repeat(OnboardingProfileValidator.MAX_NAME_LENGTH)
        val overLimit = "A".repeat(OnboardingProfileValidator.MAX_NAME_LENGTH + 1)

        assertTrue(OnboardingProfileValidator.isInputWithinLimit(maxAllowed))
        assertFalse(OnboardingProfileValidator.isInputWithinLimit(overLimit))
    }

    @Test
    fun `production ProfileImageHelper downscales to max 512px PNG and handles missing URI`() {
        val sourceFile = File(context.cacheDir, "onboarding_src_2048x1536.jpg")
        val sourceBitmap = Bitmap.createBitmap(2048, 1536, Bitmap.Config.ARGB_8888)
        sourceBitmap.eraseColor(Color.CYAN)
        FileOutputStream(sourceFile).use { out ->
            sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        sourceBitmap.recycle()

        val savedPath = ProfileImageHelper.processAndSaveProfileImage(context, Uri.fromFile(sourceFile))
        assertNotNull("Valid source image must produce saved avatar path", savedPath)

        val savedFile = File(savedPath!!)
        assertTrue("Saved avatar file must exist in cacheDir", savedFile.exists())
        assertEquals(context.cacheDir.canonicalPath, savedFile.parentFile?.canonicalPath)

        val header = savedFile.inputStream().use { stream ->
            ByteArray(4).also { stream.read(it) }
        }
        assertEquals(0x89.toByte(), header[0])
        assertEquals(0x50.toByte(), header[1])
        assertEquals(0x4E.toByte(), header[2])
        assertEquals(0x47.toByte(), header[3])

        val decoded = BitmapFactory.decodeFile(savedPath)
        assertNotNull(decoded)
        assertEquals(512, decoded.width)
        assertEquals(384, decoded.height)
        decoded.recycle()

        val missingUri = Uri.fromFile(File(context.cacheDir, "non_existent_avatar.jpg"))
        assertNull(ProfileImageHelper.processAndSaveProfileImage(context, missingUri))

        sourceFile.delete()
        savedFile.delete()
    }

    @Test
    fun `production DefaultNetworkModeProvider always enforces NetworkMode LOCAL`() {
        val prefs = context.getSharedPreferences("onboarding_profile_net_mode", Context.MODE_PRIVATE)
        val provider = DefaultNetworkModeProvider(prefs)

        provider.set(
            NetworkModeConfig(
                networkMode = NetworkMode.CUSTOM,
                userFilePath = "/tmp/custom.yml",
                storedFilePath = "/tmp/stored.yml",
                useReserveMultiplexLib = true
            )
        )
        provider.clear()

        val config = provider.get()
        assertEquals(NetworkMode.LOCAL, config.networkMode)
        assertNull(config.userFilePath)
        assertNull(config.storedFilePath)
        assertFalse(config.useReserveMultiplexLib)
    }

    @Test
    fun `production OnboardingStartViewModel onCreateProfileClicked trims name and creates local account`() = runBlocking {
        val dispatchers = AppCoroutineDispatchers(
            io = Dispatchers.Unconfined,
            computation = Dispatchers.Unconfined,
            main = Dispatchers.Unconfined
        )
        val authRepository = mock<AuthRepository>()
        val blockRepository = mock<BlockRepository>()
        val configStorage = mock<ConfigStorage>()
        val initialParamsProvider = mock<InitialParamsProvider> {
            on { toCommand() } doReturn Command.SetInitialParams(
                version = "1.4.0",
                platform = "android",
                workDir = context.filesDir.absolutePath,
                defaultLogLevel = "INFO"
            )
        }
        val spaceManager = mock<SpaceManager>()
        val analytics = mock<Analytics>()
        val pathProvider = mock<PathProvider> {
            on { providePath() } doReturn context.filesDir.absolutePath
        }
        val crashReporter = mock<CrashReporter>()
        val localeProvider = mock<LocaleProvider> {
            on { language() } doReturn "en"
        }
        val globalSubscriptionManager = mock<GlobalSubscriptionManager>()
        val stringResourceProvider = mock<StringResourceProvider> {
            on { getInitialSpaceName() } doReturn "My Space"
        }

        val expectedConfig = Config(
            home = "home_1",
            profile = "profile_1",
            gateway = "http://127.0.0.1:47800",
            space = "space_1",
            techSpace = "tech_1",
            spaceView = "sv_1",
            widgets = "widgets_1",
            analytics = "analytics_1",
            device = "device_1",
            network = "network_1",
            workspaceObjectId = "ws_1",
            spaceChatId = null
        )

        whenever(authRepository.createWallet(any())).thenReturn(Wallet("test sovereign mnemonic"))
        whenever(authRepository.getNetworkMode()).thenReturn(
            NetworkModeConfig(
                networkMode = NetworkMode.LOCAL,
                userFilePath = null,
                storedFilePath = null,
                useReserveMultiplexLib = false
            )
        )
        whenever(authRepository.createAccount(any())).thenReturn(
            AccountSetup(
                account = Account("acc_1"),
                config = expectedConfig,
                status = AccountStatus.Active
            )
        )
        whenever(spaceManager.set(eq("space_1"), eq(false))).thenReturn(Result.success(expectedConfig))
        whenever(blockRepository.importGetStartedUseCase("space_1")).thenReturn(
            Command.ImportUseCase.Result(startingObject = "start_obj_1")
        )

        val setupWallet = SetupWallet(authRepository, dispatchers)
        val createAccount = CreateAccount(
            repository = authRepository,
            configStorage = configStorage,
            initialParamsProvider = initialParamsProvider,
            awaitAccountStartManager = AwaitAccountStartManager.Default,
            spaceManager = spaceManager,
            dispatcher = dispatchers
        )
        val importGetStartedUseCase = ImportGetStartedUseCase(blockRepository, dispatchers)
        val setSpaceDetails = SetSpaceDetails(dispatchers, blockRepository)

        val vm = OnboardingStartViewModel(
            configStorage = configStorage,
            analytics = analytics,
            createAccount = createAccount,
            setupWallet = setupWallet,
            importGetStartedUseCase = importGetStartedUseCase,
            pathProvider = pathProvider,
            spaceGradientProvider = SpaceGradientProvider.Default,
            crashReporter = crashReporter,
            localeProvider = localeProvider,
            globalSubscriptionManager = globalSubscriptionManager,
            spaceManager = spaceManager,
            setSpaceDetails = setSpaceDetails,
            stringResourceProvider = stringResourceProvider
        )

        val navigations = mutableListOf<OnboardingStartViewModel.AuthNavigation>()
        val navJob = CoroutineScope(Dispatchers.Unconfined).launch {
            vm.navigation.collect { navigations.add(it) }
        }

        vm.onCreateProfileClicked("  Alice Sovereign  ", "/cache/avatar_512.png")
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        navJob.cancel()

        verify(authRepository).createAccount(
            argThat {
                name == "Alice Sovereign" &&
                    avatarPath == "/cache/avatar_512.png" &&
                    networkMode == NetworkMode.LOCAL
            }
        )
        assertEquals(
            listOf(
                OnboardingStartViewModel.AuthNavigation.ProceedWithSignUp(
                    spaceId = "space_1",
                    startingObjectId = "start_obj_1",
                    profileId = "profile_1"
                )
            ),
            navigations
        )
    }
}
