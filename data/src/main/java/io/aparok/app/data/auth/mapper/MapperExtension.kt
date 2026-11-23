package io.aparok.app.data.auth.mapper

import io.aparok.app.core_models.Account
import io.aparok.app.data.auth.model.AccountEntity
import io.aparok.app.data.auth.model.WalletEntity
import io.aparok.app.domain.auth.model.Wallet

fun AccountEntity.toDomain(): Account {
    return Account(
        id = id
    )
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = id
    )
}

fun WalletEntity.toDomain(): Wallet {
    return Wallet(
        mnemonic = mnemonic
    )
}