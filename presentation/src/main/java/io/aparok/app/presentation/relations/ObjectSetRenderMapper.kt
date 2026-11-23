package io.aparok.app.presentation.relations

import io.aparok.app.core_models.Block
import io.aparok.app.core_models.DV
import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.DVFilterQuickOption
import io.aparok.app.core_models.DVViewer
import io.aparok.app.core_models.DVViewerCardSize
import io.aparok.app.core_models.DVViewerType
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.DataViewGroup
import io.aparok.app.core_models.GroupOrder
import io.aparok.app.core_models.ObjectOrder
import io.aparok.app.core_models.ObjectViewDetails
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relation
import io.aparok.app.core_models.ui.ObjectIcon
import io.aparok.app.core_models.ui.objectIcon
import io.aparok.app.core_models.ext.DateParser
import io.aparok.app.core_utils.ext.orNull
import io.aparok.app.core_utils.ext.typeOf
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.ObjectStore
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.editor.editor.model.BlockView
import io.aparok.app.presentation.extension.getObject
import io.aparok.app.presentation.extension.isValueRequired
import io.aparok.app.presentation.mapper.toCheckboxView
import io.aparok.app.presentation.mapper.toDateView
import io.aparok.app.presentation.mapper.toGridRecordRows
import io.aparok.app.presentation.mapper.toNumberView
import io.aparok.app.presentation.mapper.toSelectedView
import io.aparok.app.presentation.mapper.toSimpleRelationView
import io.aparok.app.presentation.mapper.toTextView
import io.aparok.app.presentation.mapper.toView
import io.aparok.app.presentation.mapper.toViewerColumns
import io.aparok.app.presentation.number.NumberParser
import io.aparok.app.presentation.objects.toObjects
import io.aparok.app.presentation.sets.buildBoardViews
import io.aparok.app.presentation.sets.boardColumnIds
import io.aparok.app.presentation.sets.buildGalleryViews
import io.aparok.app.presentation.sets.buildListViews
import io.aparok.app.presentation.sets.dataViewState
import io.aparok.app.presentation.sets.filter.CreateFilterView
import io.aparok.app.presentation.sets.model.FilterValue
import io.aparok.app.presentation.sets.model.FilterView
import io.aparok.app.presentation.sets.model.SimpleRelationView
import io.aparok.app.presentation.sets.model.StatusView
import io.aparok.app.presentation.sets.model.TagView
import io.aparok.app.presentation.sets.model.Viewer
import io.aparok.app.presentation.sets.model.ViewerTabView
import io.aparok.app.presentation.sets.state.ObjectState
import timber.log.Timber

fun DV.tabs(activeViewerId: String? = null): List<ViewerTabView> {
    return viewers.mapIndexed { index, viewer ->
        ViewerTabView(
            id = viewer.id,
            name = viewer.name,
            isActive = if (activeViewerId != null)
                viewer.id == activeViewerId
            else
                index == 0
        )
    }
}

