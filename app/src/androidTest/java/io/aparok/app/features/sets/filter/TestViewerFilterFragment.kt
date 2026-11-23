package io.aparok.app.features.sets.filter

import io.aparok.app.presentation.sets.filter.ViewerFilterViewModel
import io.aparok.app.ui.sets.ViewerFilterFragment

class TestViewerFilterFragment : ViewerFilterFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: ViewerFilterViewModel.Factory
    }
}