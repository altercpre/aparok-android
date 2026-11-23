package io.aparok.app.di.common

import android.app.Activity
import androidx.fragment.app.Fragment
import io.aparok.app.app.AndroidApplication

fun Fragment.componentManager(): ComponentManager {
    return (requireActivity().applicationContext as AndroidApplication).componentManager
}

fun Activity.componentManager(): ComponentManager {
    return (applicationContext as AndroidApplication).componentManager
}