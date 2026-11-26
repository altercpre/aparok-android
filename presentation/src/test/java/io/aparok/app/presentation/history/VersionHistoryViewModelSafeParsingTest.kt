package io.aparok.app.presentation.history

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Event
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.Payload
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.StubHeader
import io.aparok.app.core_models.StubParagraph
import io.aparok.app.core_models.StubSpaceMember
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.core_models.history.ShowVersionResponse
import io.aparok.app.core_models.history.Version
import io.aparok.app.core_models.multiplayer.ParticipantStatus
import io.aparok.app.core_models.multiplayer.SpaceMemberPermissions
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.core_models.primitives.TimestampInSeconds
import io.aparok.app.domain.base.Either
import io.aparok.app.domain.base.Resultat
import io.aparok.app.domain.history.GetVersions
import io.aparok.app.domain.history.SetVersion
import io.aparok.app.domain.history.ShowVersion
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.search.SearchObjects
import io.aparok.app.presentation.editor.render.BlockViewRenderer
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import io.aparok.app.presentation.history.VersionHistoryViewModel.Companion.VERSIONS_MAX_LIMIT
import io.aparok.app.presentation.search.ObjectSearchConstants
import io.aparok.app.presentation.sets.state.DefaultObjectStateReducer
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

@OptIn(ExperimentalCoroutinesApi::class)
class VersionHistoryViewModelSafeParsingTest {

    private val objectId = "obj_safe_parsing_1"
    private val spaceId = SpaceId("space_safe_parsing_1")
    private val vmParams = VersionHistoryViewModel.VmParams(
        objectId = objectId,
        spaceId = spaceId
    )

    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var getVersions: GetVersions

    @Mock
    lateinit var setVersion: SetVersion

    @Mock
    lateinit var showVersion: ShowVersion

    @Mock
    lateinit var urlBuilder: UrlBuilder

    @Mock
    lateinit var objectSearch: SearchObjects

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var renderer: DefaultBlockViewRenderer

    @Mock
    lateinit var storeOfRelations: StoreOfRelations

    @Mock
    lateinit var storeOfObjectTypes: StoreOfObjectTypes

    private val member = StubSpaceMember(
        id = "member_1",
        space = spaceId.id,
        name = "Alice",
        identity = "identity_1",
        memberStatus = ParticipantStatus.ACTIVE,
        memberPermissions = listOf(SpaceMemberPermissions.OWNER)
    )

