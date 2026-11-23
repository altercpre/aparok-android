package io.aparok.app.presentation.sets.subscription

import io.aparok.app.core_models.DVSort
import io.aparok.app.core_models.DVSortType
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.RelationFormat
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.search.DataViewState
import io.aparok.app.domain.search.DataViewSubscriptionContainer
import io.aparok.app.presentation.relations.ObjectSetConfig
import io.aparok.app.presentation.search.ObjectSearchConstants
import io.aparok.app.presentation.search.ObjectSearchConstants.defaultDataViewFilters
import io.aparok.app.presentation.sets.filterOutDeletedAndMissingObjects
import io.aparok.app.presentation.sets.getSetOfValue
import io.aparok.app.presentation.sets.state.ObjectState
import io.aparok.app.presentation.extension.removeUnsupportedFilters
import io.aparok.app.presentation.sets.updateFormatForSubscription
import io.aparok.app.presentation.sets.viewerByIdOrFirst
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber

interface DataViewSubscription {

    suspend fun startObjectSetSubscription(
        context: Id,
        space: Id,
        state: ObjectState.DataView.Set,
        currentViewerId: Id?,
        offset: Long,
        storeOfRelations: StoreOfRelations
    ): Flow<DataViewState>

    suspend fun startObjectCollectionSubscription(
        context: Id,
        collection: Id,
        space: Id,
        state: ObjectState.DataView.Collection,
        currentViewerId: Id?,
        offset: Long,
        storeOfRelations: StoreOfRelations
    ): Flow<DataViewState>

    suspend fun startObjectTypeSetSubscription(
        context: Id,
        space: Id,
        state: ObjectState.DataView.TypeSet,
        currentViewerId: Id?,
        offset: Long,
        storeOfRelations: StoreOfRelations
    ): Flow<DataViewState>

    suspend fun unsubscribe(ids: List<Id>)
}

