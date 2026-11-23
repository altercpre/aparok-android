package io.aparok.app.domain.layout

import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either

class GetSupportedObjectLayouts :
    BaseUseCase<List<ObjectType.Layout>, GetSupportedObjectLayouts.Params>() {

    override suspend fun run(params: Params) = safe {
        listOf(
            ObjectType.Layout.NOTE,
            ObjectType.Layout.BASIC,
            ObjectType.Layout.PROFILE,
            ObjectType.Layout.TODO,
        )
    }

    data class Params(val ctx: Id)
}