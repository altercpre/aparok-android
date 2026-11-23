package io.aparok.app.core_ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import io.aparok.app.core_ui.R
import io.aparok.app.core_ui.databinding.WidgetRelationObjectBinding
import io.aparok.app.core_ui.extensions.color
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.core_models.ui.ObjectIcon

class RelationObjectItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding = WidgetRelationObjectBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun setup(name: String, icon: ObjectIcon) = with(binding) {
        tvName.visible()
        tvName.text = name.ifBlank {
            context.resources.getString(R.string.untitled)
        }
        when (icon) {
            ObjectIcon.None -> objectIcon.gone()
            else -> {
                objectIcon.visible()
                objectIcon.setIcon(icon)
            }
        }
    }

    fun setupAsNonExistent() = with(binding) {
        tvName.visible()
        tvName.setText(R.string.non_existent_object)
        tvName.setTextColor(context.color(R.color.text_tertiary))
        objectIcon.setNonExistentIcon()
    }
}