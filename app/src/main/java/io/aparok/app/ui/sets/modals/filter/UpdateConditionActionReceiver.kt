package io.aparok.app.ui.sets.modals.filter

import io.aparok.app.presentation.sets.model.Viewer

interface UpdateConditionActionReceiver {
    fun update(condition: Viewer.Filter.Condition)
}