package io.aparok.app.middleware.converters

import anytype.Event
import io.aparok.app.data.auth.model.AccountEntity

fun Event.Account.Show.toAccountEntity(): AccountEntity {
    val acc = account
    checkNotNull(acc)
    return AccountEntity(
        id = acc.id
    )
}