package io.aparok.app.presentation.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import javax.inject.Inject

class TemplateBlankViewModelFactory @Inject constructor(
    private val renderer: DefaultBlockViewRenderer,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val fieldParser: FieldParser
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TemplateBlankViewModel(
            renderer = renderer,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            fieldParser = fieldParser
        ) as T
    }
}