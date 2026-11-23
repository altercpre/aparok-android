package io.aparok.app.features.relations

import io.aparok.app.presentation.sets.RelationDateValueViewModel
import io.aparok.app.ui.relations.RelationDateValueFragment

class TestRelationDateValueFragment: RelationDateValueFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: RelationDateValueViewModel.Factory
    }
}