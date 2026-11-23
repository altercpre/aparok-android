package io.aparok.app.presentation.sets.main

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.DV
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.ObjectViewDetails
import io.aparok.app.presentation.sets.ObjectSetViewModel
import io.aparok.app.test_utils.MockDataFactory
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class ObjectSetZeroViewTest : ObjectSetViewModelTestSetup() {

    val title = Block(
        id = MockDataFactory.randomUuid(),
        content = Block.Content.Text(
            style = Block.Content.Text.Style.TITLE,
            text = MockDataFactory.randomString(),
            marks = emptyList()
        ),
        children = emptyList(),
        fields = Block.Fields.empty()
    )

    val header = Block(
        id = MockDataFactory.randomUuid(),
        content = Block.Content.Layout(
            type = Block.Content.Layout.Type.HEADER
        ),
        fields = Block.Fields.empty(),
        children = listOf(title.id)
    )

    private val objectSetDetails = ObjectViewDetails(
        details = mapOf(
            root to mapOf(
                Relations.ID to root,
                Relations.SET_OF to listOf(MockDataFactory.randomUuid())
            )
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        proceedWithDefaultBeforeTestStubbing()
    }

    @Test
    fun `should not crash when trying to open relations, filters or sorts for data view containing no view`() {

        // SETUP

        val dv = Block(
            id = MockDataFactory.randomUuid(),
            content = DV(
                viewers = emptyList()
            ),
            children = emptyList(),
            fields = Block.Fields.empty()
        )

        stubInterceptEvents()
        stubOpenObject(
            doc = listOf(
                header,
                title,
                dv
            ),
            details = objectSetDetails
        )

        val vm = givenViewModel()

        // TESTING

        proceedWithStartingViewModel(vm)
        vm.onViewerCustomizeButtonClicked()
        vm.onViewerSortsClicked()
        vm.onViewerFiltersClicked()
        vm.onViewerSettingsClicked("")
    }

    private fun proceedWithStartingViewModel(vm: ObjectSetViewModel) {
        vm.onStart()
    }
}