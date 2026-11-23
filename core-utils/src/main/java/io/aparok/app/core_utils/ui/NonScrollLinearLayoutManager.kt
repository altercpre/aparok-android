package io.aparok.app.core_utils.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NonScrollLinearLayoutManager(context: Context): LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean = false
}