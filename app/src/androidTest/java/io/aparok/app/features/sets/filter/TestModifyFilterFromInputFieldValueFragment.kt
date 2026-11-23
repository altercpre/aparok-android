package io.aparok.app.features.sets.filter

import io.aparok.app.presentation.sets.filter.FilterViewModel
import io.aparok.app.ui.sets.modals.filter.ModifyFilterFromInputFieldValueFragment

class TestModifyFilterFromInputFieldValueFragment : ModifyFilterFromInputFieldValueFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: FilterViewModel.Factory
    }
}