package io.aparok.app.device

import io.aparok.app.core_models.Id
import io.aparok.app.domain.cover.GradientCollectionProvider
import io.aparok.app.presentation.editor.cover.CoverGradient

object DefaultGradientCollectionProvider : GradientCollectionProvider {
    override fun provide(): List<Id> = CoverGradient.default
}