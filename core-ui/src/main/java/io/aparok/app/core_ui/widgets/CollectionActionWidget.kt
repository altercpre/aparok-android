package io.aparok.app.core_ui.widgets

import android.content.Context
import android.util.AttributeSet
import io.aparok.app.core_ui.features.objects.ObjectActionAdapter
import io.aparok.app.presentation.objects.ObjectAction

class CollectionActionWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseActionWidget<ObjectAction>(context, attrs) {

    override fun provideAdapter() = ObjectActionAdapter { action -> actionListener(action) }
}