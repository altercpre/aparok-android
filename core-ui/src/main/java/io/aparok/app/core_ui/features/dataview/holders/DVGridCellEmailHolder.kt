package io.aparok.app.core_ui.features.dataview.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.R
import io.aparok.app.presentation.sets.model.CellView

class DVGridCellEmailHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(cell: CellView.Email) {
        itemView.findViewById<TextView>(R.id.tvText).text = cell.email.orEmpty()
    }
}