suspend fun DVViewer.render(
    builder: UrlBuilder,
    coverImageHashProvider: CoverImageHashProvider,
    useFallbackView: Boolean = false,
    objects: List<Id>,
    dataViewRelations: List<ObjectWrapper.Relation>,
    store: ObjectStore,
    objectOrderIds: List<Id> = emptyList(),
    objectOrders: List<ObjectOrder> = emptyList(),
    storeOfRelations: StoreOfRelations,
    fieldParser: FieldParser,
    storeOfObjectTypes: StoreOfObjectTypes,
    stringResourceProvider: StringResourceProvider,
    boardGroupOptions: Map<Id, ObjectWrapper.Option> = emptyMap(),
    boardGroupOrder: GroupOrder? = null,
    boardGroups: List<DataViewGroup> = emptyList(),
    boardRecordsByColumn: Map<Id, List<Id>> = emptyMap(),
    boardCountsByColumn: Map<Id, Int> = emptyMap(),
    kanbanEnabled: Boolean = false
): Viewer {
    return when (type) {
        DVViewerType.GRID -> {
            buildGridView(
                dataViewRelations = dataViewRelations,
                objects = objects,
                builder = builder,
                store = store,
                objectOrderIds = objectOrderIds,
                fieldParser = fieldParser,
                storeOfObjectTypes = storeOfObjectTypes
            )
        }
        DVViewerType.GALLERY -> {
            Viewer.GalleryView(
                id = id,
                items = buildGalleryViews(
                    objectIds = objects,
                    relations = dataViewRelations,
                    coverImageHashProvider = coverImageHashProvider,
                    urlBuilder = builder,
                    objectStore = store,
                    objectOrderIds = objectOrderIds,
                    storeOfRelations = storeOfRelations,
                    fieldParser = fieldParser,
                    storeOfObjectTypes = storeOfObjectTypes
                ),
                title = name,
                largeCards = cardSize == DVViewerCardSize.LARGE
            )
        }

        DVViewerType.LIST -> {
            val vmap = viewerRelations.associateBy { it.key }
            val visibleRelations = dataViewRelations.filter { relation ->
                val vr = vmap[relation.key]
                vr?.isVisible ?: false
            }
            Viewer.ListView(
                id = id,
                items = buildListViews(
                    objects = objects,
                    relations = visibleRelations,
                    urlBuilder = builder,
                    store = store,
                    objectOrderIds = objectOrderIds,
                    fieldParser = fieldParser,
                    storeOfObjectTypes = storeOfObjectTypes
                ),
                title = name
            )
        }

        DVViewerType.BOARD -> if (!kanbanEnabled) {
            // Kanban is gated behind an experimental flag; treat it as unsupported when off.
            Viewer.Unsupported(id = id, title = name, type = Viewer.Unsupported.TYPE_KANBAN)
        } else {
            Viewer.Board(
                id = id,
                title = name,
                groupingKey = groupRelationKey,
                knownColumnIds = boardGroups.takeIf { it.isNotEmpty() }?.let { boardColumnIds(it).toSet() },
                columns = buildBoardViews(
                    relations = dataViewRelations,
                    urlBuilder = builder,
                    objectStore = store,
                    objectOrders = objectOrders,
                    storeOfRelations = storeOfRelations,
                    fieldParser = fieldParser,
                    storeOfObjectTypes = storeOfObjectTypes,
                    stringResourceProvider = stringResourceProvider,
                    groupOptions = boardGroupOptions,
                    groupOrder = boardGroupOrder,
                    groups = boardGroups,
                    recordsByColumn = boardRecordsByColumn,
                    countsByColumn = boardCountsByColumn
                )
            )
        }

        else -> {
            if (useFallbackView) {
                buildGridView(
                    dataViewRelations = dataViewRelations,
                    objects = objects,
                    builder = builder,
                    store = store,
                    objectOrderIds = objectOrderIds,
                    fieldParser = fieldParser,
                    storeOfObjectTypes = storeOfObjectTypes
                )
            } else {
                Viewer.Unsupported(
                    id = id,
                    title = name,
                    type = when (type) {
                        DVViewerType.CALENDAR -> {
                            Viewer.Unsupported.TYPE_CALENDAR
                        }
                        DVViewerType.GRAPH -> {
                            Viewer.Unsupported.TYPE_GRAPH
                        }
                        else -> {
                            Viewer.Unsupported.TYPE_UNKNOWN
                        }
                    }

                )
            }
        }
    }
}

private fun List<Viewer.GridView.Row>.sortObjects(objectOrderIds: List<Id>): List<Viewer.GridView.Row> {
    val orderMap = objectOrderIds.mapIndexed { index, id -> id to index }.toMap()
    return sortedBy { orderMap[it.id] }
}

private suspend fun DVViewer.buildGridView(
    dataViewRelations: List<ObjectWrapper.Relation>,
    objects: List<Id>,
    builder: UrlBuilder,
    store: ObjectStore,
    objectOrderIds: List<Id>,
    fieldParser: FieldParser,
    storeOfObjectTypes: StoreOfObjectTypes
): Viewer {
    val vmap = viewerRelations.associateBy { it.key }
    val visibleRelations = dataViewRelations.filter { relation ->
        val vr = vmap[relation.key]
        vr?.isVisible ?: false
    }
    val columns = viewerRelations.toViewerColumns(
        relations = visibleRelations,
        filterBy = listOf(ObjectSetConfig.NAME_KEY)
    )
    val rows = buildList {
        addAll(
            objects.toGridRecordRows(
                showIcon = !hideIcon,
                columns = columns,
                builder = builder,
                store = store,
                fieldParser = fieldParser,
                storeOfObjectTypes = storeOfObjectTypes
            )
        )
    }
    return Viewer.GridView(
        id = id,
        name = name,
        columns = columns,
        rows = if (objectOrderIds.isNotEmpty()) rows.sortObjects(objectOrderIds) else rows
    )
}

