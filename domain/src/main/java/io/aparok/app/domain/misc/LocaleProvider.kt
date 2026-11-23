package io.aparok.app.domain.misc

import java.util.Locale

interface LocaleProvider {
    fun language() : String
    fun locale() : Locale
}