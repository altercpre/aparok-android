package io.aparok.app.core_models.settings

import io.aparok.app.core_models.DEFAULT_RELATIVE_DATES
import io.aparok.app.core_models.FALLBACK_DATE_PATTERN
import io.aparok.app.core_models.Id

data class VaultSettings(
    val isRelativeDates: Boolean,
    val dateFormat: String
) {
    companion object {
        fun default() : VaultSettings = VaultSettings(
            isRelativeDates = DEFAULT_RELATIVE_DATES,
            dateFormat = FALLBACK_DATE_PATTERN
        )
    }
}