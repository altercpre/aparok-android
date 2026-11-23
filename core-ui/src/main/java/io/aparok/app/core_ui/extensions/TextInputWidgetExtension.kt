package io.aparok.app.core_ui.extensions

import io.aparok.app.core_ui.common.isLinksOrMentionsPresent
import io.aparok.app.core_ui.widgets.text.TextInputWidget
import io.aparok.app.presentation.editor.editor.Markup

fun TextInputWidget.applyMovementMethod(item: Markup) {
    if (item.marks.isNotEmpty() && item.marks.isLinksOrMentionsPresent()) {
        setLinksClickable()
    } else {
        setDefaultMovementMethod()
    }
}

fun TextInputWidget.disable() {
    isEnabled = false
    pauseTextWatchers {
        enableReadMode()
    }
}