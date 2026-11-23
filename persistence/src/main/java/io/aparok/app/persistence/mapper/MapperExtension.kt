package io.aparok.app.persistence.mapper

import io.aparok.app.data.auth.model.AccountEntity
import io.aparok.app.persistence.model.AccountTable

fun AccountTable.toEntity(): AccountEntity {
    return AccountEntity(
        id = id
    )
}

fun AccountEntity.toTable(): AccountTable {
    return AccountTable(
        id = id,
        timestamp = System.currentTimeMillis(),
        name = "",
        color = null
    )
}