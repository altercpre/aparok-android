package io.aparok.app.middleware.auth

import anytype.Rpc
import io.aparok.app.core_models.Account
import io.aparok.app.core_models.AccountSetup
import io.aparok.app.core_models.AccountStatus
import io.aparok.app.middleware.mappers.config
import io.aparok.app.middleware.mappers.core

fun Rpc.Account.Create.Response.toAccountSetup() : AccountSetup {
    val acc = account
    checkNotNull(acc) { "Account can't be empty" }
    val info = acc.info
    checkNotNull(info) { "Info can't be empty" }
    val status = acc.status

    return AccountSetup(
        account = Account(
            id = acc.id
        ),
        config = info.config(),
        status = status?.core() ?: AccountStatus.Unknown
    )
}

fun Rpc.Account.Select.Response.toAccountSetup(): AccountSetup {
    val acc = account
    checkNotNull(acc) { "Account can't be empty" }
    val info = acc.info
    checkNotNull(info) { "Info can't be empty" }
    val status = acc.status

    return AccountSetup(
        account = Account(acc.id),
        config = info.config(),
        status = status?.core() ?: AccountStatus.Unknown
    )
}