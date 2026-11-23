package io.aparok.app.presentation.settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorSettings(val customContextMenu: Boolean) : Parcelable