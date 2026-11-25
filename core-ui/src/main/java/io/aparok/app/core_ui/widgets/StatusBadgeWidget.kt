package io.aparok.app.core_ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.aparok.app.core_models.multiplayer.SpaceSyncAndP2PStatusState
import io.aparok.app.core_models.multiplayer.SpaceSyncError
import io.aparok.app.core_models.multiplayer.SpaceSyncStatus
import io.aparok.app.core_models.multiplayer.SpaceSyncUpdate
import io.aparok.app.core_ui.R
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.visible


class StatusBadgeWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    fun bind(status: SpaceSyncAndP2PStatusState?) {
        visible()
        setImageResource(R.drawable.ci_lock_closed)
        contentDescription = "Saved on this device"
    }
}