fun title(
    title: Block,
    ctx: Id,
    coverImageHashProvider: CoverImageHashProvider,
    urlBuilder: UrlBuilder,
    details: ObjectViewDetails,
    objType: ObjectWrapper.Type? = null
): BlockView.Title.Basic {
    val wrapper = details.getObject(ctx)
    val coverContainer = BasicObjectCoverWrapper(wrapper).getCover(
        urlBuilder = urlBuilder,
        coverImageHashProvider = coverImageHashProvider
    )
    val resolvedIcon = wrapper?.objectIcon(
        builder = urlBuilder,
        objType = objType
    ) ?: ObjectIcon.None
    return BlockView.Title.Basic(
        id = title.id,
        text = wrapper?.name.orEmpty(),
        emoji = wrapper?.iconEmoji.orNull(),
        image = wrapper?.iconImage?.takeIf { it.isNotBlank() }?.let { urlBuilder.medium(it) },
        coverImage = coverContainer.coverImage,
        coverColor = coverContainer.coverColor,
        coverGradient = coverContainer.coverGradient,
        icon = resolvedIcon
    )
}

suspend fun ObjectState.simpleRelations(
    viewerId: Id?,
    storeOfRelations: StoreOfRelations
): ArrayList<SimpleRelationView> {
    val state = dataViewState() ?: return arrayListOf()
    val dv = state.dataViewContent
    val viewer = dv.viewers.find { it.id == viewerId } ?: dv.viewers.first()
    return viewer.viewerRelations.toSimpleRelationView(
        dv.relationLinks.mapNotNull {
            storeOfRelations.getByKey(it.key)
        }
    )
}

fun DVViewer.toViewRelation(relation: ObjectWrapper.Relation): SimpleRelationView {
    val viewerRelation = viewerRelations.firstOrNull { it.key == relation.key }
    if (viewerRelation == null) {
        Timber.e("ViewerRelations is not containing relation:$relation")
    }
    return SimpleRelationView(
        key = relation.key,
        isHidden = relation.isHidden ?: false,
        isVisible = viewerRelation?.isVisible ?: false,
        title = relation.name.orEmpty(),
        format = relation.format
    )
}

fun ObjectWrapper.Relation.toCreateFilterCheckboxView(isSelected: Boolean? = null): List<CreateFilterView.Checkbox> {
    return listOf(
        CreateFilterView.Checkbox(
            isChecked = true,
            isSelected = isSelected == true
        ),
        CreateFilterView.Checkbox(
            isChecked = false,
            isSelected = isSelected != true
        )
    )
}

fun List<ObjectWrapper.Option>.toCreateFilterTagView(
    selected: List<Id>,
): List<CreateFilterView.Tag> = map { option ->
    CreateFilterView.Tag(
        id = option.id,
        name = option.name.orEmpty(),
        color = option.color,
        isSelected = selected.contains(option.id)
    )
}

fun List<ObjectWrapper.Option>.toCreateFilterStatusView(
    selected: List<Id>,
): List<CreateFilterView.Status> = map { option ->
    CreateFilterView.Status(
        id = option.id,
        name = option.name.orEmpty(),
        color = option.color,
        isSelected = selected.contains(option.id)
    )
}

fun ObjectWrapper.Relation.toCreateFilterDateView(
    quickOption: DVFilterQuickOption?,
    condition: DVFilterCondition,
    value: Long,
    fieldParser: FieldParser
) = quickOptionOrderMap.getOrDefault(condition, quickOptionDefaultOrder)
    .map {
        val isSelected = quickOption.isSelected(it, value)
        val fieldDate = fieldParser.toDate(any = value)
        CreateFilterView.Date(
            id = key,
            type = it,
            condition = condition,
            value = if (isSelected) value else CreateFilterView.Date.NO_VALUE,
            isSelected = isSelected,
            relativeDate = fieldDate?.relativeDate,
            description = ""
        )
    }

private fun DVFilterQuickOption?.isSelected(
    quickOption: DVFilterQuickOption,
    value: Long
) = if (this == quickOption) {
    if (quickOption == DVFilterQuickOption.EXACT_DATE) {
        value > 0
    } else {
        true
    }
} else {
    false
}


private val quickOptionDefaultOrder by lazy {
    listOf(
        DVFilterQuickOption.TODAY,
        DVFilterQuickOption.YESTERDAY,
        DVFilterQuickOption.TOMORROW,
        DVFilterQuickOption.CURRENT_WEEK,
        DVFilterQuickOption.LAST_WEEK,
        DVFilterQuickOption.NEXT_WEEK,
        DVFilterQuickOption.CURRENT_MONTH,
        DVFilterQuickOption.LAST_MONTH,
        DVFilterQuickOption.NEXT_MONTH,
        DVFilterQuickOption.DAYS_AGO,
        DVFilterQuickOption.DAYS_AHEAD,
        DVFilterQuickOption.EXACT_DATE,
    )
}

