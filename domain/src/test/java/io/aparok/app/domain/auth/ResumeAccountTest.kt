package io.aparok.app.domain.auth

import io.aparok.app.core_models.CoroutineTestRule
import io.aparok.app.core_models.StubConfig
import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.interactor.ResumeAccount
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.device.PathProvider
import io.aparok.app.domain.platform.InitialParamsProvider
import io.aparok.app.domain.workspace.SpaceManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ResumeAccountTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var rule = CoroutineTestRule()

    @Mock
    lateinit var repo: AuthRepository

    @Mock
    lateinit var configStorage: ConfigStorage

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var userSettingsRepository: UserSettingsRepository

    @Mock
    lateinit var pathProvider: PathProvider

    @Mock
    lateinit var initialParamsProvider: InitialParamsProvider

    @Mock
    lateinit var awaitAccountStartManager: AwaitAccountStartManager

    lateinit var resumeAccount: ResumeAccount

    private val config = StubConfig()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        resumeAccount = ResumeAccount(
            repository = repo,
            configStorage = configStorage,
            pathProvider = pathProvider,
            initialParamsProvider = initialParamsProvider,
            awaitAccountStartManager = awaitAccountStartManager,
            spaceManager = spaceManager,
            settings = userSettingsRepository
        )
    }
}