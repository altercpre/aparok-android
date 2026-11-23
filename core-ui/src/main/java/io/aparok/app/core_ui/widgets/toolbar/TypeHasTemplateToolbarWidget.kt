package io.aparok.app.core_ui.widgets.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.WidgetTypeHasTemplateToolbarBinding

class TypeHasTemplateToolbarWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(context, attrs) {

    val binding = WidgetTypeHasTemplateToolbarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun setText(count: Int, typeName: String) {
    }
}