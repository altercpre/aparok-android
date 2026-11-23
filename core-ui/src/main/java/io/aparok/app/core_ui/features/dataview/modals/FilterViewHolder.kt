package io.aparok.app.core_ui.features.dataview.modals

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_models.Relations
import io.aparok.app.core_ui.widgets.RelationFormatIconWidget
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.visible

abstract class FilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract val textTitle: TextView
    abstract val textCondition: TextView
    abstract val iconFormat: RelationFormatIconWidget
    abstract val iconArrow: ImageView
    abstract val iconRemove: ImageView

    fun setup(
        relationKey: Key,
        isEditMode: Boolean,
        title: String,
        condition: String,
        format: RelationFormat
    ) {
        if (isEditMode) {
            iconRemove.visible()
            iconArrow.gone()
        } else {
            iconRemove.gone()
            iconArrow.visible()
        }
        textTitle.text = title
        textCondition.text = condition
        iconFormat.bind(isName = relationKey == Relations.NAME, format = format)
    }
}