private val quickOptionOrderMap: Map<DVFilterCondition, List<DVFilterQuickOption>> by lazy {

    // Extended options including all date ranges
    val extendedOptions = listOf(
        DVFilterQuickOption.TODAY,
        DVFilterQuickOption.YESTERDAY,
        DVFilterQuickOption.TOMORROW,
        DVFilterQuickOption.CURRENT_WEEK,
        DVFilterQuickOption.LAST_WEEK,
        DVFilterQuickOption.NEXT_WEEK,
        DVFilterQuickOption.CURRENT_MONTH,
        DVFilterQuickOption.LAST_MONTH,
        DVFilterQuickOption.NEXT_MONTH,
        DVFilterQuickOption.CURRENT_YEAR,
        DVFilterQuickOption.LAST_YEAR,
        DVFilterQuickOption.NEXT_YEAR,
    )

    // Default options for exact dates and offsets
    val defaultOptions = listOf(
        DVFilterQuickOption.DAYS_AGO,
        DVFilterQuickOption.DAYS_AHEAD,
        DVFilterQuickOption.EXACT_DATE,
    )

    buildMap {
        // Equal condition: specific day options + defaults
        put(
            DVFilterCondition.EQUAL, listOf(
                DVFilterQuickOption.TODAY,
                DVFilterQuickOption.YESTERDAY,
                DVFilterQuickOption.TOMORROW,
            ) + defaultOptions
        )

        // Comparison conditions: all extended options + defaults (includes years)
        val comparisonOptions = extendedOptions + defaultOptions
        put(DVFilterCondition.GREATER, comparisonOptions)
        put(DVFilterCondition.LESS, comparisonOptions)
        put(DVFilterCondition.GREATER_OR_EQUAL, comparisonOptions)
        put(DVFilterCondition.LESS_OR_EQUAL, comparisonOptions)

        // In condition: only extended options (no defaults)
        put(DVFilterCondition.IN, extendedOptions)

        // Empty conditions
        put(DVFilterCondition.EMPTY, emptyList())
        put(DVFilterCondition.NOT_EMPTY, emptyList())
    }
}


fun ObjectState.DataView.filterExpression(viewerId: Id?): List<DVFilter> {
    val viewer =
        dataViewContent.viewers.find { it.id == viewerId } ?: dataViewContent.viewers.first()
    return viewer.filters
}

fun ObjectWrapper.Relation.toText(value: Any?): String? =
    if (value is String?) {
        value
    } else {
        throw IllegalArgumentException("Text relation format $format value should be String, actual:$value")
    }

fun ObjectWrapper.Relation.toUrl(value: Any?): String? =
    if (value is String?) {
        value
    } else {
        throw IllegalArgumentException("Text relation format $format value should be String, actual:$value")
    }

fun ObjectWrapper.Relation.toPhone(value: Any?): String? =
    if (value is String?) {
        value
    } else {
        throw IllegalArgumentException("Text relation format $format value should be String, actual:$value")
    }

fun ObjectWrapper.Relation.toEmail(value: Any?): String? =
    if (value is String?) {
        value
    } else {
        throw IllegalArgumentException("Text relation format $format value should be String, actual:$value")
    }

fun ObjectWrapper.Relation.toCheckbox(value: Any?): Boolean? =
    if (value is Boolean?) {
        value
    } else {
        Timber.e("Relation format $format value should be Boolean, actual:$value")
        null
    }

suspend fun ObjectWrapper.Relation.toTags(
    value: Any?,
    store: ObjectStore
): List<TagView> {
    val ids = value.values<Id>()
    return buildList {
        ids.forEach { id ->
            val option = store.get(id)?.let { ObjectWrapper.Option(it.map) }
            if (option != null && option.isDeleted != true) {
                add(
                    TagView(
                        id = option.id,
                        tag = option.name.orEmpty(),
                        color = option.color
                    )
                )
            } else {
                Timber.e("Failed to find corresponding tag option")
            }
        }
    }
}

suspend fun ObjectWrapper.Relation.toStatus(
    value: Any?,
    store: ObjectStore
): StatusView? {
    val filter: Id? = value.values<Id>().firstOrNull()
    return if (filter != null) {
        val option = store.get(filter)?.let { ObjectWrapper.Option(it.map) }
        if (option != null && option.isDeleted != true) {
            StatusView(
                id = option.id,
                status = option.name.orEmpty(),
                color = option.color
            )
        } else {
            null
        }
    } else {
        null
    }
}