    private val versionItem = VersionHistoryGroup.Item(
        id = "ver_1",
        spaceMember = member.id,
        spaceMemberName = "Alice",
        dateFormatted = "Nov 26",
        timeFormatted = "12:00",
        timeStamp = TimestampInSeconds(1_700_000_000L),
        icon = null,
        versions = listOf(
            Version(
                id = "ver_1",
                previousIds = emptyList(),
                spaceMember = member.id,
                spaceMemberName = "Alice",
                timestamp = TimestampInSeconds(1_700_000_000L),
                groupId = 1L
            )
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        dateProvider.stub {
            on { formatToDateString(any(), any()) } doReturn "26 11 2026"
            on { formatTimestampToDateAndTime(any()) } doReturn ("Nov 26" to "12:00")
        }
        objectSearch.stub {
            onBlocking {
                invoke(
                    SearchObjects.Params(
                        filters = ObjectSearchConstants.filterParticipants(vmParams.spaceId),
                        keys = ObjectSearchConstants.spaceMemberKeys,
                        space = vmParams.spaceId
                    )
                )
            } doReturn Either.Right(listOf(member))
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `missing ShowObject event in payload safely keeps preview in Loading`() = runTest(dispatcher) {
        stubInitialVersions(versionItem.versions)
        showVersion.stub {
            onBlocking {
                async(ShowVersion.Params(objectId = objectId, versionId = versionItem.id))
            } doReturn Resultat.success(
                ShowVersionResponse(
                    payload = Payload(context = objectId, events = emptyList()),
                    version = null,
                    traceId = ""
                )
            )
        }

        val vm = buildViewModel()
        val navJob = backgroundScope.launch { vm.navigation.collect {} }

        vm.onGroupItemClicked(versionItem)
        advanceUntilIdle()
        navJob.cancel()

        assertEquals(VersionHistoryPreviewScreen.Loading, vm.previewViewState.value)
    }

    @Test
    fun `ShowObject missing root block safely avoids NoSuchElementException`() = runTest(dispatcher) {
        stubInitialVersions(versionItem.versions)
        showVersion.stub {
            onBlocking {
                async(ShowVersion.Params(objectId = objectId, versionId = versionItem.id))
            } doReturn Resultat.success(
                ShowVersionResponse(
                    payload = Payload(
                        context = objectId,
                        events = listOf(
                            Event.Command.ShowObject(
                                context = objectId,
                                root = "unrelated_root",
                                blocks = listOf(StubParagraph(id = "orphan_block", text = "Orphan"))
                            )
                        )
                    ),
                    version = null,
                    traceId = ""
                )
            )
        }

        val vm = buildViewModel()
        val navJob = backgroundScope.launch { vm.navigation.collect {} }

        vm.onGroupItemClicked(versionItem)
        advanceUntilIdle()
        navJob.cancel()

        assertEquals(VersionHistoryPreviewScreen.Loading, vm.previewViewState.value)
    }

    @Test
    fun `valid ShowObject with root block transitions preview to Success Editor`() = runTest(dispatcher) {
        stubInitialVersions(versionItem.versions)
        val rootBlock = StubHeader(id = objectId, children = listOf("child_p1"))
        val childBlock = StubParagraph(id = "child_p1", text = "Restored version content")

        renderer.stub {
            onBlocking {
                with(renderer) {
                    any<Map<Id, List<Block>>>().render(
                        context = any(),
                        mode = any(),
                        root = any(),
                        focus = any(),
                        anchor = any(),
                        indent = any(),
                        details = any(),
                        restrictions = any(),
                        selection = any(),
                        count = any(),
                        parentScheme = any(),
                        participantCanEdit = any(),
                        onRenderFlag = any<(BlockViewRenderer.RenderFlag) -> Unit>()
                    )
                }
            } doReturn emptyList()
        }

        showVersion.stub {
            onBlocking {
                async(ShowVersion.Params(objectId = objectId, versionId = versionItem.id))
            } doReturn Resultat.success(
                ShowVersionResponse(
                    payload = Payload(
                        context = objectId,
                        events = listOf(
                            Event.Command.ShowObject(
                                context = objectId,
                                root = objectId,
                                details = mapOf(
                                    objectId to mapOf(
                                        Relations.ID to objectId,
                                        Relations.LAYOUT to ObjectType.Layout.BASIC.code.toDouble()
                                    )
                                ),
                                blocks = listOf(rootBlock, childBlock)
                            )
                        )
                    ),
                    version = null,
                    traceId = ""
                )
            )
        }

        val vm = buildViewModel()
        val navJob = backgroundScope.launch { vm.navigation.collect {} }

        vm.onGroupItemClicked(versionItem)
        advanceUntilIdle()
        navJob.cancel()

        val preview = assertIs<VersionHistoryPreviewScreen.Success.Editor>(vm.previewViewState.value)
        assertEquals(versionItem.id, preview.versionId)
    }

    @Test
    fun `empty versions list sets viewState to Error NoVersions`() = runTest(dispatcher) {
        stubInitialVersions(emptyList())

        val vm = buildViewModel()
        advanceUntilIdle()

        assertEquals(VersionHistoryState.Error.NoVersions, vm.viewState.value)
    }

    private fun stubInitialVersions(versions: List<Version>) {
        getVersions.stub {
            onBlocking {
                async(
                    GetVersions.Params(
                        objectId = objectId,
                        lastVersion = "",
                        limit = VERSIONS_MAX_LIMIT
                    )
                )
            } doReturn Resultat.success(versions)
        }
    }

    private fun buildViewModel(): VersionHistoryViewModel {
        return VersionHistoryViewModel(
            vmParams = vmParams,
            analytics = analytics,
            getVersions = getVersions,
            objectSearch = objectSearch,
            dateProvider = dateProvider,
            urlBuilder = urlBuilder,
            showVersion = showVersion,
            setVersion = setVersion,
            renderer = renderer,
            setStateReducer = DefaultObjectStateReducer(),
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes
        )
    }
}
