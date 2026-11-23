package io.aparok.app.core_models

sealed class ThemeMode {
    object System : ThemeMode()
    object Light : ThemeMode()
    object Night : ThemeMode()
}