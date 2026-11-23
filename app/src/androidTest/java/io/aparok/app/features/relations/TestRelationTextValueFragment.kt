package io.aparok.app.features.relations

import io.aparok.app.presentation.sets.RelationTextValueViewModel
import io.aparok.app.ui.relations.RelationTextValueFragment

class TestRelationTextValueFragment : RelationTextValueFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: RelationTextValueViewModel.Factory
    }
}