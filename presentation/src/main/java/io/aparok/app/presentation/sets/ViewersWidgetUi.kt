package io.aparok.app.presentation.sets

import io.aparok.app.core_models.DVViewerType
import io.aparok.app.core_models.Id
import io.aparok.app.presentation.sets.viewer.ViewerView
import io.aparok.app.presentation.widgets.FromIndex
import io.aparok.app.presentation.widgets.ToIndex

data class ViewersWidgetUi(
    val showWidget: Boolean,
    val isEditing: Boolean,
    val isReadOnly: Boolean,
    val items: List<ViewerView>
) {

    fun dismiss() = copy(
        showWidget = false,
        isEditing = false
    )

    companion object {
        fun init() = ViewersWidgetUi(
            showWidget = false,
            isEditing = false,
            items = emptyList(),
            isReadOnly = true
        )
    }

    sealed class Action {
        data object Dismiss : Action()
        data object EditMode : Action()
        data object DoneMode : Action()
        data class Delete(val viewer: Id) : Action()
        data class Edit(val id: Id) : Action()
        data class OnMove(
            val currentViews: List<ViewerView>,
            val from: FromIndex,
            val to: ToIndex
        ) : Action()

        data class SetActive(val id: Id, val type: DVViewerType) : Action()

        data object Plus : Action()
    }
}