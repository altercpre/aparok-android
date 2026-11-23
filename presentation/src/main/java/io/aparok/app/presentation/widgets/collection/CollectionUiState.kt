package io.aparok.app.presentation.widgets.collection

import io.aparok.app.domain.base.Resultat
import io.aparok.app.presentation.objects.ObjectAction

class CollectionUiState(
    val views: Resultat<List<CollectionView>>,
    val showEditMode: Boolean,
    val showWidget: Boolean,
    val collectionName: String,
    val actionName: String,
    val objectActions: List<ObjectAction>,
    val inDragMode: Boolean,
    val displayType: Boolean,
    val operationInProgress: Boolean,
    val isActionButtonVisible: Boolean
)