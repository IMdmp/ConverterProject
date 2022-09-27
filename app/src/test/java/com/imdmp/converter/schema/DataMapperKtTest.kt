package com.imdmp.converter.schema

import com.imdmp.converter.repository.database.entity.WalletEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

class DataMapperKtTest {
    private val DELTA = 1e-15

    @Test
    fun convertToWalletSchema() {
        val expectedCurrency = "EUR"
        val expectedBalance = Random.nextDouble()
        val startingWalletEntity = WalletEntity(expectedCurrency, expectedBalance)

        val expectedSchema = WalletSchema(expectedCurrency, expectedBalance)

        val resultSchema = startingWalletEntity.convertToWalletSchema()

        assertEquals(expectedSchema.currencyAbbrev, resultSchema.currencyAbbrev)
        assertEquals(expectedSchema.currencyValue, resultSchema.currencyValue, DELTA)
    }
}