package io.aparok.app.core_models

import io.aparok.app.core_models.restrictions.DataViewRestrictions
import io.aparok.app.core_models.restrictions.ObjectRestriction

data class ObjectView(
    val root: Id,
    val blocks: List<Block>,
    val details: Map<Id, Struct>,
    val objectRestrictions: List<ObjectRestriction>,
    val dataViewRestrictions: List<DataViewRestrictions>
)