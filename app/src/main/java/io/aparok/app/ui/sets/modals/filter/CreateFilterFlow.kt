package io.aparok.app.ui.sets.modals.filter

import io.aparok.app.core_models.Id
import io.aparok.app.presentation.sets.model.SimpleRelationView

interface CreateFilterFlow {
    fun onRelationSelected(ctx: Id, relation: SimpleRelationView)
    fun onFilterCreated()
}