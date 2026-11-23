package io.aparok.app.domain.auth

import io.aparok.app.domain.account.AwaitAccountStartManager
import io.aparok.app.domain.auth.interactor.Logout
import io.aparok.app.domain.auth.interactor.PreloadRemainingSpaces
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.common.DefaultCoroutineTestRule
import io.aparok.app.domain.config.ConfigStorage
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.launch.PreferredSpaceIdHolder
import io.aparok.app.domain.launch.RemainingSpacesPreloader
import io.aparok.app.domain.workspace.SpaceManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutTest {

    @get:Rule
    val rule = DefaultCoroutineTestRule()

    @Mock lateinit var repo: AuthRepository
    @Mock lateinit var config: ConfigStorage
    @Mock lateinit var user: UserSettingsRepository
    @Mock lateinit var spaceManager: SpaceManager
    @Mock lateinit var awaitAccountStartManager: AwaitAccountStartManager
    @Mock lateinit var preferredSpaceIdHolder: PreferredSpaceIdHolder

    private lateinit var logout: Logout

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val dispatchers = AppCoroutineDispatchers(
            io = rule.dispatcher,
            computation = rule.dispatcher,
            main = rule.dispatcher
        )
        logout = Logout(
            repo = repo,
            config = config,
            user = user,
            spaceManager = spaceManager,
            awaitAccountStartManager = awaitAccountStartManager,
            remainingSpacesPreloader = RemainingSpacesPreloader(
                PreloadRemainingSpaces(dispatchers, repo)
            ),
            preferredSpaceIdHolder = preferredSpaceIdHolder,
            dispatchers = dispatchers
        )
    }

    @Test
    fun `clears preferred space id holder on logout`() = runTest {
        logout(Logout.Params()).collect {}
        verify(preferredSpaceIdHolder).clear()
    }
}
