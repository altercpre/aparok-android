package io.aparok.app.data.auth.repo

import io.aparok.app.core_models.NetworkModeConfig
import io.aparok.app.data.auth.model.AccountEntity

interface AuthCache {

    suspend fun saveAccount(account: AccountEntity)
    suspend fun updateAccount(account: AccountEntity)

    suspend fun saveMnemonic(mnemonic: String)
    suspend fun getMnemonic(): String?

    suspend fun getCurrentAccount(): AccountEntity
    suspend fun getCurrentAccountId(): String

    suspend fun logout()
    suspend fun getAccounts(): List<AccountEntity>
    suspend fun setCurrentAccount(id: String)

    suspend fun getNetworkMode(): NetworkModeConfig
    suspend fun setNetworkMode(modeConfig: NetworkModeConfig)
}