suspend fun DVFilter.toView(
    store: ObjectStore,
    relation: ObjectWrapper.Relation,
    isInEditMode: Boolean,
    urlBuilder: UrlBuilder,
    fieldParser: FieldParser,
    storeOfObjectTypes: StoreOfObjectTypes
): FilterView.Expression = when (relation.format) {
    Relation.Format.SHORT_TEXT -> {
        FilterView.Expression.TextShort(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toTextView(),
            filterValue = FilterValue.TextShort(relation.toText(value)),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.LONG_TEXT -> {
        FilterView.Expression.Text(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toTextView(),
            filterValue = FilterValue.Text(relation.toText(value)),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.URL -> {
        FilterView.Expression.Url(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toTextView(),
            filterValue = FilterValue.Url(relation.toUrl(value)),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.EMAIL -> {
        FilterView.Expression.Email(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toTextView(),
            filterValue = FilterValue.Email(relation.toEmail(value)),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.PHONE -> {
        FilterView.Expression.Phone(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toTextView(),
            filterValue = FilterValue.Phone(relation.toPhone(value)),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.NUMBER -> {
        FilterView.Expression.Number(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toNumberView(),
            filterValue = FilterValue.Number(NumberParser.parse(value)),
            format = relation.format,
            isValueRequired = true,
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.DATE -> {
        val fieldDate = fieldParser.toDate(any = value)
        FilterView.Expression.Date(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toDateView(),
            quickOption = quickOption,
            filterValue = FilterValue.Date(fieldDate?.timestamp?.time),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode,
            relativeDate = fieldDate?.relativeDate
        )
    }

    Relation.Format.STATUS -> {
        val updatedFilterValue = relation.toStatus(
            value = value,
            store = store
        )
        FilterView.Expression.Status(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toSelectedView(),
            filterValue = FilterValue.Status(value = updatedFilterValue),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.TAG -> {
        FilterView.Expression.Tag(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toSelectedView(),
            filterValue = FilterValue.Tag(
                relation.toTags(
                    value = value,
                    store = store
                )
            ),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.OBJECT, Relation.Format.FILE -> {
        FilterView.Expression.Object(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toSelectedView(),
            filterValue = FilterValue.Object(
                relation.toObjects(
                    value = value,
                    store = store,
                    urlBuilder = urlBuilder,
                    fieldParser = fieldParser,
                    storeOfObjectTypes = storeOfObjectTypes
                )
            ),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    Relation.Format.CHECKBOX -> {
        FilterView.Expression.Checkbox(
            id = id,
            relation = this.relation,
            title = relation.name.orEmpty(),
            operator = operator.toView(),
            condition = condition.toCheckboxView(),
            filterValue = FilterValue.Check(relation.toCheckbox(value)),
            format = relation.format,
            isValueRequired = condition.isValueRequired(),
            isInEditMode = isInEditMode
        )
    }

    else -> throw UnsupportedOperationException("Unsupported relation format:${relation.format}")
}

suspend fun ObjectWrapper.Relation.toFilterValue(
    value: Any?,
    urlBuilder: UrlBuilder,
    store: ObjectStore,
    fieldParser: FieldParser,
    storeOfObjectTypes : StoreOfObjectTypes
): FilterValue = when (this.format) {
    Relation.Format.SHORT_TEXT -> FilterValue.TextShort(toText(value))
    Relation.Format.LONG_TEXT -> FilterValue.Text(toText(value))
    Relation.Format.NUMBER -> FilterValue.Number(NumberParser.parse(value))
    Relation.Format.STATUS -> FilterValue.Status(
        toStatus(
            value = value,
            store = store
        )
    )

    Relation.Format.TAG -> FilterValue.Tag(
        toTags(
            value = value,
            store = store
        )
    )

    Relation.Format.DATE -> FilterValue.Date(DateParser.parse(value))
    Relation.Format.URL -> FilterValue.Url(toText(value))
    Relation.Format.EMAIL -> FilterValue.Email(toText(value))
    Relation.Format.PHONE -> FilterValue.Phone(toText(value))
    Relation.Format.OBJECT, Relation.Format.FILE -> {
        val obj = toObjects(
            value = value,
            store = store,
            urlBuilder = urlBuilder,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes
        )
        FilterValue.Object(obj)
    }

    Relation.Format.CHECKBOX -> FilterValue.Check(toCheckbox(value))
    else -> throw UnsupportedOperationException("Unsupported relation format:${format}")
}

inline fun <reified T> Any?.values(): List<T> = when (this) {
    is List<*> -> this.typeOf()
    is T -> listOf(this)
    else -> emptyList()
}