class DefaultDataViewSubscription(
    private val dataViewSubscriptionContainer: DataViewSubscriptionContainer
) : DataViewSubscription {

    override suspend fun startObjectCollectionSubscription(
        context: Id,
        collection: Id,
        space: Id,
        state: ObjectState.DataView.Collection,
        currentViewerId: Id?,
        offset: Long,
        storeOfRelations: StoreOfRelations
    ): Flow<DataViewState> {
        if (context.isEmpty() || collection.isEmpty()) {
            Timber.w("Data view collection subscription: context or collection is empty")
            return emptyFlow()
        }
        val activeViewer = state.viewerByIdOrFirst(currentViewerId)
        if (activeViewer == null) {
            Timber.w("Data view collection subscription: active viewer is null")
            return emptyFlow()
        }
        val filters = buildList {
            addAll(activeViewer.filters.updateFormatForSubscription(storeOfRelations).removeUnsupportedFilters())
            addAll(defaultDataViewFilters())
        }
        val dataViewLinksKeys = state.dataViewContent.relationLinks.map { it.key }
        val keys = ObjectSearchConstants.defaultDataViewKeys + dataViewLinksKeys

        val sorts = getSortsWithDefaultCreatedDate(
            viewerSorts = activeViewer.sorts,
            storeOfRelations = storeOfRelations
        )

        val params = DataViewSubscriptionContainer.Params(
            space = SpaceId(space),
            collection = collection,
            subscription = getDataViewSubscriptionId(context),
            sorts = sorts,
            filters = filters,
            sources = listOf(),
            keys = keys,
            limit = ObjectSetConfig.DEFAULT_LIMIT,
            offset = offset
        )
        return dataViewSubscriptionContainer.observe(params)
    }

    override suspend fun startObjectSetSubscription(
        context: Id,
        space: Id,
        state: ObjectState.DataView.Set,
        currentViewerId: Id?,
        offset: Long,
        storeOfRelations: StoreOfRelations
    ): Flow<DataViewState> {
        if (context.isEmpty()) {
            Timber.w("Data view set subscription: context is empty")
            return emptyFlow()
        }
        val activeViewer = state.viewerByIdOrFirst(currentViewerId)
        if (activeViewer == null) {
            Timber.w("Data view set subscription: active viewer is null")
            return emptyFlow()
        }

        val setOfValue = state.getSetOfValue(ctx = context)
        if (setOfValue.isEmpty()) {
            Timber.w("Data view set subscription: setOf value is empty, proceed without subscription")
            return emptyFlow()
        }

        val query = state.filterOutDeletedAndMissingObjects(setOfValue)
        if (query.isEmpty()) {
            Timber.w(
                "Data view set subscription: query has no valid types or relations, " +
                        "proceed without subscription"
            )
            return emptyFlow()
        }

        val filters = buildList {
            addAll(activeViewer.filters.updateFormatForSubscription(storeOfRelations).removeUnsupportedFilters())
            addAll(defaultDataViewFilters())
        }
        val dataViewLinksKeys = state.dataViewContent.relationLinks.map { it.key }
        val keys = ObjectSearchConstants.defaultDataViewKeys + dataViewLinksKeys
        val sorts = getSortsWithDefaultCreatedDate(
            viewerSorts = activeViewer.sorts,
            storeOfRelations = storeOfRelations
        )

        val params = DataViewSubscriptionContainer.Params(
            space = SpaceId(space),
            subscription = getDataViewSubscriptionId(context),
            sorts = sorts,
            filters = filters,
            sources = query,
            keys = keys,
            limit = ObjectSetConfig.DEFAULT_LIMIT,
            offset = offset
        )
        return dataViewSubscriptionContainer.observe(params)
    }

    override suspend fun startObjectTypeSetSubscription(
        context: Id,
        space: Id,
        state: ObjectState.DataView.TypeSet,
        currentViewerId: Id?,
        offset: Long,
        storeOfRelations: StoreOfRelations
    ): Flow<DataViewState> {
        if (context.isEmpty()) {
            Timber.w("Data view TypeSet subscription: context is empty")
            return emptyFlow()
        }
        val activeViewer = state.viewerByIdOrFirst(currentViewerId)
        if (activeViewer == null) {
            Timber.w("Data view TypeSet subscription: active viewer is null")
            return emptyFlow()
        }

        val setOfValue = state.getSetOfValue(ctx = context)
        if (setOfValue.isEmpty()) {
            Timber.w("Data view TypeSet subscription: setOf value is empty, proceed without subscription")
            return emptyFlow()
        }

        val query = state.filterOutDeletedAndMissingObjects(setOfValue)
        if (query.isEmpty()) {
            Timber.w(
                "Data view TypeSet subscription: query has no valid types or relations, " +
                        "proceed without subscription"
            )
            return emptyFlow()
        }

        val filters = buildList {
            addAll(activeViewer.filters.updateFormatForSubscription(storeOfRelations).removeUnsupportedFilters())
            addAll(defaultDataViewFilters())
        }
        val dataViewLinksKeys = state.dataViewContent.relationLinks.map { it.key }
        val keys = ObjectSearchConstants.defaultDataViewKeys + dataViewLinksKeys
        val sorts = getSortsWithDefaultCreatedDate(
            viewerSorts = activeViewer.sorts,
            storeOfRelations = storeOfRelations
        )

        val params = DataViewSubscriptionContainer.Params(
            space = SpaceId(space),
            subscription = getDataViewSubscriptionId(context),
            sorts = sorts,
            filters = filters,
            sources = query,
            keys = keys,
            limit = ObjectSetConfig.DEFAULT_LIMIT,
            offset = offset
        )
        return dataViewSubscriptionContainer.observe(params)
    }

    override suspend fun unsubscribe(ids: List<Id>) {
        dataViewSubscriptionContainer.unsubscribe(ids)
    }

    companion object {
        private const val DATA_VIEW_SUBSCRIPTION_POSTFIX = "-dataview"
        fun getDataViewSubscriptionId(context: Id) = "$context$DATA_VIEW_SUBSCRIPTION_POSTFIX"
    }
}

suspend fun List<DVSort>.updateWithRelationFormat(storeOfRelations: StoreOfRelations): List<DVSort> {
    return map { sort ->
        val relation = storeOfRelations.getByKey(sort.relationKey)
        sort.copy(relationFormat = relation?.format ?: RelationFormat.LONG_TEXT)
    }
}


private suspend fun getSortsWithDefaultCreatedDate(
    viewerSorts: List<DVSort>,
    storeOfRelations: StoreOfRelations
): List<DVSort> {
    return viewerSorts.ifEmpty {
        listOf(
            DVSort(
                relationKey = Relations.CREATED_DATE,
                type = DVSortType.DESC,
                includeTime = true,
                relationFormat = RelationFormat.DATE
            )
        )
    }.updateWithRelationFormat(storeOfRelations)
}