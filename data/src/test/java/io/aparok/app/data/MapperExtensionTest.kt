package io.aparok.app.data

import io.aparok.app.core_models.Account
import io.aparok.app.data.auth.mapper.toDomain
import io.aparok.app.data.auth.mapper.toEntity
import io.aparok.app.data.auth.model.AccountEntity
import io.aparok.app.data.auth.model.WalletEntity
import io.aparok.app.test_utils.MockDataFactory
import org.junit.Test
import kotlin.test.assertTrue

class MapperExtensionTest {

    @Test
    fun `should map correctly wallet from data to domain layer`() {

        val wallet = WalletEntity(
            mnemonic = MockDataFactory.randomString()
        )

        val result = wallet.toDomain()

        assertTrue { result.mnemonic == wallet.mnemonic }
    }

    @Test
    fun `should map correctly account from domain to data layer`() {

        val account = Account(
            id = MockDataFactory.randomUuid()
        )

        account.toEntity().let { result ->
            assertTrue { result.id == account.id }
        }
    }

    @Test
    fun `should map correctly account from data to domain layer`() {

        val account = AccountEntity(
            id = MockDataFactory.randomUuid()
        )

        account.toDomain().let { result ->
            assertTrue { result.id == account.id }
        }
    }
}