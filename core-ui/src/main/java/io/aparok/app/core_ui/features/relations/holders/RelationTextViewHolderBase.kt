package io.aparok.app.core_ui.features.relations.holders

import android.text.InputType
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.ItemObjectRelationTextBinding
import io.aparok.app.core_utils.ext.focusAndShowKeyboard
import io.aparok.app.presentation.sets.RelationTextValueView

abstract class RelationTextViewHolderBase<T : RelationTextValueView>(
    val binding: ItemObjectRelationTextBinding,
    inputType: Int,
    @StringRes
    hint: Int
) : RecyclerView.ViewHolder(binding.root) {

    protected val input: TextView = binding.textInputField
    private val textInputField = binding.textInputField

    init {
        input.inputType = inputType
        input.setHint(hint)
    }

    @CallSuper
    open fun bind(item: T) {
        textInputField.setText(item.value)
        if (item.value.isNullOrEmpty() && item.isEditable) {
            textInputField.focusAndShowKeyboard()
        }
        if (!item.isEditable) {
            enableReadMode()
        }
    }

    private fun enableReadMode() {
        input.inputType = InputType.TYPE_NULL
        input.setHint(R.string.empty)
    }
}