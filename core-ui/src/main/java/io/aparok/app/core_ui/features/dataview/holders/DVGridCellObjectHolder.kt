package io.aparok.app.core_ui.features.dataview.holders

import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.databinding.ItemViewerGridCellObjectBinding
import io.aparok.app.core_ui.widgets.GridCellObjectItem
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.presentation.sets.model.CellView
import io.aparok.app.presentation.sets.model.ObjectView

class DVGridCellObjectHolder(
    val binding: ItemViewerGridCellObjectBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cell: CellView.Object) = with(itemView) {
        for (i in 0..MAX_VISIBLE_OBJECTS_INDEX) getViewByIndex(i)?.gone()
        cell.objects.forEachIndexed { index, objectView ->
            when (index) {
                in 0..MAX_VISIBLE_OBJECTS_INDEX -> {
                    getViewByIndex(index)?.let { view ->
                        if (objectView is ObjectView.Default) {
                            view.visible()
                            view.setup(name = objectView.name, icon = objectView.icon)
                        } else {
                            view.visible()
                            view.setupAsNonExistent()
                        }
                    }
                }
            }
        }
    }

    private fun getViewByIndex(index: Int): GridCellObjectItem? = when (index) {
        0 -> binding.object0
        1 -> binding.object1
        2 -> binding.object2
        3 -> binding.object3
        else -> null
    }

    companion object {
        const val MAX_VISIBLE_OBJECTS_INDEX = 3
    }
}