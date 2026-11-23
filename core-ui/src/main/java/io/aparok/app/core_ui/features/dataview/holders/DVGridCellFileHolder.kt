package io.aparok.app.core_ui.features.dataview.holders

import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.databinding.ItemViewerGridCellFileBinding
import io.aparok.app.core_ui.widgets.GridCellFileItem
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.presentation.sets.model.CellView

class DVGridCellFileHolder(val binding: ItemViewerGridCellFileBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cell: CellView.File) {
        for (i in 0..MAX_VISIBLE_FILES_INDEX) getViewByIndex(i)?.gone()
        cell.files.forEachIndexed { index, fileView ->
            when (index) {
                in 0..MAX_VISIBLE_FILES_INDEX -> {
                    getViewByIndex(index)?.let { view ->
                        view.visible()
                        view.setup(name = fileView.name, mime = fileView.mime, extension = fileView.ext)
                    }
                }
            }
        }
    }

    private fun getViewByIndex(index: Int): GridCellFileItem? = when (index) {
        0 -> binding.file0
        1 -> binding.file1
        2 -> binding.file2
        else -> null
    }

    companion object {
        const val MAX_VISIBLE_FILES_INDEX = 2
    }
}