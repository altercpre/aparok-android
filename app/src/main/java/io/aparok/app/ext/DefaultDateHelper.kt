package io.aparok.app.ext

import io.aparok.app.core_utils.date.isToday
import io.aparok.app.core_utils.date.isTomorrow
import io.aparok.app.domain.account.DateHelper

class DefaultDateHelper : DateHelper {
    override fun isToday(millis: Long): Boolean {
        return millis.isToday()
    }

    override fun isTomorrow(millis: Long): Boolean {
        return millis.isTomorrow()
    }
}