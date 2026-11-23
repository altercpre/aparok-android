package io.aparok.app.core_ui.features.dataview.modals

import android.widget.ImageView
import android.widget.TextView
import io.aparok.app.core_models.Key
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemDvViewerFilterTextBinding
import io.aparok.app.core_ui.widgets.RelationFormatIconWidget
import io.aparok.app.core_utils.ext.invisible
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.presentation.extension.hasValue
import io.aparok.app.presentation.sets.model.ColumnView
import io.aparok.app.presentation.sets.model.FilterView

class FilterTextViewHolder(val binding: ItemDvViewerFilterTextBinding) : FilterViewHolder(binding.root) {

    override val textTitle: TextView get() = binding.tvTitle
    override val textCondition: TextView get() = binding.tvCondition
    override val iconFormat: RelationFormatIconWidget get() = binding.iconFormat
    override val iconArrow: ImageView get() = binding.iconArrow
    override val iconRemove: ImageView get() = binding.iconRemoveText

    fun bind(
        item: FilterView.Expression
    ) {
        init(
            relationKey = item.relation,
            isInEditMode = item.isInEditMode,
            condition = item.condition.title,
            title = item.title,
            format = item.format
        )
        if (item.condition.hasValue()) {
            binding.tvValue.visible()
            binding.tvValue.text = when (item) {
                is FilterView.Expression.Email -> {
                    getStringWithQuotes(item.filterValue.value)
                }
                is FilterView.Expression.Phone -> {
                    getStringWithQuotes(item.filterValue.value)
                }
                is FilterView.Expression.Text -> {
                    getStringWithQuotes(item.filterValue.value)
                }
                is FilterView.Expression.TextShort -> {
                    getStringWithQuotes(item.filterValue.value)
                }
                is FilterView.Expression.Url -> {
                    getStringWithQuotes(item.filterValue.value)
                }
                else -> null
            }
        } else {
            binding.tvValue.invisible()
            binding.tvValue.text = null
        }
    }

    private fun getStringWithQuotes(value: String?): String =
        if (value == null) "" else itemView.context.getString(R.string.value_quotes, value)

    private fun init(
        relationKey: Key,
        isInEditMode: Boolean,
        title: String,
        condition: String,
        format: RelationFormat
    ) {
        setup(
            relationKey = relationKey,
            isEditMode = isInEditMode,
            title = title,
            condition = condition,
            format = format
        )
    }
}