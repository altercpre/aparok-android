package io.aparok.app.features.sets.sort

import io.aparok.app.presentation.sets.sort.ViewerSortViewModel
import io.aparok.app.ui.sets.modals.sort.ViewerSortFragment

class TestViewerSortFragment : ViewerSortFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: ViewerSortViewModel.Factory
    }
}