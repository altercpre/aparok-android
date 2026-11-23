package io.aparok.app.presentation.templates

import androidx.lifecycle.viewModelScope
import io.aparok.app.core_models.Block
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectTypeIds
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.ext.asMap
import io.aparok.app.presentation.common.BaseViewModel
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.editor.EditorViewModel
import io.aparok.app.core_models.ObjectViewDetails
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.editor.render.BlockViewRenderer
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import io.aparok.app.presentation.templates.TemplateView.Companion.DEFAULT_TEMPLATE_ID_BLANK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class TemplateBlankViewModel(
    private val renderer: DefaultBlockViewRenderer,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val fieldParser: FieldParser
) : BaseViewModel(), BlockViewRenderer by renderer {

    val state = MutableStateFlow<List<BlockView>>(emptyList())
    private val TEMPLATE_TYPE_NAME = "Template"

    fun onStart(typeId: Id, typeName: String, layout: Int) {
        Timber.d("onStart, typeId: $typeId, typeName: $typeName, layout: $layout")
        val blockTitle = Block(
            id = "blockTitle",
            content = Block.Content.Text(
                text = "",
                style = Block.Content.Text.Style.TITLE,
                marks = emptyList()
            ),
            children = emptyList(),
            fields = Block.Fields.empty(),
        )
        val featuredRelationsBlock = Block(
            id = Relations.FEATURED_RELATIONS,
            content = Block.Content.FeaturedRelations,
            children = emptyList(),
            fields = Block.Fields.empty(),
        )
        val headerBlock = if (layout != ObjectType.Layout.NOTE.code) {
            Block(
                id = "header",
                content = Block.Content.Layout(
                    type = Block.Content.Layout.Type.HEADER
                ),
                children = listOf(blockTitle.id, featuredRelationsBlock.id),
                fields = Block.Fields.empty(),
            )
        } else {
            Block(
                id = "header",
                content = Block.Content.Layout(
                    type = Block.Content.Layout.Type.HEADER
                ),
                children = listOf(featuredRelationsBlock.id),
                fields = Block.Fields.empty(),
            )
        }
        val rootBlock = Block(
            id = DEFAULT_TEMPLATE_ID_BLANK,
            content = Block.Content.Smart,
            children = listOf(headerBlock.id),
            fields = Block.Fields.empty(),
        )
        val page = listOf(rootBlock, headerBlock, blockTitle, featuredRelationsBlock)
        val objectDetails = mapOf(
            DEFAULT_TEMPLATE_ID_BLANK to mapOf(
                Relations.ID to DEFAULT_TEMPLATE_ID_BLANK,
                Relations.LAYOUT to layout,
                Relations.TYPE to typeId,
                Relations.IS_DELETED to false
            )
        )

        val typeDetails = mapOf(
            typeId to mapOf(
                Relations.ID to typeId,
                Relations.UNIQUE_KEY to ObjectTypeIds.TEMPLATE,
                Relations.NAME to TEMPLATE_TYPE_NAME,
                Relations.TYPE_UNIQUE_KEY to ObjectTypeIds.TEMPLATE
            )
        )

        val customDetails =
            ObjectViewDetails(
                details = mapOf(DEFAULT_TEMPLATE_ID_BLANK to objectDetails, typeId to typeDetails))

        viewModelScope.launch {
            val objType = storeOfObjectTypes.get(typeId)
            val featuredPropertiesIds = if (objType != null) {
                fieldParser.getObjectParsedProperties(
                    objectType = objType,
                    objPropertiesKeys = listOf(),
                    storeOfRelations = storeOfRelations
                ).header.map { it.id }
            } else {
                emptyList()
            }
            state.value = page.asMap().render(
                context = DEFAULT_TEMPLATE_ID_BLANK,
                mode = Editor.Mode.Read,
                root = page.first(),
                focus = io.aparok.app.domain.editor.Editor.Focus.empty(),
                anchor = page.first().id,
                indent = EditorViewModel.INITIAL_INDENT,
                details = customDetails,
                restrictions = emptyList(),
                selection = emptySet()
            )
        }
    }
}