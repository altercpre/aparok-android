package io.aparok.app.presentation.types

import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import io.aparok.app.domain.launch.GetDefaultObjectType
import io.aparok.app.domain.multiplayer.SpaceViewSubscriptionContainer
import io.aparok.app.presentation.util.DefaultCoroutineTestRule
import io.aparok.app.test_utils.MockDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock

@ExperimentalCoroutinesApi
class ObjectTypeChangeViewModelTest {

    @Mock
    lateinit var blockRepository: BlockRepository

    @Mock
    lateinit var userSettingsRepository: UserSettingsRepository

    @get:Rule
    val coroutineTestRule = DefaultCoroutineTestRule()

    private lateinit var getDefaultObjectType: GetDefaultObjectType

    val spaceId = MockDataFactory.randomUuid()

    @Mock
    lateinit var spaceViews: SpaceViewSubscriptionContainer

    private val dispatchers = AppCoroutineDispatchers(
        io = coroutineTestRule.dispatcher,
        main = coroutineTestRule.dispatcher,
        computation = coroutineTestRule.dispatcher
    )

    @Before
    fun before() {
    }
}