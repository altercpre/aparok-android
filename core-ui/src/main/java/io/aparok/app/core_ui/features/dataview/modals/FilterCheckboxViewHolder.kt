package io.aparok.app.core_ui.features.dataview.modals

import android.widget.ImageView
import android.widget.TextView
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemDvViewerFilterCheckboxBinding
import io.aparok.app.core_ui.widgets.RelationFormatIconWidget
import io.aparok.app.core_utils.ext.invisible
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.presentation.extension.hasValue
import io.aparok.app.presentation.sets.model.FilterView

class FilterCheckboxViewHolder(
    val binding: ItemDvViewerFilterCheckboxBinding
) : FilterViewHolder(binding.root) {

    override val textTitle: TextView get() = binding.tvTitle
    override val textCondition: TextView get() = binding.tvCondition
    override val iconFormat: RelationFormatIconWidget get() = binding.iconFormat
    override val iconArrow: ImageView get() = binding.iconArrow
    override val iconRemove: ImageView get() = binding.iconRemoveCheckbox

    fun bind(item: FilterView.Expression.Checkbox) = with(itemView) {
        setup(
            relationKey = item.relation,
            isEditMode = item.isInEditMode,
            title = item.title,
            condition = item.condition.title,
            format = item.format
        )
        if (item.condition.hasValue()) {
            binding.tvValue.visible()
            binding.tvValue.text = if (item.filterValue.value == true) {
                itemView.context.getString(R.string.dv_filter_checkbox_checked)
            } else {
                itemView.context.getString(R.string.dv_filter_checkbox_not_checked)
            }
        } else {
            binding.tvValue.text = null
            binding.tvValue.invisible()
        }
    }
}