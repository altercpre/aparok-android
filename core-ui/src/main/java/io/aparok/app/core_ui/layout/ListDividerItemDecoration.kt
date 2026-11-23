package io.aparok.app.core_ui.layout

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import io.aparok.app.core_ui.R

class ListDividerItemDecoration(context: Context) : DividerItemDecoration(context, VERTICAL) {

    init {
        context.getDrawable(R.drawable.divider)?.let {
            setDrawable(it)
        }
    }
}