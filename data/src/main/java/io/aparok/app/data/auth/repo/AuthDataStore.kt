package io.aparok.app.data.auth.repo

import io.aparok.app.core_models.AccountSetup
import io.aparok.app.core_models.AccountStatus
import io.aparok.app.core_models.Command
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.NetworkModeConfig
import io.aparok.app.data.auth.model.AccountEntity
import io.aparok.app.data.auth.model.WalletEntity
import kotlinx.coroutines.flow.Flow

interface AuthDataStore {

    suspend fun selectAccount(command: Command.AccountSelect): AccountSetup
    suspend fun createAccount(command: Command.AccountCreate): AccountSetup

    suspend fun preloadRemainingSpaces()

    suspend fun deleteAccount() : AccountStatus
    suspend fun restoreAccount() : AccountStatus

    suspend fun migrateAccount(account: Id, path: String)
    suspend fun cancelAccountMigration(account: Id)

    suspend fun recoverAccount()

    suspend fun saveAccount(account: AccountEntity)

    suspend fun updateAccount(account: AccountEntity)

    fun observeAccounts(): Flow<AccountEntity>

    suspend fun getCurrentAccount(): AccountEntity
    suspend fun getCurrentAccountId(): String

    suspend fun createWallet(path: String): WalletEntity
    suspend fun recoverWallet(path: String, mnemonic: String)
    suspend fun convertWallet(entropy: String): String
    suspend fun saveMnemonic(mnemonic: String)
    suspend fun getMnemonic(): String?

    suspend fun logout(clearLocalRepositoryData: Boolean)
    suspend fun getAccounts(): List<AccountEntity>
    suspend fun setCurrentAccount(id: String)

    suspend fun getVersion(): String
    suspend fun setInitialParams(command: Command.SetInitialParams)

    suspend fun getNetworkMode(): NetworkModeConfig
    suspend fun setNetworkMode(modeConfig: NetworkModeConfig)
    suspend fun debugExportLogs(dir: String): String
    suspend fun debugRunProfiler(durationInSeconds: Int): String

    suspend fun registerDeviceToken(request: Command.RegisterDeviceToken)

    suspend fun appShutdown()
}